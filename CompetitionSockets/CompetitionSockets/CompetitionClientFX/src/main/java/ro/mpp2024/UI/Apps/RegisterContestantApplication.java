package ro.mpp2024.UI.Apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.UI.Controllers.RegisterContestantController;
import ro.mpp2024.services.IServices;

public class RegisterContestantApplication extends Application {
    private final IServices server;
    public RegisterContestantApplication(IServices server) {
        this.server = server;
    }

    @Override
    public void start(Stage stage) throws Exception {
        //create a new scene for registering
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("register-contestant-view.fxml"));
        RegisterContestantController registerContestantController = new RegisterContestantController();
        registerContestantController.setServer(server);
        fxmlLoader.setController(registerContestantController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }
}
