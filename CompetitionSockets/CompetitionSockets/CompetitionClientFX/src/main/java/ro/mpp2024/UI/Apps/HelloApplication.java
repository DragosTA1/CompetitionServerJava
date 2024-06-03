package ro.mpp2024.UI.Apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.UI.Controllers.HelloController;
import ro.mpp2024.UI.Controllers.HomeController;
import ro.mpp2024.network.jsonprotocol.CompetitionServicesJsonProxy;
import ro.mpp2024.repository.*;
import ro.mpp2024.server.ServicesImpl;
import ro.mpp2024.services.IServices;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
//    public static IServices servicesImpl;
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";
    public HelloApplication() {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

//        IOperatorRepository operatorRepository = new OperatorRepository(props);
//
//        IGroupRepository groupRepository = new GroupRepository(props);
//
//        IContestRepository contestRepository = new ContestRepository(props);
//
//        IContestantRepository contestantRepository = new ContestantRepository(props);
//
//        IParticipationRepository participationRepository = new ParticipationRepository(props);
//        servicesImpl = new ServicesImpl(operatorRepository, contestantRepository, contestRepository, groupRepository, participationRepository);
    }
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(HelloApplication.class.getResourceAsStream("/competitionclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find competitionclient.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("competition.server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("competition.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new CompetitionServicesJsonProxy(serverIP, serverPort);

        var url = HelloApplication.class.getResource("hello-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloController loginController = fxmlLoader.getController();
        loginController.setServer(server);

        HomeController homeController = new HomeController();
        loginController.setCtrl(homeController);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}