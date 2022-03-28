package model;

/**
 * This class is used to update / store information about a single Trail entity
 */
public class TrailModel {
    private int trailId;
    private double elevationGain;
    private String trailName;
    private int trailRating;
    private double trailDistance;
    private int trailDifficulty;

    public TrailModel(int trailId, double elevationGain, String trailName, int trailRating,
                      double trailDistance, int trailDifficulty) {
        this.trailId = trailId;
        this.elevationGain = elevationGain;
        this.trailName = trailName;
        this.trailRating = trailRating;
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

    public int getTrailRating() {
        return this.trailRating;
    }

    public double getTrailDistance() {
        return this.trailDistance;
    }

    public int getTrailDifficulty() {
        return this.trailDifficulty;
    }
}
