package ro.mpp2024.model;

public class Contest implements Identifiable<Integer> {
    private final int type; //make enum

    public Group getParentGroup() {
        return parentGroup;
    }

    private final Group parentGroup;
    private int id;

    public Contest(int type, Group parentGroup) {
        this.type = type;
        this.parentGroup = parentGroup;
    }

    public int getType() {
        return type;
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
        return "Contest{" +
                "type=" + type +
                ", parentGroup=" + parentGroup +
                ", id=" + id +
                '}';
    }
}
