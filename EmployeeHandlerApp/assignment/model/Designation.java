package model;

public enum Designation {
    CLERK, PROGRAMMER, MANAGER, UNASSIGNED;

    public static Designation toDesgination(String designation) {
        Designation assgn;
        if (designation.equalsIgnoreCase("CLERK")) 
            assgn = Designation.CLERK;
        else if (designation.equalsIgnoreCase("PROGRAMMER")) 
            assgn = Designation.PROGRAMMER;
        else if (designation.equalsIgnoreCase("MANAGER")) 
            assgn = Designation.MANAGER;
        else
            assgn = Designation.UNASSIGNED;
        return assgn;
    }
}
