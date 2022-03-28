package controller;

import database.DatabaseConnectionHandler;
import delegates.LoginWindowDelegate;
import delegates.TrailOperationsDelegate;
import model.TrailModel;
import ui.LoginWindow;

/**
 * Main controller class.  Based on cs304 JavaDemo
 */
public class TrailManager implements LoginWindowDelegate, TrailOperationsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

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

//            TerminalTransactions transaction = new TerminalTransactions();
//            transaction.setupDatabase(this);
//            transaction.showMainMenu(this);
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

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        TrailManager trailManager = new TrailManager();
        trailManager.start();
    }

}
