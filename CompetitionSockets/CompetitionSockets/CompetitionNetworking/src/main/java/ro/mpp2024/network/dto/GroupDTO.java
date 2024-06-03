package ro.mpp2024.network.dto;

public class GroupDTO {
    private Integer minimumAge;
    private Integer maximumAge;
    private int id;

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public Integer getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(Integer maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupDTO(int id, int minimumAge, int maximumAge) {
        this.id = id;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
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
