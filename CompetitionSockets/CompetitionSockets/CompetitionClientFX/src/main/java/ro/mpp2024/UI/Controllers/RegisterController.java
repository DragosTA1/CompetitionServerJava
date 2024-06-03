package ro.mpp2024.UI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IServices;

import static ro.mpp2024.UI.Apps.HelloApplication.*;

public class RegisterController {
    public IServices getServer() {
        return server;
    }

    public void setServer(IServices server) {
        this.server = server;
    }

    private IServices server;
    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    protected void onRegisterButtonClick() {
        if(userTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || cityTextField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return;
        }
        //add the operator
        try {
            server.AddOperator(userTextField.getText(), emailTextField.getText(), passwordTextField.getText(), cityTextField.getText());
        } catch (CompetitionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Registration failed!");
            alert.showAndWait();

        }
        //close the current stage
        Stage currentStage = (Stage) userTextField.getScene().getWindow();
        currentStage.close();
    }

}
