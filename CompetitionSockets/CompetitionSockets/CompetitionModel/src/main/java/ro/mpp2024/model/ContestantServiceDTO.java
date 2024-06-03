package ro.mpp2024.model;

public class ContestantServiceDTO {
    private int id;
    private String name;
    private int age;
    private String CNP;
    private int participationCount;

    public ContestantServiceDTO(Contestant contestant, int participationCount) {
        this.id = contestant.getID();
        this.name = contestant.getName();
        this.age = contestant.getAge();
        this.CNP = contestant.getCNP();
        this.participationCount = participationCount;
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
        return "ContestantServiceDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", participationCount=" + participationCount +
                '}';
    }
}
