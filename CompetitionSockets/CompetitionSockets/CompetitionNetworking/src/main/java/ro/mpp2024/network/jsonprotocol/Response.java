package ro.mpp2024.network.jsonprotocol;

import ro.mpp2024.network.dto.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    public Response() {
        this.type = null;
        this.errorMessage = null;
        this.operator = null;
        this.groups = null;
        this.contests = null;
        this.contestants = null;
        this.contestant = null;
        this.participation = null;
    }
    private ResponseType type;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    private OperatorDTO operator;

    public ResponseType getType() {
        return type;
    }
    public void setType(ResponseType type) {
        this.type = type;
    }

    public ArrayList<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupDTO> groups) {
        this.groups = groups;
    }

    private ArrayList<GroupDTO> groups;

    public ArrayList<ContestDTO> getContests() {
        return contests;
    }

    public void setContests(ArrayList<ContestDTO> contests) {
        this.contests = contests;
    }

    public ArrayList<ContestDTO> contests;

    public ArrayList<ContestantDTO> getContestants() {
        return contestants;
    }

    public void setContestants(ArrayList<ContestantDTO> contestants) {
        this.contestants = contestants;
    }

    private ArrayList<ContestantDTO> contestants;
    private ContestantDTO contestant;
    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", operator='" + operator + '\'' +
                ", groups=" + groups +
                ", contests=" + contests +
                ", contestants=" + contestants +
                '}';
    }

    public ContestantDTO getContestant() {
        return contestant;
    }
    public void setContestant(ContestantDTO contestantDTO) {
        this.contestant = contestantDTO;
    }

    public ParticipationDTO getParticipation() {
        return participation;
    }

    public void setParticipation(ParticipationDTO participation) {
        this.participation = participation;
    }

    private ParticipationDTO participation;

}
