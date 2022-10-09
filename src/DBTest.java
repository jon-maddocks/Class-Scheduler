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
    static final String SCHEDULE_TABLE = "Schedule_Table";
    static final String COURSES_TABLE = "Courses_Table";
    static final String CLASSROOM_TABLE = "Classroom_Table";
    static final String PROFESSORS_TABLE = "Professors_Table";
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
        Scheduler scheduler = new Scheduler();
        runScheduler();
        ArrayList<String> arlScheduled = new ArrayList<>(getClasses());
        try {
            con = getConnection();

            if (!DBExists) {
                DBExists = true;

                //check for database table existence and if it's not there, create it and add 2 records
                state = con.createStatement();
                res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Schedule_Table'");
                if (!res.next()) {
                    System.out.println("Building the SCHEDULE_TABLE table . . .");
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
            int result = Integer.parseInt(rs.getString(1));

            if (result == 0) {
                for (String s : arlScheduled
                ) {
                    arrSplit = s.split("\\s+", 7);
                    ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));
                    String[] arrSplitTimes = arrList.get(2).split("-", 2);
                    arlScheduledClasses.add(arrSplit[0] + " " + arrSplit[5] + " " + arrSplit[4] +
                            " " + arrSplitTimes[0] + " " + arrSplitTimes[1]);

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
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabase() {
        Connection con;
        PreparedStatement prep;


        String[] arrSplit;

        ArrayList<String> arlScheduled = new ArrayList<>(getClasses());
        try {
            con = getConnection();
            int class_TUID = 1;

            System.out.println("Adding records to Schedule_Table . . .");
            for (String s : arlScheduled
            ) {
                arrSplit = s.split("\\s+", 7);
                ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));
                String[] arrSplitTimes = arrList.get(2).split("-", 2);
                arlScheduledClasses.add(arrSplit[0] + " " + arrSplit[5] + " " + arrSplit[4] +
                        " " + arrSplitTimes[0] + " " + arrSplitTimes[1]);

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
            System.out.println("* * New records have been added * *");
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
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Courses_Table'");
            if (!res.next()) {
                System.out.println("Building the COURSES_TABLE table . . .");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Courses_Table(" +
                        "TUID INTEGER, " +
                        "Course_ID VARCHAR(6)," +
                        "Course_Title VARCHAR(60)," +
                        "Credit_Hours INTEGER," +
                        "PRIMARY KEY (TUID));");
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
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Professors_Table'");
            if (!res.next()) {
                System.out.println("Building the PROFESSORS_TABLE table . . .");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE IF NOT EXISTS Professors_Table(" +
                        "TUID INTEGER, " +
                        "Professor_Name VARCHAR(60)," +
                        "PRIMARY KEY (TUID));");
            }

            for (int i = 0; i < arlProfessors.size(); i++) {
                courses_tuid++;
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
            state = con.createStatement();
            res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='Classroom_Table'");
            if (!res.next()) {
                System.out.println("Building the CLASSROOM_TABLE table . . .");
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
        String deleteSchedule = "DROP TABLE Schedule_Table";
        String deleteProf = "DROP TABLE Professors_Table";
        String deleteCourses = "DROP TABLE Courses_Table";
        String deleteClassroom = "DROP TABLE Classroom_Table";
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            System.out.println("Deleting : Schedule_Table");
            stmt.executeUpdate(deleteSchedule);
            System.out.println("Deleting : Professors_Table");
            stmt.executeUpdate(deleteProf);
            System.out.println("Deleting : Courses_Table");
            stmt.executeUpdate(deleteCourses);
            System.out.println("Deleting : Classroom_Table");
            stmt.executeUpdate(deleteClassroom);
            System.out.println(" * * ALL Tables have been deleted! * * ");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String monSQL = "SELECT * FROM Schedule_Table " +
                "WHERE Days = 'MW' OR Days = 'M'";
        String tueSQL = "SELECT * FROM Schedule_Table " +
                "WHERE Days = 'TR' OR Days = 'T'";
        String wedSQL = "SELECT * FROM Schedule_Table " +
                "WHERE Days = 'MW' OR Days = 'W'";
        String thuSQL = "SELECT * FROM Schedule_Table " +
                "WHERE Days = 'TR' OR Days = 'R'";
        String friSQL = "SELECT * FROM Schedule_Table " +
                "WHERE Days = 'F'";
        String strHeaders = String.format("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|", "TUID", "Course_TUID",
                "Section", "Classroom_TUID", "Professor_TUID", "Start_Time", "End_Time", "Days");

        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement;

            prepStatement = conn.prepareStatement(monSQL);
            ResultSet rsMon = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------------------ MONDAY " +
                    "------------------------------------------------------------");
            System.out.println(strHeaders);
            while (rsMon.next()) {
                System.out.printf("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|\n",
                        parseLeadZero(rsMon.getString(1)),
                        parseLeadZero(rsMon.getString(2)), parseLeadZero(rsMon.getString(3)),
                        parseLeadZero(rsMon.getString(4)), parseLeadZero(rsMon.getString(5)),
                        rsMon.getString(6), rsMon.getString(7),
                        rsMon.getString(8));
            }

            prepStatement = conn.prepareStatement(tueSQL);
            ResultSet rsTue = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------------------ TUESDAY " +
                    "-----------------------------------------------------------");
            System.out.println(strHeaders);
            while (rsTue.next()) {
                System.out.printf("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|\n",
                        parseLeadZero(rsTue.getString(1)),
                        parseLeadZero(rsTue.getString(2)), parseLeadZero(rsTue.getString(3)),
                        parseLeadZero(rsTue.getString(4)), parseLeadZero(rsTue.getString(5)),
                        rsTue.getString(6), rsTue.getString(7),
                        rsTue.getString(8));
            }

            prepStatement = conn.prepareStatement(wedSQL);
            ResultSet rsWed = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------------------ WEDNESDAY " +
                    "---------------------------------------------------------");
            System.out.println(strHeaders);
            while (rsWed.next()) {
                System.out.printf("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|\n",
                        parseLeadZero(rsWed.getString(1)),
                        parseLeadZero(rsWed.getString(2)), parseLeadZero(rsWed.getString(3)),
                        parseLeadZero(rsWed.getString(4)), parseLeadZero(rsWed.getString(5)),
                        rsWed.getString(6), rsWed.getString(7),
                        rsWed.getString(8));
            }

            prepStatement = conn.prepareStatement(thuSQL);
            ResultSet rsThu = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------------------ THURSDAY " +
                    "----------------------------------------------------------");
            System.out.println(strHeaders);
            while (rsThu.next()) {
                System.out.printf("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|\n",
                        parseLeadZero(rsThu.getString(1)),
                        parseLeadZero(rsThu.getString(2)), parseLeadZero(rsThu.getString(3)),
                        parseLeadZero(rsThu.getString(4)), parseLeadZero(rsThu.getString(5)),
                        rsThu.getString(6), rsThu.getString(7),
                        rsThu.getString(8));
            }


            prepStatement = conn.prepareStatement(friSQL);
            ResultSet rsFri = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------------------ FRIDAY " +
                    "------------------------------------------------------------");
            System.out.println(strHeaders);
            while (rsFri.next()) {
                System.out.printf("%15s|%15s|%15s|%15s|%15s|%15s|%15s|%15s|\n",
                        parseLeadZero(rsFri.getString(1)),
                        parseLeadZero(rsFri.getString(2)), parseLeadZero(rsFri.getString(3)),
                        parseLeadZero(rsFri.getString(4)), parseLeadZero(rsFri.getString(5)),
                        rsFri.getString(6), rsFri.getString(7),
                        rsFri.getString(8));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleByProfessor() {
        String strSQL = "SELECT Professors_Table.Professor_Name, SUM(Courses_Table.Credit_Hours) AS 'Total' " +
                "FROM Schedule_Table " +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID " +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID " +
                "GROUP BY Professor_TUID" +
                ";";
        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                System.out.printf("Professor: %-10s { Total Credit Hours: %-2s }\n%n",
                        rs.getString(1), parseLeadZero(rs.getString(2)));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleByClassSection() {
        String strSQL = "SELECT Courses_Table.Course_ID, Courses_Table.Course_Title, Schedule_Table.Section, " +
                "Classroom_Table.Classroom_Name, Classroom_Table.Capacity " +
                "FROM Courses_Table " +
                "INNER JOIN Schedule_Table ON Schedule_Table.Course_TUID = Courses_Table.TUID " +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID " +
                "ORDER BY Courses_Table.Course_Title;";

        String strHeaders = String.format("%-15s|%-35s|%-10s|%-10s|%-10s|", "Course ID","Course Title", "Section",
                "Classroom", "Capacity");

        try {
            Connection conn = getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(strSQL);
            ResultSet rs = prepStatement.executeQuery();

            System.out.println(strHeaders);
            while (rs.next()) {
                System.out.printf("%-15s|%-35s|%-10s|%-10s|%-10s|\n", rs.getString(1), rs.getString(2),
                        parseLeadZero(rs.getString(3)),
                        rs.getString(4), rs.getString(5));
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

    public static boolean databaseExists() {
        File file = new File(DATABASE_FILE);
        return file.exists();
    }

    //if FALSE, read COPY.txt else, read INPUT.txt
    public static boolean initialBoot() {
        System.out.print("Initial data was found on the Database. " +
                "Would you like to clear all scheduled records? (Y/N): ");
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

    public static boolean tableExists(String table) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet rs = dbm.getTables(null, null, table, null);
        conn.close();
        return rs.next();
    }


    // Only code I copied off a Java Tutorial Site. I was unsure on the process on how to copy a text file
    // https://www.makeuseof.com/java-copy-files-different-ways/
    public static void copyFile(String strFile) throws IOException {
        if(!file.exists())
            file.createNewFile();

        File source = new File(strFile);
        File destination = new File("COPY.txt");
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(destination);
            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }
        }
    }

    public static void appendToFile(String strFile){
        arlList.clear();
        try{
            BufferedReader bwRead= new BufferedReader(new FileReader(strFile));
            BufferedWriter bwWrite = new BufferedWriter(new FileWriter(file, true));

            String line;
            while((line = bwRead.readLine()) != null){
                bwWrite.write(line + "\n");
                arlList.add(line);
            }
            bwRead.close();
            bwWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runScheduler();
    }

    public static void readFile(String strCLASS_FILE) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(strCLASS_FILE));
            while (true) {
                String line = bf.readLine();
                if (line != null)
                    arlList.add(line);
                else
                    break;
            }
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        boolean DBExists = databaseExists();
        String strInput = "";
        String strHeader = "* * * * * * James Institute of Technology - Degree Scheduling Program * * * * * *";

        System.out.println(strHeader);
        //Check if DB exists and if the schedule table exists
        if (DBExists && tableExists(SCHEDULE_TABLE)) {
            //On start, if N -> get persisted data from file and do nothing
            if (!initialBoot()) {
                readFile(COPY_FILE);
            } else {
                //On start, if Y -> clear table, and get new data. Copy new file
                System.out.println("Please enter new input file : ");
                strInput = scanner.next();
                System.out.println("Adding records to Schedule_Table . . .");
                readFile(strInput);
                copyFile(strInput);
            }
        //DB does not exist or schedule table does not exist
        } else {
            System.out.print("Please enter input file : ");
            strInput = scanner.next();
            readFile(strInput);
            copyFile(strInput);
        }

        try {
            getConnection();
            buildCourses(tableExists(COURSES_TABLE));
            buildProfessors(tableExists(PROFESSORS_TABLE));
            buildClassrooms(tableExists(CLASSROOM_TABLE));
            buildDatabase(tableExists(SCHEDULE_TABLE));
            int choice;
            do {
                System.out.println("\n" + strHeader);
                System.out.println("1) Sort schedule by day & time");
                System.out.println("2) Sort schedule by faculty member & total credit hours");
                System.out.println("3) Sort schedule by class");
                System.out.println("4) Load more classes via text file");
                System.out.println("5) Exit program");

                System.out.print("\nEnter choice: ");
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
                        System.out.print("Please enter additional data file : ");
                        strInput = scanner.next();
                        appendToFile(strInput);
                        runScheduler();
                        updateDatabase();
                        break;
                    case 5:
                        System.out.print("Would you like to delete the database? (Y/N)");
                        while(true){
                            strInput = scanner.next();
                            if(strInput.equalsIgnoreCase("Y")){
                                getConnection().close();
                                deleteDatabase();
                                break;
                            } else if(strInput.equalsIgnoreCase("N")){
                                System.out.print("Database left intact. Can be viewed later.");
                                break;
                            } else {
                                System.out.print("Please input only Y or N ");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid. Try Again.");
                        break;
                }
            } while (choice != 5);
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