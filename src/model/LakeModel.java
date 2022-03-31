package model;

public class LakeModel {
    private String lakeName;
    private boolean swimmable;


    public LakeModel(String lakeName, boolean swimmable) {
        this.lakeName = lakeName;
        this.swimmable = swimmable;
    }

    public String getLakeName() {
        return this.lakeName;
    }

    public boolean getSwimmable() {
        return this.swimmable;
    }
}
