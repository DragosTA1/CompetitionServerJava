package ro.mpp2024.model;

public class Contestant implements Identifiable<Integer> {
    private String name;
    private int age;
    private String CNP;
    private Integer id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

//    public ArrayList<Contest> getContests() {
//        return contests;
//    }

//    public boolean addToContests(Contest contest) {
//        if (contests.size() < 2) {
//            contests.add(contest);
//            return true;
//        }
//        return false;
//    }
//    private final ArrayList<Contest> contests = new ArrayList<>();

    public Contestant(String name, int age, String CNP) {
        this.name = name;
        this.age = age;
        this.CNP = CNP;
        this.id = 0;
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
        return "Contestant{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", CNP='" + CNP + '\'' +
                ", id=" + id +
                '}';
    }
}

