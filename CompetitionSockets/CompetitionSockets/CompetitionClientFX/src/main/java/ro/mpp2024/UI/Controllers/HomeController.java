package ro.mpp2024.UI.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ro.mpp2024.UI.Apps.RegisterContestantApplication;
import ro.mpp2024.model.*;
import ro.mpp2024.model.ContestServiceDTO;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

import java.util.ArrayList;
import java.util.Objects;

import static ro.mpp2024.UI.Apps.HelloApplication.*;

public class HomeController implements IObserver {
    public IServices getServer() {
        return server;
    }

    public void setServer(IServices server) {
        this.server = server;
    }

    private IServices server;
    @FXML
    private Label userLabel;
    @FXML
    private TableView<Group> groupsTableView;
    @FXML
    private TableView<ContestServiceDTO> contestsTableView;
    @FXML
    private TableColumn<Group, Integer> minimumAgeColumn = new TableColumn<>();
    @FXML
    private TableColumn<Group, Integer> maximumAgeColumn = new TableColumn<>();
    @FXML
    private TableColumn<ContestServiceDTO, Integer> typeColumn = new TableColumn<>();
    @FXML
    private TableColumn<ContestServiceDTO, Integer> contestParticipationCountColumn = new TableColumn<>();
    @FXML
    private TableColumn<ContestantServiceDTO, String> nameColumn = new TableColumn<>();
    @FXML
    private TableColumn<ContestantServiceDTO, Integer> ageColumn = new TableColumn<>();
    @FXML
    TableColumn<ContestantServiceDTO, String> cnpColumn = new TableColumn<>();
    @FXML
    TableColumn<ContestantServiceDTO, Integer> participationCountColumn = new TableColumn<>();
    @FXML
    private TableView<ContestantServiceDTO> contestantsTableView;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    private Operator operator;
    private Contest selectedContest;

    public void initialize() {
        this.userLabel.setText(operator.getUsername());
        populateGroupsTableView();

        setupGroupsTableSelectionListener();
        setupContestsTableSelectionListener();
    }
    @FXML
    private void onSearchContestantsButtonClick() {
        if (selectedContest == null) {
            // alert user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Please select a contest");
            alert.showAndWait();
            return;
        }
        populateContestantsTableView(selectedContest);
    }
    public void logout() {
        try {
            server.Logout(operator, this);
        } catch (CompetitionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Logout failed");
            e.printStackTrace();
        }
    }
    @FXML
    private void onLogoutButtonClick()
    {
        // user chose OK
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.hide();
        logout();
        // open login window
//        HelloApplication helloApplication = new HelloApplication();
//        try {
//            helloApplication.start(new Stage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @FXML
    private void onRegisterContestantButtonClick() {
        RegisterContestantApplication registerContestantApplication = new RegisterContestantApplication(server);
        try {
            registerContestantApplication.start(new Stage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateContestantsTableView(Contest selectedContest) {
        //initialize columns
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        nameColumn.setText("Nume");
        ageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        ageColumn.setText("Varsta");
        cnpColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("CNP"));
        cnpColumn.setText("CNP");
        participationCountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("participationCount"));
        participationCountColumn.setText("Numar participari");

        //initialize table
        try {

            Iterable<ContestantServiceDTO> contestants = server.FindAllByContestWithParticipationCount(selectedContest.getID());
            ArrayList<ContestantServiceDTO> contestantsList = new ArrayList<>();
            contestants.forEach(contestantsList::add);
            ObservableList<ContestantServiceDTO> observableList = FXCollections.observableArrayList(contestantsList);
            contestantsTableView.getItems().clear();
            contestantsTableView.setItems(observableList);
        } catch (CompetitionException e) {
            // alert the user that the contestants weren't loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Contestants couldn't be loaded; Please restart.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    private void setupContestsTableSelectionListener() {
        contestsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedContest = newSelection.getContest();
            }
        });
    }

    private void populateGroupsTableView() {
        //initialize columns
        minimumAgeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("minimumAge"));
        minimumAgeColumn.setText("Varsta minima");
        maximumAgeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("maximumAge"));
        maximumAgeColumn.setText("Varsta maxima");

        //initialize table
        try {
            Iterable<Group> groups = server.FindAllGroups();

            ArrayList<Group> groupsList = new ArrayList<>();
            groups.forEach(groupsList::add);
            ObservableList<Group> observableList = FXCollections.observableArrayList(groupsList);
            groupsTableView.getItems().clear();
            groupsTableView.setItems(observableList);
        } catch (CompetitionException e) {
            // alert the user that the groups weren't loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Groups couldn't be loaded; Please restart.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    private void setupGroupsTableSelectionListener() {
        groupsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateContestsTableView(newSelection);
            }
        });
    }

    private void populateContestsTableView(Group newSelection) {
        //initialize columns
        typeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        typeColumn.setText("Lungime");

        contestParticipationCountColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("participationCount"));
        contestParticipationCountColumn.setText("Numar participari");

        //initialize table
        try {
            Iterable<ContestServiceDTO> contests = server.FindAllByGroupWithParticipationCount(newSelection.getID());
            ArrayList<ContestServiceDTO> contestsList = new ArrayList<>();
            contests.forEach(contestsList::add);
            ObservableList<ContestServiceDTO> observableList = FXCollections.observableArrayList(contestsList);
            contestsTableView.getItems().clear();
            contestsTableView.setItems(observableList);
        } catch (CompetitionException e) {
            // alert the user that the contests weren't loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Contests couldn't be loaded; Please restart.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    @Override
    public void participationAdded(Participation participation) {
        Platform.runLater(() -> {
                    System.out.println("Participation added: " + participation);
                    // if the participation contest is in the contestTable, repopulate it
                    if (!groupsTableView.getSelectionModel().isEmpty()) {
                        for (var item : contestsTableView.getItems()) {
                            if (Objects.equals(item.getContest().getID(), participation.getContest().getID())) {
                                populateContestsTableView(groupsTableView.getSelectionModel().getSelectedItem());
                                break;
                            }
                        }
                    }
                }
        );
    }
}
