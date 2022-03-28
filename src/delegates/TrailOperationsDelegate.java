package delegates;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TrailOperations class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the
 * controller class (in this case TrailManager).
 *
 * TrailOperations calls the methods that we have listed below but
 * TrailManager is the actual class that will implement the methods.
 */
public interface TrailOperationsDelegate {
    public void databaseSetup();
    public void deleteTrail(int trailId);

    // add selection, projection, join, nested aggregation baby


}