package model.site;

public class Site {

    private final int lengthInFeet;
    private final int breadthInFeet;
    private int id;

    protected Site(int lengthInFeet, int breadthInFeet) {
        this.lengthInFeet = lengthInFeet;
        this.breadthInFeet = breadthInFeet;
    }

    public int area() {
        return lengthInFeet * breadthInFeet;
    }

    public int getLengthInFeet() {
        return lengthInFeet;
    }

    public int getBreadthInFeet() {
        return breadthInFeet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
