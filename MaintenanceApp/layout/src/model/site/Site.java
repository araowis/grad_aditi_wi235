package model.site;

public class Site {
    public boolean ownershipStatus;
    protected int lengthInFeet;
    protected int breadthInFeet;
    protected boolean maintenancePaid;
    protected int id;      

    static final int FIRST_SITES_MAX = 10;
    static final int SECOND_SITES_MAX = 10;
    static final int THIRD_SITES_MAX = 15;

    private Site() {
        this.ownershipStatus = false;
        this.lengthInFeet = 0;
        this.breadthInFeet = 0;
        this.maintenancePaid = false;
        this.id = 0;
    }

    protected Site(int lengthInFeet, int breadthInFeet) {
        this.ownershipStatus = false;
        this.lengthInFeet = lengthInFeet;
        this.breadthInFeet = breadthInFeet;
        this.maintenancePaid = false;
    }


    public int area() {
        return lengthInFeet * breadthInFeet;
    }

    public boolean isOccupied() {
        return ownershipStatus;
    }

    public void setOccupied(boolean occupied) {
        this.ownershipStatus = occupied;
    }

    public int getLengthInFeet() {
        return lengthInFeet;
    }

    public void setLength(int lengthInFeet) {
        this.lengthInFeet = lengthInFeet;
    }

    public int getBreadthInFeet() {
        return breadthInFeet;
    }

    public void setBreadth(int breadthInFeet) {
        this.breadthInFeet = breadthInFeet;
    }

    public boolean isMaintenancePaid() {
        return maintenancePaid;
    }

    public void setMaintenancePaid(boolean maintenancePaid) {
        this.maintenancePaid = maintenancePaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
