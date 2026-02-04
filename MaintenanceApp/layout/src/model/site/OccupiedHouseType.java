package model.site;

public enum OccupiedHouseType {
    VILLA, APARTMENT, INDEPENDENT_HOUSE;

    @Override
    public String toString() {
        switch (this) {
            case VILLA: return "Villa";
            case APARTMENT: return "Apartment";
            case INDEPENDENT_HOUSE: return "Independent House";
            default: return super.toString();
        }
    }
    
    public static OccupiedHouseType toType(String type) {
        switch (type) {
            case "Villa": return VILLA;
            case "Apartment": return APARTMENT;
            case "Independent House": return INDEPENDENT_HOUSE;
            default: return APARTMENT;
        }
    }
}
