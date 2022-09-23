import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class DBTest {

    final static String DATABASE_FILE = "SQLiteTest1.db";
    final static String CLASSES_FILE = "JITClasses.txt";
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection con;
        //Database path -- if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_FILE);
        return con;
    }

    private static void buildDatabase(boolean DBExists) throws ClassNotFoundException, SQLException {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;


        String[] arrSplit;
        int class_tuid = 1;

        BufferedReader bfReader;
        try{
            bfReader = new BufferedReader(new FileReader(CLASSES_FILE));
            con = getConnection();
            while(true){
                String line = bfReader.readLine();
                if(line == null)
                    break;
                arrSplit = trimCourseID(line).split("\\s+", 5);
                ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));

                if(!DBExists){
                    DBExists = true;

                    //check for database table existence and if it's not there, create it and add 2 records
                    state = con.createStatement();
                    res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");


                    if(!res.next()){
                        System.out.println("Building the SCHEDULE_TABLE table");
                        state2 = con.createStatement();
                        state2.executeUpdate("CREATE TABLE IF NOT EXISTS Schedule_Table(" +
                                "TUID INTEGER, " +
                                "Course_TUID INTEGER," +
                                "Section INTEGER," +
                                "Classroom_TUID INTEGER," +
                                "Professor_TUID INTEGER," +
                                "Start_Time VARCHAR(5)," +
                                "End_Time VARCHAR(5)," +
                                "Days VARCHAR(2)," +
                                "PRIMARY KEY (TUID));");
                    }
                }
                //Add a couple of records using parameters
                System.out.println("Add record 1 to Schedule Table table");
                prep = con.prepareStatement("INSERT INTO Schedule_Table VALUES(?,?,?,?,?,?,?,?);");
                prep.setInt(1,class_tuid);
                prep.setInt(2,999);
                prep.setInt(3,999);
                prep.setInt(4,999);
                prep.setInt(5,999);
                prep.setString(6, arrList.get(3));
                prep.setString(7, arrList.get(4));
                prep.setString(8, arrList.get(2));
                prep.execute();

                class_tuid++;

            }
            bfReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void buildCourses(boolean DBExists) throws ClassNotFoundException, SQLException{
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 1;

        con = getConnection();
        if(!DBExists){
            DBExists = true;

            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if(!res.next()){
                System.out.println("Building the COURSES_TABLE table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Courses_Table(" +
                        "TUID INTEGER, " +
                        "Course_ID VARCHAR(6)," +
                        "Course_Title VARCHAR(60)," +
                        "Credit_Hours INTEGER," +
                        "PRIMARY KEY (TUID));");
                 /*
                        "FOREIGN KEY (TUID)," +
                        "REFERENCES Schedule_Table(Course_TUID));"); */
            }
        }
        //Add a couple of records using parameters
        System.out.println("Add record 1 to Courses table");
        prep = con.prepareStatement("INSERT INTO Courses_Table VALUES(?,?,?,?);");
        prep.setInt(1,courses_tuid);
        prep.setString(2,"CSC ID");
        prep.setString(3,"Title");
        prep.setInt(4,999);
        prep.execute();

        courses_tuid++;
    }

    public static void buildProfessors(boolean DBExists) throws ClassNotFoundException, SQLException{
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 1;

        con = getConnection();
        if(!DBExists){
            DBExists = true;

            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if(!res.next()){
                System.out.println("Building the PROFESSORS_TABLE table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Professors_Table(" +
                        "TUID INTEGER, " +
                        "Professor_Name VARCHAR(60)," +
                        "PRIMARY KEY (TUID));");
            }
        }
        //Add a couple of records using parameters
        System.out.println("Add record 1 to Professors table");
        prep = con.prepareStatement("INSERT INTO Professors_Table VALUES(?,?);");
        prep.setInt(1,courses_tuid);
        prep.setString(2,"Professor Name");
        prep.execute();

        courses_tuid++;
    }

    public static void buildClassrooms(boolean DBExists) throws ClassNotFoundException, SQLException{
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 1;

        con = getConnection();
        if(!DBExists){
            DBExists = true;

            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if(!res.next()){
                System.out.println("Building the CLASSROOM_TABLE table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Classroom_Table(" +
                        "TUID INTEGER, " +
                        "Classroom_Name VARCHAR(1)," +
                        "Capacity INTEGER," +
                        "PRIMARY KEY (TUID));");
            }
        }
        //Add a couple of records using parameters
        System.out.println("Add record 1 to Classroom table");
        prep = con.prepareStatement("INSERT INTO Classroom_Table VALUES(?,?,?);");
        prep.setInt(1,courses_tuid);
        prep.setString(2,"Classroom Name");
        prep.setInt(3,999);
        prep.execute();

        courses_tuid++;
    }


    public static void deleteDatabase() throws ClassNotFoundException, SQLException{
        try{
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            String dropTable = "DROP DATABASE USER";
            statement.executeUpdate(dropTable);
            System.out.println("Database deleted . . . ");
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static ResultSet displayUsers() throws SQLException, ClassNotFoundException {
        Statement state;
        ResultSet res;
        Connection con = null;

        if(con == null){
            //Get Database Connection
            con = getConnection();
        }
        state = con.createStatement();
        res = state.executeQuery("SELECT TUID, Course_TUID, Section, Classroom_TUID, Professor_TUID," +
                "Start_Time, End_Time, Days FROM Schedule_Table");
        return res;
    }

    public static String trimCourseID(String strLine){
        StringBuilder strTrim = new StringBuilder();

        for(int i = 0; i < strLine.length(); i++){
            if(i != 3)
                strTrim.append(strLine.charAt(i));
        }

        return strTrim.toString();
    }


    public static void main(String[] args) {
        ResultSet res;
        Connection con;
        boolean DBExists = false;

        try {
            getConnection();
          //  deleteDatabase();
            buildDatabase(DBExists);
            buildCourses(DBExists);
            buildProfessors(DBExists);
            buildClassrooms(DBExists);


/*            //Bring back the set of user from the database
            res = displayUsers();
            //Iterate over the resultSet, print out each record's details
            while(res.next()){
                System.out.println("TUID: " + res.getInt("TUID") + "\n" +
                                   " Course_TUID: " + res.getInt("Course_TUID") + "\n" +
                                   " Section: " + res.getInt("Section") + "\n" +
                                   " Classroom_TUID: " + res.getInt("Classroom_TUID") + "\n" +
                                   " Professor_TUID: " + res.getInt("Professor_TUID") + "\n" +
                                   " Start Time: " + res.getString("Start_Time") + "\n" +
                                   " End Time: " + res.getString("End_TIme") + "\n" +
                                   " Days: " + res.getString("Days"));
            }
 */


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

/*  In order to run:
*       javac DBTest.java
*       java -classpath ".;sqlite-jdbc-3.39.3.0.jar" DBTest
*
* */
