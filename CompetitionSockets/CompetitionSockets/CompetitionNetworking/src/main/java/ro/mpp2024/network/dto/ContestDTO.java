package ro.mpp2024.network.dto;

import ro.mpp2024.model.Contest;
import ro.mpp2024.model.ContestServiceDTO;

public class ContestDTO {
    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public int getParticipationCount() {
        return participationCount;
    }

    public void setParticipationCount(int participationCount) {
        this.participationCount = participationCount;
    }

    Contest contest;
    int participationCount;

    public int getType() {
        return contest.getType();
    }

    public void setType(int type) {
        this.type = type;
    }

    int type;

    public ContestDTO(Contest contest, int participationCount) {
        this.contest = contest;
        this.participationCount = participationCount;
        type = contest.getType();
    }
    public ContestDTO(ContestServiceDTO contestServiceDTO) {
        this.contest = contestServiceDTO.getContest();
        this.participationCount = contestServiceDTO.getParticipationCount();
        type = contest.getType();
    }
}
