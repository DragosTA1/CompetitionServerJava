package ro.mpp2024.server;

import ro.mpp2024.network.utils.AbstractServer;
import ro.mpp2024.network.utils.CompetitionJsonConcurrentServer;
import ro.mpp2024.repository.*;
import ro.mpp2024.services.IServices;
import ro.mpp2024.network.utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort = 55555;
    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }
        IOperatorRepository operatorRepository = new OperatorRepository(serverProps);
        IContestantRepository contestantRepository = new ContestantRepository(serverProps);
        IContestRepository contestRepository = new ContestRepository(serverProps);
        IParticipationRepository participationRepository = new ParticipationRepository(serverProps);
        IGroupRepository groupRepository = new GroupRepository(serverProps);

        IServices serverImpl = new ServicesImpl(operatorRepository, contestantRepository, contestRepository, groupRepository, participationRepository);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new CompetitionJsonConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
