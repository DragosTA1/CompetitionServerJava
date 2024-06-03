package ro.mpp2024.model;

public class Group implements Identifiable<Integer> {
    public Integer minimumAge;
    public Integer maximumAge;
    private int id;
    public Integer getMinimumAge() {
        return minimumAge;
    }

    public Integer getMaximumAge() {
        return maximumAge;
    }
    public Group(int minimumAge, int maximumAge) {
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
    }
    public Group() {
        this.minimumAge = 0;
        this.maximumAge = 0;
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
        return "Group{" +
                "minimumAge=" + minimumAge +
                ", maximumAge=" + maximumAge +
                ", id=" + id +
                '}';
    }
}
