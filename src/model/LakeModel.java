package model;

public class LakeModel {
    private String lakeName;
    private int swimmable; // 0 or 1


    public LakeModel(String lakeName, int swimmable) {
        this.lakeName = lakeName;
        this.swimmable = swimmable;
    }

    public String getLakeName() {
        return this.lakeName;
    }

    public int getSwimmable() {
        return this.swimmable;
    }
}
