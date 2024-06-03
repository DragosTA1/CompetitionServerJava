package ro.mpp2024.network.jsonprotocol;

import com.google.gson.Gson;
import ro.mpp2024.model.*;
import ro.mpp2024.model.ContestServiceDTO;
import ro.mpp2024.network.dto.DTOUtils;
import ro.mpp2024.network.dto.ParticipationDTO;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompetitionServicesJsonProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public CompetitionServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    @Override
    public void AddOperator(String username, String email, String password, String city) throws CompetitionException {
        initializeConnection();

        Operator operator = new Operator(username, email, password, city);
        Request req = JsonProtocolUtils.createRegisterRequest(operator);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK) {
            this.client = client;
        }
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
    }

    @Override
    public Operator MatchByUserAndPassword(Operator operator, IObserver client) throws CompetitionException {
        initializeConnection();

        System.out.println("Matching operator by user and password: " + operator.getUsername() + " " + operator.getPassword() + "(PROXY)");
        Request req = JsonProtocolUtils.createLoginRequest(operator);
        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.LOGIN) {
            this.client = client;
            return DTOUtils.getFromDTO(response.getOperator());
        }
        else {
            System.out.println("Error: " + response.getErrorMessage() + "(PROXY)");
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Closed connection " + connection);
    }

    private Response readResponse() {
        Response response = null;
        try {
            System.out.println("Waiting for response");
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) throws CompetitionException {
        String requestJson = gsonFormatter.toJson(request);
        try {
            output.println(requestJson);
            output.flush();
        } catch (Exception e) {
            throw new CompetitionException("Error sending object " + e);
        }
    }

    private void initializeConnection() throws CompetitionException {
        // make sure it is the first time we connect
        if (connection != null && !connection.isClosed()) {
            System.out.println("Already connected " + connection);
            return;
        }
        try {
            System.out.println("Connecting to server");
            gsonFormatter = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    @Override
    public Iterable<Group> FindAllGroups() throws CompetitionException {
        Request req = JsonProtocolUtils.createGetGroupsRequest();

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        return DTOUtils.getGroupListFromDTO(response.getGroups());
    }

    @Override
    public Group FindGroupByAge(int age) throws CompetitionException {
        Request req = JsonProtocolUtils.createGetGroupsByAgeRequest(age);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        // we know there is only one group
        return DTOUtils.getGroupListFromDTO(response.getGroups()).iterator().next();
    }

    @Override
    public Iterable<Contestant> FindAllByContest(Integer idContest) {
        return null;
    }

    @Override
    public Iterable<ContestantServiceDTO> FindAllByContestWithParticipationCount(Integer idContest) throws CompetitionException {
        Request req = JsonProtocolUtils.createGetContestantsByContestWithParticipationCountRequest(idContest);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        return DTOUtils.getContestantServiceDTOListFromDTO(response.getContestants());

    }

    @Override
    public Iterable<Contestant> findAllByGroup(Integer idGroup) {
        return null;
    }

    @Override
    public Iterable<Contest> FindAllByGroup(int idGroup) throws CompetitionException {
        Request req = JsonProtocolUtils.createGetContestsByGroupRequest(idGroup);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        return DTOUtils.getContestListFromDTO(response.getContests());
    }

    @Override
    public Iterable<ContestServiceDTO> FindAllByGroupWithParticipationCount(int idGroup) throws CompetitionException {
        System.out.println("Finding all contests by group with participation count: " + idGroup + "(PROXY)");
        Request req = JsonProtocolUtils.createGetContestsByGroupWithParticipationCountRequest(idGroup);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        return DTOUtils.getContestServiceDTOListFromDTO(response.getContests());
    }

    @Override
    public void AddContestant(String name, int age, String cnp, IObserver client) {

    }

    @Override
    public Contestant AddContestant(String name, int age, String cnp) throws CompetitionException {
        System.out.println("Adding contestant: " + name + " " + age + " " + cnp + "(PROXY)");
        Contestant contestant = new Contestant(name, age, cnp);
        Request req = JsonProtocolUtils.createAddContestantRequest(contestant);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
//        System.out.println("Returning contestant: " + response.getContestant().getId() + "(PROXY)");
        return DTOUtils.getFromDTO(response.getContestant());
    }

    @Override
    public void AddParticipation(Contestant contestant, Contest contest) throws CompetitionException {
        Participation participation = new Participation(contestant, contest, new Date());
        Request req = JsonProtocolUtils.createAddParticipationRequest(participation);

        sendRequest(req);
        Response response = readResponse();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new CompetitionException(err);
        }
        // OK
    }

    @Override
    public void Logout(Operator operator, IObserver client) throws CompetitionException {
        Request req = JsonProtocolUtils.createLogoutRequest(operator);

        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new CompetitionException(err);
        }
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if(response == null){
                        return;
                    }
                    else if(isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleUpdate(Response response) {
        if(response.getType() == ResponseType.PARTICIPATION_NOTIFICATION) {
            ParticipationDTO participationDTO = response.getParticipation();
            Participation participation = DTOUtils.getFromDTO(participationDTO);
            try {
                client.participationAdded(participation);
            } catch (CompetitionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response)
    {
        return response.getType() == ResponseType.PARTICIPATION_NOTIFICATION;
    }
}
