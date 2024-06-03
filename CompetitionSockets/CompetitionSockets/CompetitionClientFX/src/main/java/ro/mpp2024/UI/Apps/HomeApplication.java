package ro.mpp2024.UI.Apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.UI.Controllers.HomeController;
import ro.mpp2024.model.Operator;
import ro.mpp2024.model.Participation;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

public class HomeApplication extends Application implements IObserver {
    private final HomeController ctrl;
    private IServices server;
    public Operator operator;
    @Override
    public void start(Stage stage) throws Exception {
        //create a new scene for registering
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("home-view.fxml"));
            HomeController homeController = ctrl;
            homeController.setServer(server);
            homeController.setOperator(operator);
            fxmlLoader.setController(homeController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HomeApplication(IServices server, HomeController ctrl, Operator operator) {
        this.server = server;
        this.operator = operator;
        this.ctrl = ctrl;
    }

    @Override
    public void participationAdded(Participation participation) {

    }
}
