package database;

import model.LakeModel;
import model.TrailModel;
import model.ConnectsToModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;


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
        dropConnectsToTablesIfExists();
        dropTrailTablesIfExists();
        dropLakeTablesIfExists();

        try {
            Statement statementLake = connection.createStatement();
            statementLake.executeUpdate("CREATE TABLE lake (lake_name varchar2(20) not null PRIMARY KEY, swimmable int not null)");
            statementLake.close();

            Statement statementTrail = connection.createStatement();
            statementTrail.executeUpdate("CREATE TABLE trail (trail_id integer not null PRIMARY KEY, trail_name varchar2(20) " +
                    "not null, trail_difficulty integer, trail_distance real, trail_elevation_gain real)");
            statementTrail.close();

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE connects_to " +
                    "(trail_id integer not null, lake_name varchar2(20) not null, " +
                    "PRIMARY KEY (trail_id, lake_name)," +
                    "CONSTRAINT FK_lake_name FOREIGN KEY (lake_name) REFERENCES lake(lake_name)," +
                    "CONSTRAINT FK_trail_id FOREIGN KEY (trail_id) REFERENCES trail(trail_id))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        TrailModel trail1 = new TrailModel(1, "Eagle Trail", 12.5, 3,
                1200);
        TrailModel trail2 = new TrailModel(2, "Lighthouse Trail", 4.3, 6,
                5500);
        TrailModel trail3 = new TrailModel(3, "Bear Trail", 9.5, 2,
                2000);

        LakeModel lake1 = new LakeModel("Great Bear Lake", 1);
        LakeModel lake2 = new LakeModel("Williams Lake", 1);
        LakeModel lake3 = new LakeModel("Ontario", 0);

        ConnectsToModel connects1 = new ConnectsToModel(1, "Great Bear Lake");
        ConnectsToModel connects2 = new ConnectsToModel(2, "Williams Lake");
        ConnectsToModel connects3 = new ConnectsToModel(3, "Ontario");

        insertTrail(trail1);
        insertTrail(trail2);
        insertTrail(trail3);

        insertLake(lake1);
        insertLake(lake2);
        insertLake(lake3);

        insertConnectsTo(connects1);
        insertConnectsTo(connects2);
        insertConnectsTo(connects3);

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
            System.out.println("problem in insertTrail");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertLake(LakeModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO lake VALUES (?,?)");
            ps.setString(1, model.getLakeName());
            ps.setInt(2, model.getSwimmable());

            ps.executeUpdate();
            connection.commit();


            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertConnectsTo(ConnectsToModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO connects_to VALUES (?,?)");
            ps.setInt(1, model.getConnectsToTrailId());
            ps.setString(2, model.getConnectsToLakeName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            result = translateTrailModelsToStrings(tempResult);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            result.add(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public ArrayList<String> getLakeInfo() {
        ArrayList<LakeModel> tempResult = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM lake");

            while (rs.next()) {
                LakeModel model = new LakeModel(rs.getString("lake_name"),
                        rs.getInt("swimmable"));
                tempResult.add(model);
            }
            result = translateLakeModelsToStrings(tempResult);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            result.add(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public ArrayList<String> getConnectionInfo() {
        ArrayList<ConnectsToModel> tempResult = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM connects_to");

            while (rs.next()) {
                ConnectsToModel model = new ConnectsToModel(rs.getInt("trail_id"),
                        rs.getString("lake_name"));
                tempResult.add(model);
            }
            result = translateConnectsToModelsToStrings(tempResult);

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

    public ArrayList<String> translateLakeModelsToStrings(ArrayList<LakeModel> models) {

        ArrayList<String> result = new ArrayList<>();

        for (LakeModel model : models) {
            String modelString = "";

            String swimmable = model.getSwimmable() == 1? "YES" : "NO";

            modelString += "lake_name:   " + model.getLakeName() + "\n" + "swimmable: " + swimmable;

            result.add(modelString);
        }
        return result;
    }

    public ArrayList<String> translateConnectsToModelsToStrings(ArrayList<ConnectsToModel> models) {

        ArrayList<String> result = new ArrayList<>();

        for (ConnectsToModel model : models) {
            String modelString = "";

            modelString += "trail_id:  " + model.getConnectsToTrailId()  + "\n" + "lake_name:   "+ model.getConnectsToLakeName();

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

    private void dropTrailTablesIfExists() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select table_name from user_tables");
            while(rs.next()) {
                System.out.println(rs.getString(1).toLowerCase());
                if(rs.getString(1).toLowerCase().equals("trail")) {
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

    private void dropLakeTablesIfExists() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select table_name from user_tables");
            while(rs.next()) {
                System.out.println(rs.getString(1).toLowerCase());
                if(rs.getString(1).toLowerCase().equals("lake")) {
                    stmt.execute("DROP TABLE lake");
                    break;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void dropConnectsToTablesIfExists() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select table_name from user_tables");
            while(rs.next()) {
                System.out.println(rs.getString(1).toLowerCase());
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
}