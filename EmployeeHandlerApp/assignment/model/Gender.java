package model;

public enum Gender {
    MALE, FEMALE, UNKNOWN;

    public static Gender toGender(String gender) {
        Gender assgn;
        if (gender.equalsIgnoreCase("female")) 
            assgn = Gender.FEMALE;
        else if (gender.equalsIgnoreCase("male")) 
            assgn = Gender.MALE;
        else
            assgn = Gender.UNKNOWN;
        return assgn;
    }
}