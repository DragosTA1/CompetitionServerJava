package ro.mpp2024.network.jsonprotocol;

import ro.mpp2024.model.*;
import ro.mpp2024.network.dto.ContestantDTO;
import ro.mpp2024.network.dto.DTOUtils;
import ro.mpp2024.network.dto.OperatorDTO;
import ro.mpp2024.network.dto.ParticipationDTO;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CompetitionClientJsonWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;
    public CompetitionClientJsonWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(connected) {
            try {
                System.out.println("Waiting for request ...");
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                System.out.println("Received request " + request);
                Response response = handleRequest(request);
                if (response != null) {
                    System.out.println("Prepared response " + response);
                    sendResponse(response);
                } else {
                    System.out.println("No response prepared");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Sleeping ...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Worker stopped");
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    private static final Response okResponse = JsonProtocolUtils.createOkResponse();
    private void sendResponse(Response response) {
       String responseLine = gsonFormatter.toJson(response);
        System.out.println("Sending response " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if(request.getType() == RequestType.LOGIN) {
            System.out.println("Login request ...");
            OperatorDTO operatorDTO = request.getOperator();
            Operator operator = DTOUtils.getFromDTO(operatorDTO);
            try {
                operator = server.MatchByUserAndPassword(operator, this);
                if (operator != null) {
                    return JsonProtocolUtils.createLoginResponse(operator);
                }
                 else{
                    throw new CompetitionException("User or password are wrong!");
                }
            } catch (CompetitionException e){
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.REGISTER) {
            System.out.println("Register request ...");
            OperatorDTO operatorDTO = request.getOperator();
            Operator operator = DTOUtils.getFromDTO(operatorDTO);
            try {
                server.AddOperator(operator.getUsername(), operator.getEmail(), operator.getPassword(), operator.getCity());
                return okResponse;
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_GROUPS) {
            System.out.println("Get groups request ...");
            try {
                Iterable<Group> groups = server.FindAllGroups();
                return JsonProtocolUtils.createGetGroupsResponse(groups);
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_CONTESTS_AND_PARTICIPATION_COUNT_BY_GROUP) {
            System.out.println("Get contests and participation count by group request ...");
            int groupId = request.getGroupId();
            try {
                return JsonProtocolUtils.createGetContestsAndParticipationCountByGroupResponse(server.FindAllByGroupWithParticipationCount(groupId));
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_CONTESTANTS_AND_PARTICIPATION_COUNT_BY_CONTEST) {
            System.out.println("Get contestants and participation count by contest request ...");
            int contestId = request.getContestId();
            try {
                return JsonProtocolUtils.createGetContestantsAndParticipationCountByContestResponse(server.FindAllByContestWithParticipationCount(contestId));
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_GROUP_BY_AGE) {
            System.out.println("Get groups by age request ...");
            int groupAge = request.getGroupAge();
            try {
                return JsonProtocolUtils.createGetGroupByAgeResponse(server.FindGroupByAge(groupAge));
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.ADD_CONTESTANT) {
            System.out.println("Add contestant request ...");
            try {
                ContestantDTO contestantDTO = request.getContestant();
                System.out.println("WORKER: Adding contestant: " + contestantDTO.getId() + contestantDTO.getName() + " " + contestantDTO.getAge() + " " + contestantDTO.getCNP());
                Contestant c = server.AddContestant(contestantDTO.getName(), contestantDTO.getAge(), contestantDTO.getCNP());
                System.out.println("WORKER: Added contestant: " + c.getID() + c.getName() + " " + c.getAge() + " " + c.getCNP());
                return JsonProtocolUtils.createAddContestantResponse(c);
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.GET_CONTESTS_BY_GROUP) {
            System.out.println("Get contests by group request ...");
            int groupId = request.getGroupId();
            try {
                return JsonProtocolUtils.createGetContestsByGroupResponse(server.FindAllByGroup(groupId));
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.ADD_PARTICIPATION) {
            System.out.println("Add participation request ...");
            ParticipationDTO participation = request.getParticipation();
            Contestant contestant = participation.getContestant();
            Contest contest = participation.getContest();
            try {
                server.AddParticipation(contestant, contest);
                return okResponse;
            } catch (CompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == RequestType.LOGOUT) {
            System.out.println("Logout request ...");
            OperatorDTO operatorDTO = request.getOperator();
            Operator operator = DTOUtils.getFromDTO(operatorDTO);
            try {
                server.Logout(operator, this);
                connected = false;
                return okResponse;
            } catch (CompetitionException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void participationAdded(Participation participation) throws CompetitionException {
        Response resp = JsonProtocolUtils.createParticipationAddedResponse(participation);
        System.out.println("Participation added " + participation);
        sendResponse(resp);
    }
}
