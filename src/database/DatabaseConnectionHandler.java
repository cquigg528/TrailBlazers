package database;

import model.TrailModel;

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
    public Double result = 0.0;

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
        dropTablesIfExists();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE trail (trail_id integer not null PRIMARY KEY, trail_name varchar2(20) not null, trail_difficulty integer, trail_distance real, trail_elevation_gain real)");
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        TrailModel trail1 = new TrailModel(1, "Eagle Trail", 12.5, 1, 1500);
        insertTrail(trail1);

        TrailModel trail2 = new TrailModel(2, "Lighthouse Trail", 4.3, 3, 5500);
        insertTrail(trail2);

        TrailModel trail3 = new TrailModel(3, "Bear Trail", 4.3, 3, 4500);
        insertTrail(trail3);

        TrailModel trail4 = new TrailModel(4, "Rattlesnake Trail", 4.3, 1, 500);
        insertTrail(trail4);

        TrailModel trail5 = new TrailModel(5, "Rattle Trail", 4.3, 2, 1500);
        insertTrail(trail5);

        TrailModel trail6 = new TrailModel(6, "Rat Trail", 4.3, 2, 500);
        insertTrail(trail6);

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
                                                  rs.getDouble("trail_elevation_gain"));
                tempResult.add(model);
            }

//            System.out.println(tempResult);

            result = translateTrailModelsToStrings(tempResult);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            result.add(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public ArrayList<String> performSelection(String selectAttribute, String whereAttribute,
                                              String comparator, String value) {

        ArrayList<TrailModel> tempResult = new ArrayList<>();

        String prepend = "trail_";

        selectAttribute = selectAttribute == "*"? selectAttribute : prepend + selectAttribute;
        whereAttribute = prepend + whereAttribute;


        String sqlString = "SELECT _scolumn FROM trail WHERE _wcolumn _op _wvalue";
        sqlString = sqlString.replace("_scolumn", selectAttribute);
        sqlString = sqlString.replace("_wcolumn", whereAttribute);
        sqlString = sqlString.replace("_op", comparator);
        sqlString = sqlString.replace("_wvalue", value);

        TrailModel model = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);

            while (rs.next()) {
                switch (selectAttribute) {
                    case "*":
                        model = new TrailModel(rs.getInt("trail_id"),
                                rs.getString("trail_name"),
                                rs.getDouble("trail_distance"),
                                rs.getInt("trail_difficulty"),
                                rs.getDouble("trail_elevation_gain"));
                        break;
//                    case "trail_difficulty":
                        // !! TODO!!
//                        Object difficultyColumn = rs.getInt("trail_difficulty");
                    default:
                        break;
                }

                tempResult.add(model);
            }

            System.out.println(("Trail model: " + tempResult));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("here: " + sqlString);
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }


        return translateTrailModelsToStrings(tempResult);
    }

    public ArrayList<String> translateTrailModelsToStrings(ArrayList<TrailModel> models) {

        ArrayList<String> result = new ArrayList<>();

        for (TrailModel model : models) {
            String modelString = "";

            modelString += "trail_id:          "+ model.getTrailId() + "\n" + "trail_name:      " + model.getTrailName() + "\n" + "trail_difficulty: " + model.getTrailDifficulty() + "\n"
                  + "trail_distance:  " + model.getTrailDistance() + "\n" + "elevation_gain: " + model.getElevationGain();

            result.add(modelString);
        }
        return result;
    }

    public void deleteTrail(int trailId) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM trail WHERE trail_id = ?");
            ps.setInt(1, trailId);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Trail " + trailId + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("this is the problem");
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

    private void dropTablesIfExists() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select table_name from user_tables");

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("trail")) {
                    stmt.execute("DROP TABLE trail");
                    break;
                }
                if(rs.getString(1).toLowerCase().equals("lake")) {
                    stmt.execute("DROP TABLE lake");
                    break;
                }
                if(rs.getString(1).toLowerCase().equals("connects_to")) {
                    stmt.execute("DROP TABLE connects_to");
                    break;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public String performAggregation() {
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MAX(trail_distance) as MD FROM trail");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getDouble("MD");
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result.toString();
    }

    public String performNestedAggregation() {
        ResultSet rs = null;
        String result_string = "";
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MAX(t0.trail_difficulty) as TD FROM trail t0 GROUP BY t0.trail_difficulty " +
                    "HAVING MAX(t0.trail_elevation_gain) <= all (SELECT MAX(t1.trail_elevation_gain) FROM trail t1 GROUP BY t1.trail_difficulty)");
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getDouble("TD");
                result_string += ", " + result.toString();
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result_string;

    }
}