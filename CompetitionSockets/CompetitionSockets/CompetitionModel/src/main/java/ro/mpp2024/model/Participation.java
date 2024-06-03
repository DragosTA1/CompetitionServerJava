package ro.mpp2024.model;

import java.util.Date;

public class Participation implements Identifiable<Integer> {
    private int id;

    private Contestant contestant;
    private Contest contest;
    private final Date date;

    public Participation(Contestant contestant, Contest contest, Date date) {
        this.contestant = contestant;
        this.contest = contest;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    @Override
    public Integer getID() {
        return id;
    }
    @Override
    public void setID(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Participation{" +
                "\ncontestant=" + contestant +
                ", \ncontest=" + contest +
                ", \ndate=" + date +
                ", id=" + id +
                '}';
    }
}
