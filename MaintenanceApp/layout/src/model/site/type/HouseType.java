package model.site.type;

public enum HouseType {
    VILLA("Villa"),
    APARTMENT("Apartment"),
    INDEPENDENT_HOUSE("Independent House");

    private final String displayName;

    HouseType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static HouseType fromString(String type) {
        for (HouseType ht : values()) {
            if (ht.displayName.equalsIgnoreCase(type)) {
                return ht;
            }
        }
        throw new IllegalArgumentException("Unknown house type: " + type);
    }
}
