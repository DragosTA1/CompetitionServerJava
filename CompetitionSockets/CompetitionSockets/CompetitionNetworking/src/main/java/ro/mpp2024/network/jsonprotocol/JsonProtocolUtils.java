package ro.mpp2024.network.jsonprotocol;

import ro.mpp2024.model.*;
import ro.mpp2024.network.dto.DTOUtils;

import java.util.ArrayList;

public class JsonProtocolUtils {
    public static Response createOkResponse() {
        Response response = new Response();
        response.setType(ResponseType.OK);
        return response;
    }

    public static Response createErrorResponse(String  errorMessage) {
        Response response = new Response();
        response.setErrorMessage(errorMessage);
        response.setType(ResponseType.ERROR);
        return response;
    }
    public static Response createGetGroupsResponse(Iterable<Group> groups) {
        Response response = new Response();
        response.setType(ResponseType.GET_GROUPS);
        response.setGroups(DTOUtils.getGroupDTOList(groups));
        return response;
    }
    public static Response createGetContestsAndParticipationCountByGroupResponse(Iterable<ContestServiceDTO> contests) {
        Response response = new Response();
        response.setType(ResponseType.GET_CONTESTS_AND_PARTICIPATION_COUNT_BY_GROUP);
        response.setContests(DTOUtils.getContestServiceDTOList(contests));
        return response;
    }
    public static Response createGetContestantsAndParticipationCountByContestResponse(Iterable<ContestantServiceDTO> contestants) {
        Response response = new Response();
        response.setType(ResponseType.GET_CONTESTANTS_AND_PARTICIPATION_COUNT_BY_CONTEST);
        response.setContestants(DTOUtils.getContestantServiceDTOList(contestants));
        return response;
    }
    public static Response createGetGroupByAgeResponse(Group group) {
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(group);
        Response response = new Response();
        response.setType(ResponseType.GET_GROUP_BY_AGE);
        response.setGroups(DTOUtils.getGroupDTOList(groups));
        return response;
    }
    public static Response createAddContestantResponse(Contestant contestant) {
        Response response = new Response();
        response.setType(ResponseType.ADD_CONTESTANT);
        response.setContestant(DTOUtils.getDTO(contestant));
        return response;
    }
    public static Response createGetContestsByGroupResponse(Iterable<Contest> contests) {
        Response response = new Response();
        response.setType(ResponseType.GET_CONTESTS_BY_GROUP);
        response.setContests(DTOUtils.getContestDTOList(contests));
        return response;
    }
    public static Request createLoginRequest(Operator operator) {
        Request request = new Request();
        request.setType(RequestType.LOGIN);
        request.setOperator(DTOUtils.getDTO(operator));
        return request;
    }
    public static Request createLogoutRequest(Operator operator) {
        Request request = new Request();
        request.setType(RequestType.LOGOUT);
        request.setOperator(DTOUtils.getDTO(operator));
        return request;
    }
    public static Request createRegisterRequest(Operator operator) {
        Request request = new Request();
        request.setType(RequestType.REGISTER);
        request.setOperator(DTOUtils.getDTO(operator));
        return request;
    }
    public static Request createGetGroupsRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_GROUPS);
        return request;
    }

    public static Request createGetContestsByGroupWithParticipationCountRequest(int groupId) {
        Request request = new Request();
        request.setType(RequestType.GET_CONTESTS_AND_PARTICIPATION_COUNT_BY_GROUP);
        request.setGroupId(groupId);
        return request;
    }
    public static Request createGetContestantsByContestWithParticipationCountRequest(int contestId) {
        Request request = new Request();
        request.setType(RequestType.GET_CONTESTANTS_AND_PARTICIPATION_COUNT_BY_CONTEST);
        request.setContestId(contestId);
        return request;
    }
    public static Request createGetGroupsByAgeRequest(int groupAge) {
        Request request = new Request();
        request.setType(RequestType.GET_GROUP_BY_AGE);
        request.setGroupAge(groupAge);
        return request;
    }
    public static Request createAddContestantRequest(Contestant contestant) {
        Request request = new Request();
        request.setType(RequestType.ADD_CONTESTANT);

        request.setContestant(DTOUtils.getDTO(contestant));
        return request;
    }

    public static Request createGetContestsByGroupRequest(int idGroup) {
        Request request = new Request();
        request.setType(RequestType.GET_CONTESTS_BY_GROUP);
        request.setGroupId(idGroup);
        return request;
    }
    public static Request createAddParticipationRequest(Participation participation) {
        Request request = new Request();
        request.setType(RequestType.ADD_PARTICIPATION);
        request.setParticipation(DTOUtils.getDTO(participation));
        return request;
    }

    public static Response createLoginResponse(Operator operator) {
        Response response = new Response();
        response.setType(ResponseType.LOGIN);
        response.setOperator(DTOUtils.getDTO(operator));
        return response;
    }

    public static Response createParticipationAddedResponse(Participation participation) {
        Response response = new Response();
        response.setType(ResponseType.PARTICIPATION_NOTIFICATION);
        response.setParticipation(DTOUtils.getDTO(participation));
        return response;
    }
}
