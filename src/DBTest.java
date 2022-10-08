import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class DBTest extends Scheduler {

    final static String DATABASE_FILE = "SQLiteTest1.db";
    final static String COPY_FILE = "COPY.txt";
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<String> arlScheduledClasses = new ArrayList<>();
    static final int CLASS_ID = 0;
    static final int CLASS_PROFESSOR = 1;
    static final int CLASS_DAYS = 2;
    static final int CLASS_START_TIME = 3;
    static final int CLASS_END_TIME = 4;


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
        ArrayList<String> arlClasses = new ArrayList<>();
        Scheduler scheduler = new Scheduler();
        ArrayList<String> arlScheduled = new ArrayList<>(getClasses());
        try {
            //      bfReader = new BufferedReader(new FileReader(CLASSES_FILE));
            con = getConnection();

            if (!DBExists) {
                DBExists = true;

                //check for database table existence and if it's not there, create it and add 2 records
                state = con.createStatement();
                res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Schedule_Table'");
                if (!res.next()) {
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
            String tableCount = "SELECT count(*) FROM Schedule_Table";
            prep = con.prepareStatement(tableCount);
            ResultSet rs = prep.executeQuery();
            // System.out.println(rs.getString(1));
            int result = Integer.parseInt(rs.getString(1));

            if (result == 0) {
                for (String s : arlScheduled
                ) {
                    arrSplit = s.split("\\s+", 7);
                    ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));
                    String[] arrSplitTimes = arrList.get(2).split("-", 2);
                    //arlClasses.add(arrList.get(CLASS_ID));
                    arlScheduledClasses.add(arrSplit[0] + " " + arrSplit[5] + " " + arrSplit[4] +
                            " " + arrSplitTimes[0] + " " + arrSplitTimes[1]);

                    // ----------------- NEEDS UPDATE -------------------------- //

                    //Add a couple of records using parameters
                    System.out.println("Add record 1 to Schedule Table table");
                    System.out.println(s);
                    prep = con.prepareStatement("INSERT INTO Schedule_Table VALUES(?,?,?,?,?,?,?,?);");
                    prep.setInt(1, class_tuid);
                    //Get course tuid
                    prep.setInt(2, Integer.parseInt(getClassTUID(arrList.get(0))));
                    //set course section number
                    prep.setInt(3, Integer.parseInt(arrList.get(1)));
                    //get classroom tuid
                    prep.setInt(4, Integer.parseInt(getClassroomTUID(arrList.get(3))));
                    //get professor tuid
                    prep.setInt(5, Integer.parseInt(getProfessorTUID(arrList.get(5))));

                    //get start time
                    prep.setString(6, arrSplitTimes[0]);
                    //get end time
                    prep.setString(7, arrSplitTimes[1]);
                    //get days
                    prep.setString(8, arrList.get(4));

                    prep.execute();

                    class_tuid++;
                }
            }
                /*
                    0: CSC105
                    1: James
                    2: MW
                    3: 8:30
                    4: 10:30

                    From scheduler
                    CSC105-01 08:30-10:30 A  MW
                    0: CSC105       CLASS_ID
                    1: 01           SECTION_NUM
                    2: 8:30-10:30   START_TIME
                    3: A            CLASSROOM
                    4: MW           DAYS
                    5: James        PROFESSOR
                 */


            //       bfReader.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabase() {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;


        String[] arrSplit;

        ArrayList<String> arlScheduled = new ArrayList<>(getClasses());
        try {
            //      bfReader = new BufferedReader(new FileReader(CLASSES_FILE));
            con = getConnection();

            String tableCount = "SELECT count(*) FROM Schedule_Table";
            prep = con.prepareStatement(tableCount);
            ResultSet rs = prep.executeQuery();
            // System.out.println(rs.getString(1));
            int class_TUID = 1;


                for (String s : arlScheduled
                ) {
                    arrSplit = s.split("\\s+", 7);
                    ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));
                    String[] arrSplitTimes = arrList.get(2).split("-", 2);
                    //arlClasses.add(arrList.get(CLASS_ID));
                    arlScheduledClasses.add(arrSplit[0] + " " + arrSplit[5] + " " + arrSplit[4] +
                            " " + arrSplitTimes[0] + " " + arrSplitTimes[1]);

                    //Add a couple of records using parameters
                    System.out.println("Add record 1 to Schedule Table table");
                    System.out.println(s);
                    prep = con.prepareStatement("INSERT or IGNORE INTO Schedule_Table VALUES(?,?,?,?,?,?,?,?);");
                    prep.setInt(1, class_TUID);
                    //Get course tuid
                    prep.setInt(2, Integer.parseInt(getClassTUID(arrList.get(0))));
                    //set course section number
                    prep.setInt(3, Integer.parseInt(arrList.get(1)));
                    //get classroom tuid
                    prep.setInt(4, Integer.parseInt(getClassroomTUID(arrList.get(3))));
                    //get professor tuid
                    prep.setInt(5, Integer.parseInt(getProfessorTUID(arrList.get(5))));

                    //get start time
                    prep.setString(6, arrSplitTimes[0]);
                    //get end time
                    prep.setString(7, arrSplitTimes[1]);
                    //get days
                    prep.setString(8, arrList.get(4));

                    prep.execute();

                    class_TUID++;
                }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void buildCourses(boolean DBExists) throws ClassNotFoundException, SQLException {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 0;
        ArrayList<String> arlCatalog = new ArrayList<>();
        arlCatalog.add("CSC105;Computers and Programming;4");
        arlCatalog.add("CSC107;Introduction to Code Preparation;1");
        arlCatalog.add("CSC116;Programming I;4");
        arlCatalog.add("CSC216;Programming II;4");
        arlCatalog.add("CSC227;Commenting and Naming Conventions;2");
        arlCatalog.add("CSC316;Data Structures & Algorithms;4");
        arlCatalog.add("CSC416;Advanced Algorithm Analysis;3");
        arlCatalog.add("CSC211;Introductory .NET Development;3");
        arlCatalog.add("CSC311;Advanced .NET Development;4");
        arlCatalog.add("CSC313;Real World Application Development;3");
        arlCatalog.add("CSC411;Data Driven Systems;3");
        arlCatalog.add("CSC412;Sensor Systems;3");
        arlCatalog.add("CSC413;Artificial Intelligence Systems;3");
        arlCatalog.add("CSC496;Software Engineering I;4");
        arlCatalog.add("CSC497;Software Engineering II;4");
        arlCatalog.add("CSC498;Software Engineering III;4");


        con = getConnection();
        if (!DBExists) {
            DBExists = true;

            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Courses_Table'");
            if (!res.next()) {
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
            for (int i = 0; i < arlCatalog.size(); i++) {
                String[] arrSplit = arlCatalog.get(i).split(";", 3);
                courses_tuid++;

                prep = con.prepareStatement("INSERT INTO Courses_Table VALUES(?,?,?,?);");
                prep.setInt(1, courses_tuid);
                prep.setString(2, arrSplit[0]);
                prep.setString(3, arrSplit[1]);
                prep.setInt(4, Integer.parseInt(arrSplit[2]));
                prep.execute();
            }
        }
        con.close();
    }

    public static void buildProfessors(boolean DBExists) throws ClassNotFoundException, SQLException {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 0;
        ArrayList<String> arlProfessors = new ArrayList<>();
        arlProfessors.add("James");
        arlProfessors.add("Smith");
        arlProfessors.add("Jones");
        arlProfessors.add("Vasquez");
        arlProfessors.add("Abdul");
        arlProfessors.add("Thomas");

        con = getConnection();
        if (!DBExists) {
            DBExists = true;

            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Professors_Table'");
            if (!res.next()) {
                System.out.println("Building the PROFESSORS_TABLE table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Professors_Table(" +
                        "TUID INTEGER, " +
                        "Professor_Name VARCHAR(60)," +
                        "PRIMARY KEY (TUID));");
            }


            for (int i = 0; i < arlProfessors.size(); i++) {
                courses_tuid++;
                //Add a couple of records using parameters
                System.out.println("Add record 1 to Professors table");
                prep = con.prepareStatement("INSERT INTO Professors_Table VALUES(?,?);");
                prep.setInt(1, courses_tuid);
                prep.setString(2, arlProfessors.get(i));
                prep.execute();
            }

        }
        con.close();
    }

    public static void buildClassrooms(boolean DBExists) throws ClassNotFoundException, SQLException {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        int courses_tuid = 0;
        ArrayList<String> arlClassrooms = new ArrayList<>();
        arlClassrooms.add("A;30");
        arlClassrooms.add("B;25");
        arlClassrooms.add("C;20");

        con = getConnection();
        if (!DBExists) {
            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Classroom_Table'");
            if (!res.next()) {
                System.out.println("Building the CLASSROOM_TABLE table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Classroom_Table(" +
                        "TUID INTEGER, " +
                        "Classroom_Name VARCHAR(1)," +
                        "Capacity INTEGER," +
                        "PRIMARY KEY (TUID));");
            }
            for (int i = 0; i < arlClassrooms.size(); i++) {
                courses_tuid++;
                String[] arrSplit = arlClassrooms.get(i).split(";", 2);

                //Add a couple of records using parameters
                System.out.println("Add record 1 to Classroom table");
                prep = con.prepareStatement("INSERT INTO Classroom_Table VALUES(?,?,?);");
                prep.setInt(1, courses_tuid);
                prep.setString(2, arrSplit[0]);
                prep.setInt(3, Integer.parseInt(arrSplit[1]));
                prep.execute();
            }
        }

        con.close();
    }


    public static void deleteDatabase() {
        String strSQL = "DROP TABLE Schedule_Table";
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(strSQL);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ResultSet displayUsers() throws SQLException, ClassNotFoundException {
        Statement state;
        ResultSet res;
        Connection con = null;

        if (con == null) {
            //Get Database Connection
            con = getConnection();
        }
        state = con.createStatement();
        res = state.executeQuery("SELECT TUID, Course_TUID, Section, Classroom_TUID, Professor_TUID," +
                "Start_Time, End_Time, Days FROM Schedule_Table");
        return res;
    }

    public static String getClassTUID(String strClass) {
        String strSQL = "Select TUID " +
                "FROM Courses_Table WHERE Course_ID = ?";
        String strResult = "";

        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResult;
    }

    public static String getProfessorTUID(String strClass) {
        String strSQL = "Select TUID " +
                "FROM Professors_Table WHERE Professor_Name = ?";
        String strResult = "";

        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    public static String getClassroomTUID(String strClass) {
        String strSQL = "Select TUID " +
                "FROM Classroom_Table WHERE Classroom_Name = ?";
        String strResult = "";

        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    public static void sortDayTime() {
        String strSQL = "SELECT * FROM Schedule_Table ORDER BY Days, END_TIME DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            ResultSet rs = prepStatement.executeQuery();

            System.out.println("TUID | Course_TUID | Section | Classroom_TUID | Professor_TUID | Start_TIme | End_Time | Days");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " +
                        rs.getString(3) + " " + rs.getString(4) + " " +
                        rs.getString(5) + " " + rs.getString(6) + " " +
                        rs.getString(7) + " " + rs.getString(8));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clearDatabase() {
        String strSQL = "DELETE FROM Schedule_Table";
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(strSQL);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    SELECT Schedule_Table.Professor_TUID, SUM(Courses_Table.Credit_Hours) AS 'Total'
                    FROM Schedule_Table
                    INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID
                    GROUP BY Professor_TUID;
     */
    public static void scheduleByProfessor() {
        String strSQL = "SELECT Professors_Table.Professor_Name, SUM(Courses_Table.Credit_Hours) AS 'Total' " +
                "FROM Schedule_Table " +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID " +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID " +
                "GROUP BY Professor_TUID" +
                ";";
        String strSchedule = "SELECT * " +
                "FROM Schedule_Table " +
                "ORDER BY Professor_TUID;";
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            ResultSet rs = prepStatement.executeQuery();

            prepStatement = conn.prepareStatement(strSchedule);
            ResultSet rsSchedule = prepStatement.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2));
        /*        while(rsSchedule.next()){
                    System.out.println(rsSchedule.getString(1) + " " + rsSchedule.getString(2) + " " +
                            rsSchedule.getString(3) + " " + rsSchedule.getString(4) + " " +
                            rsSchedule.getString(5) + " " + rsSchedule.getString(6) + " " +
                            rsSchedule.getString(7) + " " + rsSchedule.getString(8));
                }

         */
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleByClassSection() {
        String strSQL = "SELECT Courses_Table.Course_Title, Schedule_Table.Section, Classroom_Table.Classroom_Name, Classroom_Table.Capacity " +
                "FROM Courses_Table " +
                "INNER JOIN Schedule_Table ON Schedule_Table.Course_TUID = Courses_Table.TUID " +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID " +
                "ORDER BY Courses_Table.Course_Title;";
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            ResultSet rs = prepStatement.executeQuery();


            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " +
                        rs.getString(3) + " " + rs.getString(4));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean databaseExists() {
        File file = new File(DATABASE_FILE);
        return file.exists();
    }

    //if FALSE, read COPY.txt else, read INPUT.txt
    public static boolean initialBoot() {
        System.out.println("Initial data was found on the database. Would you like to clear it to its initial state? (Y/N): ");
        String strInput = scanner.next();
        boolean flag = true;
        switch (strInput) {
            case "Y", "y":
                //reset
                clearDatabase();
                break;
            case "N", "n":
                flag = false;
                break;
            default:
                System.out.println("Initial: Try Again!");
                break;
        }

        return flag;
    }

    public static boolean schedule_tableExists() throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet rs = dbm.getTables(null, null, "Schedule_Table", null);
        conn.close();
        return rs.next();
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        ResultSet res;
        Connection con;
        boolean DBExists = databaseExists();
        String strInput = "";

        System.out.println("\t*** James Institute of Technology - Degree Scheduling Program ***");
        if (DBExists && schedule_tableExists()) {
            if (!initialBoot()) {
                readFile(COPY_FILE);
            } else {
                System.out.println("Please enter input file : ");
                strInput = scanner.next();
                readFile(strInput);
                copyFile(strInput);
            }
        } else {
            System.out.println("Please enter input file : ");
            strInput = scanner.next();
            readFile(strInput);
            copyFile(strInput);
        }

        //enter input file
        try {
            getConnection();
            buildCourses(DBExists);
            buildProfessors(DBExists);
            buildClassrooms(DBExists);
            buildDatabase(schedule_tableExists());

            int choice;
            do {
                System.out.println("1) Sort schedule by day & time");
                System.out.println("2) Sort schedule by faculty member & total credit hours");
                System.out.println("3) Sort schedule by class");
                System.out.println("4) Load more classes via text file");
                System.out.println("5) Exit program");

                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        sortDayTime();
                        break;
                    case 2:
                        scheduleByProfessor();
                        break;
                    case 3:
                        scheduleByClassSection();
                        break;
                    case 4:
                        System.out.println("Please enter additional data file : ");
                        strInput = scanner.next();
                        appendToFile(strInput);
                        runScheduler();
                        updateDatabase();
                        break;
                    case 5:
                        System.out.println("Would you like to delete the database? (Y/N)");
                        while(true){
                            strInput = scanner.next();
                            if(strInput.equalsIgnoreCase("Y")){
                                deleteDatabase();
                                break;
                            } else if(strInput.equalsIgnoreCase("N")){
                                System.out.println("Database left intact. Can be viewed later.");
                                break;
                            } else {
                                System.out.println("Please input only Y or N ");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid. Try Again.");
                        break;
                }
            } while (choice != 5);

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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*  In order to run:
 *       javac DBTest.java
 *       java -classpath ".;sqlite-jdbc-3.39.3.0.jar" DBTest
 *
 * */
