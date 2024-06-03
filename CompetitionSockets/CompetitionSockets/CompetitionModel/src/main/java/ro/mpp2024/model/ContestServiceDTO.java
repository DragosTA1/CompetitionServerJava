package ro.mpp2024.model;

public class ContestServiceDTO {
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

    public ContestServiceDTO(Contest contest, int participationCount) {
        this.contest = contest;
        this.participationCount = participationCount;
        type = contest.getType();
    }
}
