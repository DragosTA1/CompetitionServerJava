package ro.mpp2024.network.dto;

import ro.mpp2024.model.Contest;
import ro.mpp2024.model.Contestant;

import java.io.Serializable;
import java.util.Date;

public class ParticipationDTO implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contestant getContestant() {
        return contestant;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    private Contestant contestant;
    private Contest contest;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    private String dateString;
    public ParticipationDTO() {}
    public ParticipationDTO(Contestant contestant, Contest contest, Date date) {
        this.contestant = contestant;
        this.contest = contest;
        this.dateString = date.toString();
        this.id = 0;
    }
    public ParticipationDTO(Contestant contestant, Contest contest) {
        this.contestant = contestant;
        this.contest = contest;
        this.id = 0;
        this.dateString = new Date().toString();
    }
    public ParticipationDTO(int id, Contestant contestant, Contest contest, Date date) {
        this.id = id;
        this.contestant = contestant;
        this.contest = contest;
        this.dateString = date.toString();
    }
}
