package model;

/**
 * This class is used to update / store information about a single Trail entity
 */
public class ConnectsToModel {
    private int trailId;
    private String lakeName;



    public ConnectsToModel(int trailId, String lakeName) {
        this.trailId = trailId;
        this.lakeName = lakeName;
    }

    public int getConnectsToTrailId() {
        return this.trailId;
    }

    public String getConnectsToLakeName() {
        return this.lakeName;
    }

}