package ro.mpp2024.network.jsonprotocol;

import ro.mpp2024.model.Contestant;
import ro.mpp2024.network.dto.ContestantDTO;
import ro.mpp2024.network.dto.GroupDTO;
import ro.mpp2024.network.dto.OperatorDTO;
import ro.mpp2024.network.dto.ParticipationDTO;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    private RequestType type;
    private OperatorDTO operator;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private int groupId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    private int contestId;

//    public Iterable<GroupDTO> getGroups() {
//        return groups;
//    }
//
//    public void setGroups(Iterable<GroupDTO> groups) {
//        this.groups = groups;
//    }
//
//    private Iterable<GroupDTO> groups;
    private int groupAge;
    private ContestantDTO contestant;
    public Request() {
    }
    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", operator=" + operator +
                ", groupId=" + groupId +
                ", contestId=" + contestId +
                ", groupAge=" + groupAge +
                ", contestant=" + contestant +
                '}';
    }

    public int getGroupAge() {
        return groupAge;
    }

    public void setGroupAge(int groupAge) {
        this.groupAge = groupAge;
    }

    public ContestantDTO getContestant() {
        return contestant;
    }

    public void setContestant(ContestantDTO contestant) {
        this.contestant = contestant;
    }

    public ParticipationDTO getParticipation() {
        return participation;
    }

    public void setParticipation(ParticipationDTO participation) {
        this.participation = participation;
    }

    private ParticipationDTO participation;
}
