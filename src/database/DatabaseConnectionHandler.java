package database;

import model.TrailModel;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;


/**
 * This class handles all database related transactions
 *
 * Code adapted from cs304 JavaDemo file
 */
public class DatabaseConnectionHandler {

    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";

    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void databaseSetup() {
        dropTrailTableIfExists();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE trail (trail_id integer not null PRIMARY KEY, trail_name varchar2(20) not null, trail_difficulty integer, trail_distance real, elevation_gain real)");
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        TrailModel trail1 = new TrailModel(1, "Eagle Trail", 3200.00, 3, 12.5);
        insertTrail(trail1);

        TrailModel trail2 = new TrailModel(2, "Beetle bun head", 9830.00, 6, 5.5);
        insertTrail(trail2);

    }

    public void insertTrail(TrailModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO trail VALUES (?,?,?,?,?)");
            ps.setInt(1, model.getTrailId());
            ps.setString(2, model.getTrailName());
            ps.setInt(3, model.getTrailDifficulty());
            ps.setDouble(4, model.getTrailDistance());
            ps.setDouble(5, model.getElevationGain());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ArrayList<String> getTrailInfo() {
        ArrayList<TrailModel> tempResult = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trail");

            System.out.println("getting trail info");

            while (rs.next()) {
                TrailModel model = new TrailModel(rs.getInt("trail_id"),
                                                  rs.getString("trail_name"),
                                                  rs.getDouble("trail_distance"),
                                                  rs.getInt("trail_difficulty"),
                                                  rs.getDouble("elevation_gain"));
                tempResult.add(model);
            }

            System.out.println(tempResult);

            result = translateModelsToStrings(tempResult);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            result.add(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public ArrayList<String> translateModelsToStrings(ArrayList<TrailModel> models) {

        ArrayList<String> result = new ArrayList<>();

        for (TrailModel model : models) {
            String modelString = "Trail: ";

            modelString += model.getTrailId() + " " + model.getTrailName() + " " + model.getTrailDifficulty() + " "
                    + model.getTrailDistance() + " " + model.getElevationGain();

            result.add(modelString);
        }
        return result;
    }

    public void deleteTrail(int trailId) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM trails WHERE trail_id = ?");
            ps.setInt(1, trailId);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Trail " + trailId + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void dropTrailTableIfExists() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select table_name from user_tables");

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("branch")) {
                    stmt.execute("DROP TABLE trail");
                    break;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}