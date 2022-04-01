package controller;

import database.DatabaseConnectionHandler;
import delegates.LoginWindowDelegate;
import delegates.TrailOperationsDelegate;
import ui.LoginWindow;
import ui.TrailOperationsWindow;

import java.util.ArrayList;

/**
 * Main controller class.  Based on cs304 JavaDemo
 */
public class TrailManager implements LoginWindowDelegate, TrailOperationsDelegate {
    private DatabaseConnectionHandler dbHandler;
    private LoginWindow loginWindow;
    private TrailOperationsWindow operationsWindow = null;

    public TrailManager() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            operationsWindow = new TrailOperationsWindow();
            operationsWindow.setupDatabase(this);
            operationsWindow.showFrame(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public void deleteTrail(int trailId) {
        dbHandler.deleteTrail(trailId);
    }

    public void databaseSetup() {
        dbHandler.databaseSetup();
    }

    public ArrayList<String> showTrailInfo() {
        return dbHandler.getTrailInfo();
    }

    public ArrayList<String> showLakeInfo() {
        return dbHandler.getLakeInfo();
    }

    public ArrayList<String> showConnectionInfo() {
        return dbHandler.getConnectionInfo();
    }

    @Override
    public void performJoinSearch(String selection) {
        ArrayList<String> results = dbHandler.performJoinSearch(selection);
        operationsWindow.displaySearchResults(results);
    }

    @Override
    public void performSelection(String selectAttribute, String whereAttribute, String comparator, String value) {
        ArrayList<String> results = dbHandler.performSelection(selectAttribute,whereAttribute, comparator, value);
        operationsWindow.displaySearchResults(results);

    }

    @Override
    public void performAggregation() {
        String result = dbHandler.performAggregation();
        operationsWindow.displayAggregateResults(result);
    }

    @Override
    public void peformNestedAggregation() {
        String result = dbHandler.performNestedAggregation();
        operationsWindow.displayNestedAggregateResults(result);
    }

    public void trailOperationsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);

    }

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        TrailManager trailManager = new TrailManager();
        trailManager.start();
    }

}
