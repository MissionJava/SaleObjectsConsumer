package se.quedro.utils;

public enum PriorityOrderAttribute {
    CITY("City"),
    SQUAREMETERS("SquareMeters"),
    PRICEPERSQUAREMETER("PricePerSquareMeter"),
    NONE("None");

    private String attribute;

    PriorityOrderAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}