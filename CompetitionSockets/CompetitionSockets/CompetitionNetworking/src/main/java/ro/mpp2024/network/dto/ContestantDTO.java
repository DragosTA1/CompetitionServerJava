package ro.mpp2024.network.dto;

import ro.mpp2024.model.Contestant;

public class ContestantDTO {
    private int id;
    private String name;
    private int age;
    private String CNP;
    private int participationCount;

    public ContestantDTO(Contestant contestant, int participationCount) {
        this.id = contestant.getID();
        this.name = contestant.getName();
        this.age = contestant.getAge();
        this.CNP = contestant.getCNP();
        this.participationCount = participationCount;
    }

    public ContestantDTO(String name, int age, String CNP) {
        this.name = name;
        this.age = age;
        this.CNP = CNP;
        this.id = 0;
        this.participationCount = 0;
    }
    public ContestantDTO(int id, String name, int age, String CNP) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.CNP = CNP;
        this.participationCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getParticipationCount() {
        return participationCount;
    }
    public String getCNP() {
        return CNP;
    }
    public Contestant getContestant(){
        var c =new Contestant(name,age,CNP);
        c.setID(id);
        return c;
    }

    @Override
    public String toString() {
        return "ContestantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", participationCount=" + participationCount +
                '}';
    }
}
