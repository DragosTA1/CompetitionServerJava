package ro.mpp2024.UI.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.mpp2024.model.Contest;
import ro.mpp2024.model.Contestant;
import ro.mpp2024.model.Group;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IServices;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static ro.mpp2024.UI.Apps.HelloApplication.*;

public class RegisterContestantController implements Initializable {
    private IServices server;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField cnpTextField;
    @FXML
    private TableView<Contest> contestsTableView;
    @FXML
    private TableColumn<Contest, Integer> typeColumn = new TableColumn<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contestsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(newValue.length() > 2){
                ageTextField.setText(oldValue);
            }
        });

        //if ageTextField changes, update the value of the table with the contests that are from the group with the age
        ageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            //get the value of the age
            if(newValue.isEmpty()){
                return;
            }
            int age = Integer.parseInt(newValue);
            if(age >= 6 && age <= 15) {
                //get the contests that are from the group with the age
                try {
                    Group grp = server.FindGroupByAge(age);
                    if (grp != null)
                        populateContestsTableView(grp);
                    else
                        contestsTableView.getItems().clear();
                } catch (CompetitionException e) {
                    // alert the user that the contestants weren't loaded
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Entities couldn't be loaded");
                    alert.showAndWait();

                    e.printStackTrace();
                }
            } else {
                contestsTableView.getItems().clear();
            }
        });
    }
    private void populateContestsTableView(Group newSelection) throws CompetitionException {
        //initialize columns
        typeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        typeColumn.setText("Lungime");

        //initialize table
        Iterable<Contest> contests = server.FindAllByGroup(newSelection.getID());
        ArrayList<Contest> contestsList = new ArrayList<>();
        contests.forEach(contestsList::add);
        ObservableList<Contest> observableList = FXCollections.observableArrayList(contestsList);
        contestsTableView.getItems().clear();
        contestsTableView.setItems(observableList);
    }
    @FXML
    private void onRegisterButtonClick() {
        ObservableList<Contest> selectedItems = contestsTableView.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty() || selectedItems.size() > 2)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select between 1 and 2 contests");
            alert.showAndWait();
            return;
        }

        if(nameTextField.getText().isEmpty() || nameTextField.getText().length() > 100) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in the name accordingly");
            alert.showAndWait();
            return;
        }

//        if(!servicesImpl.validateCNP(cnpTextField.getText())) {
        if(cnpTextField.getText().length() != 13) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in the CNP accordingly");
            alert.showAndWait();
            return;
        }

        String name = nameTextField.getText();
        int age = Integer.parseInt(ageTextField.getText());
        String cnp = cnpTextField.getText();
        //register the contestant
        try {
            Contestant c = server.AddContestant(name, age, cnp);
            System.out.println(c.getID() + " " + c.getName() + " " + c.getAge() + " " + c.getCNP());

            selectedItems.forEach(contest -> {
                //register the participation
                try {
                    server.AddParticipation(c, contest);
                } catch (CompetitionException e) {
                    // alert the user that the participation couldn't be added
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Error adding the participation");
                    alert.showAndWait();

                    e.printStackTrace();
                }
            });

            //close
            Stage currentStage = (Stage) nameTextField.getScene().getWindow();
            currentStage.close();
        } catch (CompetitionException e) {
            // alert the user that the contestant couldn't be added
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error adding the contestant");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    public IServices getServer() {
        return server;
    }

    public void setServer(IServices server) {
        this.server = server;
    }
}
