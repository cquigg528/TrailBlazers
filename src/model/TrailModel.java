package model;

/**
 * This class is used to update / store information about a single Trail entity
 */
public class TrailModel {
    private int trailId;
    private String trailName;
    private int trailDifficulty;
    private double trailDistance;
    private double elevationGain;



    public TrailModel(int trailId, String trailName, double trailDistance,
                       int trailDifficulty, double elevationGain) {
        this.trailId = trailId;
        this.elevationGain = elevationGain;
        this.trailName = trailName;
        this.trailDistance = trailDistance;
        this.trailDifficulty = trailDifficulty;
    }

    public int getTrailId() {
        return this.trailId;
    }

    public double getElevationGain() {
        return this.elevationGain;
    }

    public String getTrailName() {
        return this.trailName;
    }

    public double getTrailDistance() {
        return this.trailDistance;
    }

    public int getTrailDifficulty() {
        return this.trailDifficulty;
    }
}
