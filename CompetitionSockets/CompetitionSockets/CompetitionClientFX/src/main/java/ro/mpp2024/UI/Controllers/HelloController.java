package ro.mpp2024.UI.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.mpp2024.UI.Apps.HomeApplication;
import ro.mpp2024.UI.Apps.RegisterApplication;
import ro.mpp2024.model.Operator;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IServices;

public class HelloController {
    public IServices getServer() {
        return server;
    }

    public void setServer(IServices server) {
        this.server = server;
    }

    private IServices server;

    public HomeController getCtrl() {
        return ctrl;
    }

    public void setCtrl(HomeController ctrl) {
        this.ctrl = ctrl;
    }

    private HomeController ctrl;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField userTextField;
    @FXML
    private Button registerButton;
    @FXML
    protected void onLoginButtonClick() throws CompetitionException {
        if(userTextField.getText().isEmpty() || passwordTextField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return;
        }
        try {
                Operator operator = server.MatchByUserAndPassword(new Operator(userTextField.getText(), passwordTextField.getText()), ctrl);
                if(operator != null) {
                    //Get the current Stage
                    Stage currentStage = (Stage) userTextField.getScene().getWindow();
                    userTextField.setText("");
                    passwordTextField.setText("");
                    try {
                        HomeApplication homeApplication = new HomeApplication(server, ctrl, operator);
                        Stage stage = new Stage();
                        homeApplication.start(stage);
                        currentStage.hide();
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                ctrl.logout();
                                System.exit(0);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        } catch (CompetitionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    protected void onRegisterButtonClick() {
      //create a new scene for registering
        //Get the current Stage
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        try {
//            currentStage.close();
            RegisterApplication registerApplication = new RegisterApplication(server);
            registerApplication.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}