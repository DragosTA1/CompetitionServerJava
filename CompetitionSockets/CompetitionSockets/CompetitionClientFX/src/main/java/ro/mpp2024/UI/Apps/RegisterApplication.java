package ro.mpp2024.UI.Apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.UI.Controllers.RegisterController;
import ro.mpp2024.services.IServices;

public class RegisterApplication extends Application {
    IServices server;
    @Override
    public void start(Stage stage) throws Exception {
        //create a new scene for registering
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        RegisterController registerController =  fxmlLoader.getController();
        registerController.setServer(server);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }
    public RegisterApplication(IServices server) {
        this.server = server;
    }
}
