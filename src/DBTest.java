import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static void buildDatabase(boolean DBExists) {
        Connection con = null;
        Statement state = null, state2 = null;
        ResultSet res = null, rs = null;
        PreparedStatement prep = null;


        String[] arrSplit;
        int class_tuid = 1;
        Scheduler scheduler = new Scheduler();
        runScheduler();
        ArrayList<String> arlScheduled = new ArrayList<>(getClasses());
        try {
            con = getConnection();
            if (!DBExists) {
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
            rs = prep.executeQuery();
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
                if(getUnavailableClasses().size() > 0){
                    System.out.println("\nCLASSES NOT SCHEDULED : ");
                    for (String s : getUnavailableClasses()
                    ) {
                        System.out.println(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state != null) state.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state2 != null) state2.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prep != null) prep.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void updateDatabase() {
        Connection con = null;
        PreparedStatement prep = null;
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
            if(getUnavailableClasses().size() > 0){
                System.out.println("CLASSES NOT SCHEDULED : ");
                for (String s : getUnavailableClasses()
                ) {
                    System.out.println(s);
                }
            } else {
                System.out.println("* * New records have been added * *");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (prep != null) prep.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void buildCourses(boolean DBExists) {
        Connection con = null;
        Statement state = null, state2 = null;
        ResultSet res = null;
        PreparedStatement prep = null;

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

        try {
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
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prep != null) prep.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state != null) state.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state2 != null) state2.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void buildProfessors(boolean DBExists) {
        Connection con = null;
        Statement state = null, state2 = null;
        ResultSet res = null;
        PreparedStatement prep = null;

        int courses_tuid = 0;
        ArrayList<String> arlProfessors = new ArrayList<>();
        arlProfessors.add("James");
        arlProfessors.add("Smith");
        arlProfessors.add("Jones");
        arlProfessors.add("Vasquez");
        arlProfessors.add("Abdul");
        arlProfessors.add("Thomas");

        try {
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
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prep != null) prep.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state != null) state.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state2 != null) state2.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void buildClassrooms(boolean DBExists) {
        Connection con = null;
        Statement state = null, state2 = null;
        ResultSet res = null;
        PreparedStatement prep = null;

        int courses_tuid = 0;
        ArrayList<String> arlClassrooms = new ArrayList<>();
        arlClassrooms.add("A;30");
        arlClassrooms.add("B;25");
        arlClassrooms.add("C;20");

        try {
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
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prep != null) prep.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state != null) state.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (state2 != null) state2.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static String getClassTUID(String strClass) {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet rs = null;

        String strSQL = "Select TUID " +
                "FROM Courses_Table WHERE Course_ID = ?";
        String strResult = "";
        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            rs = prepStatement.executeQuery();
            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
        return strResult;
    }

    public static String getProfessorTUID(String strClass) {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet rs = null;

        String strSQL = "Select TUID " +
                "FROM Professors_Table WHERE Professor_Name = ?";
        String strResult = "";
        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            rs = prepStatement.executeQuery();

            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
        return strResult;
    }

    public static String getClassroomTUID(String strClass) {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet rs = null;
        String strSQL = "Select TUID " +
                "FROM Classroom_Table WHERE Classroom_Name = ?";
        String strResult = "";
        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(strSQL);
            prepStatement.setString(1, strClass);
            rs = prepStatement.executeQuery();

            while (rs.next()) {
                strResult = rs.getString("TUID");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
        return strResult;
    }

    public static void sortDayTime() {
        Connection conn = null;
        PreparedStatement prepStatement = null;
        ResultSet rsMon = null, rsTue = null, rsWed = null, rsThu = null, rsFri = null;
        String monSQL = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Classroom_Table.Classroom_Name,\n" +
                " Professors_Table.Professor_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                "FROM Schedule_Table\n" +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID  \n" +
                "WHERE Days = 'MW' OR Days = 'M'\n" +
                "ORDER BY Start_Time;";
        String tueSQL = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Classroom_Table.Classroom_Name,\n" +
                " Professors_Table.Professor_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                "FROM Schedule_Table\n" +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID  \n" +
                "WHERE Days = 'TR' OR Days = 'T'\n" +
                "ORDER BY Start_Time;";
        String wedSQL = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Classroom_Table.Classroom_Name,\n" +
                " Professors_Table.Professor_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                "FROM Schedule_Table\n" +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID  \n" +
                "WHERE Days = 'MW' OR Days = 'W'\n" +
                "ORDER BY Start_Time;";
        String thuSQL = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Classroom_Table.Classroom_Name,\n" +
                " Professors_Table.Professor_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                "FROM Schedule_Table\n" +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID  \n" +
                "WHERE Days = 'TR' OR Days = 'R'\n" +
                "ORDER BY Start_Time;";
        String friSQL = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Classroom_Table.Classroom_Name,\n" +
                " Professors_Table.Professor_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                "FROM Schedule_Table\n" +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID  \n" +
                "WHERE Days = 'F'\n" +
                "ORDER BY Start_Time;";
        String strHeaders = String.format("%15s|%10s|%15s|%15s|%15s|%15s|%15s|", "Course ID", "Section",
                "Classroom", "Professor", "Start Time", "End Time", "Days");

        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(monSQL);
            rsMon = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------- MONDAY " +
                    "--------------------------------------------------");
            System.out.println(strHeaders);
            while (rsMon.next()) {
                System.out.printf("%15s|%10s|%15s|%15s|%15s|%15s|%15s|\n",
                        rsMon.getString(1),
                        parseLeadZero(rsMon.getString(2)), rsMon.getString(3),
                        rsMon.getString(4), convertTime(rsMon.getString(5)),
                        convertTime(rsMon.getString(6)), rsMon.getString(7));
            }

            prepStatement = conn.prepareStatement(tueSQL);
            rsTue = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------- TUESDAY " +
                    "-------------------------------------------------");
            System.out.println(strHeaders);
            while (rsTue.next()) {
                System.out.printf("%15s|%10s|%15s|%15s|%15s|%15s|%15s|\n",
                        rsTue.getString(1),
                        parseLeadZero(rsTue.getString(2)), rsTue.getString(3),
                        rsTue.getString(4), convertTime(rsTue.getString(5)),
                        convertTime(rsTue.getString(6)), rsTue.getString(7));
            }

            prepStatement = conn.prepareStatement(wedSQL);
            rsWed = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------ WEDNESDAY " +
                    "------------------------------------------------");
            System.out.println(strHeaders);
            while (rsWed.next()) {
                System.out.printf("%15s|%10s|%15s|%15s|%15s|%15s|%15s|\n",
                        rsWed.getString(1),
                        parseLeadZero(rsWed.getString(2)), rsWed.getString(3),
                        rsWed.getString(4), convertTime(rsWed.getString(5)),
                        convertTime(rsWed.getString(6)), rsWed.getString(7));
            }

            prepStatement = conn.prepareStatement(thuSQL);
            rsThu = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------ THURSDAY " +
                    "-------------------------------------------------");
            System.out.println(strHeaders);
            while (rsThu.next()) {
                System.out.printf("%15s|%10s|%15s|%15s|%15s|%15s|%15s|\n",
                        rsThu.getString(1),
                        parseLeadZero(rsThu.getString(2)), rsThu.getString(3),
                        rsThu.getString(4), convertTime(rsThu.getString(5)),
                        convertTime(rsThu.getString(6)), rsThu.getString(7));
            }

            prepStatement = conn.prepareStatement(friSQL);
            rsFri = prepStatement.executeQuery();
            System.out.println("\n------------------------------------------------- FRIDAY " +
                    "--------------------------------------------------");
            System.out.println(strHeaders);
            while (rsFri.next()) {
                System.out.printf("%15s|%10s|%15s|%15s|%15s|%15s|%15s|\n",
                        rsFri.getString(1),
                        parseLeadZero(rsFri.getString(2)), rsFri.getString(3),
                        rsFri.getString(4), convertTime(rsFri.getString(5)),
                        convertTime(rsFri.getString(6)), rsFri.getString(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rsMon != null) rsMon.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rsTue != null) rsTue.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rsWed != null) rsWed.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rsThu != null) rsThu.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (rsFri != null) rsFri.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void scheduleByProfessor() {
        Connection conn = null;
        PreparedStatement prepStatement = null, tempStatement = null;
        ResultSet rs = null, tempRS = null;
        String strSQL = "SELECT Professors_Table.Professor_Name, SUM(Courses_Table.Credit_Hours) AS 'Total' " +
                "FROM Schedule_Table " +
                "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID " +
                "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID " +
                "GROUP BY Professor_TUID" +
                ";";
        String strHeaders = String.format("%15s|%10s|%10s|%15s|%15s|%15s|%15s|\n", "Course ID", "Section", "Credits",
                "Classroom", "Start Time", "End Time", "Days");

        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(strSQL);
            rs = prepStatement.executeQuery();
            int intProfIndex = 1;
            while (rs.next()) {
                System.out.printf("Professor: %-10s { Total Credit Hours: %-2s }\n",
                        rs.getString(1), parseLeadZero(rs.getString(2)));

                System.out.print(strHeaders);
                String strTemp = "SELECT Courses_Table.Course_ID, Schedule_Table.Section, Courses_Table.Credit_Hours, " +
                        "Classroom_Table.Classroom_Name, Schedule_Table.Start_Time, Schedule_Table.End_Time, Schedule_Table.Days\n" +
                        "FROM Schedule_Table\n" +
                        "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                        "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                        "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID\n" +
                        "WHERE Professor_TUID = ";
                String strAdd = "ORDER BY Course_ID;";
                strTemp += "'" + intProfIndex + "'" + "\n" + strAdd;
                tempStatement = conn.prepareStatement(strTemp);
                tempRS = tempStatement.executeQuery();
                while(tempRS.next()){
                    System.out.printf("%15s|%10s|%10s|%15s|%15s|%15s|%15s|\n", tempRS.getString(1),
                            parseLeadZero(tempRS.getString(2)), parseLeadZero(tempRS.getString(3)),
                            tempRS.getString(4), convertTime(tempRS.getString(5)),
                            convertTime(tempRS.getString(6)),
                            tempRS.getString(7));
                }
                System.out.println();
                intProfIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (tempRS != null) tempRS.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (tempStatement != null) tempStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void scheduleByClassSection() {
        Connection conn = null;
        PreparedStatement prepStatement = null, tempStatement = null;
        ResultSet rs = null, tempRS = null;
        String strSQL = "SELECT Courses_Table.Course_ID, Courses_Table.Course_Title, sum(Classroom_Table.Capacity) AS 'Total Capacity'\n" +
                "FROM Courses_Table \n" +
                "INNER JOIN Schedule_Table ON Schedule_Table.Course_TUID = Courses_Table.TUID\n" +
                "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                "GROUP BY Course_ID\n" +
                "ORDER BY Course_ID;";
        String strHeaders = String.format("%15s|%10s|%10s|%15s|%15s|%15s|%15s|", "Section", "Classroom", "Capacity",
                "Professor", "Start Time", "End Time", "Days");

        try {
            conn = getConnection();
            prepStatement = conn.prepareStatement(strSQL);
            rs = prepStatement.executeQuery();

            int intCourseIndex = 1;
            while (rs.next()) {
                System.out.printf("\nCourse: %-7s- %-35s { Total Capacity: %-3s }\n", rs.getString(1), rs.getString(2),
                        rs.getString(3));

                System.out.println(strHeaders);
                String strTemp = "SELECT Schedule_Table.Section, Classroom_Table.Classroom_Name," +
                        " Classroom_Table.Capacity, Professors_Table.Professor_Name, Schedule_Table.Start_Time," +
                        " Schedule_Table.End_Time, Schedule_Table.Days\n" +
                        "FROM Schedule_Table\n" +
                        "INNER JOIN Classroom_Table ON Classroom_Table.TUID = Schedule_Table.Classroom_TUID\n" +
                        "INNER JOIN Professors_Table ON Professors_Table.TUID = Schedule_Table.Professor_TUID\n" +
                        "INNER JOIN Courses_Table ON Courses_Table.TUID = Schedule_Table.Course_TUID\n" +
                        "WHERE Schedule_Table.Course_TUID = ";
                String strAdd = "ORDER BY Section;";
                strTemp += "'" + intCourseIndex + "'\n" + strAdd;
                tempStatement = conn.prepareStatement(strTemp);
                tempRS = tempStatement.executeQuery();
                while(tempRS.next()){
                    System.out.printf("%15s|%10s|%10s|%15s|%15s|%15s|%15s|\n", parseLeadZero(tempRS.getString(1)),
                            tempRS.getString(2), tempRS.getString(3),
                            tempRS.getString(4), convertTime(tempRS.getString(5)),
                            convertTime(tempRS.getString(6)),
                            tempRS.getString(7));
                }
                System.out.println();
                intCourseIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (tempRS != null) tempRS.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (prepStatement != null) prepStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (tempStatement != null) tempStatement.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }
    public static void clearDatabase() {
        Connection conn = null;
        Statement stmt = null;
        String strSQL = "DELETE FROM Schedule_Table";
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(strSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static void deleteDatabase() {
        Connection conn = null;
        Statement stmt = null;
        String deleteSchedule = "DROP TABLE Schedule_Table";
        String deleteProf = "DROP TABLE Professors_Table";
        String deleteCourses = "DROP TABLE Courses_Table";
        String deleteClassroom = "DROP TABLE Classroom_Table";
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            System.out.println("Deleting : Schedule_Table");
            stmt.executeUpdate(deleteSchedule);
            System.out.println("Deleting : Professors_Table");
            stmt.executeUpdate(deleteProf);
            System.out.println("Deleting : Courses_Table");
            stmt.executeUpdate(deleteCourses);
            System.out.println("Deleting : Classroom_Table");
            stmt.executeUpdate(deleteClassroom);
            System.out.println(" * * ALL Tables have been deleted! * * ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace();}
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
        String strInput;
        boolean flag = true;
        do {
            strInput = scanner.next();
            if(strInput.equalsIgnoreCase("Y")){
                clearDatabase();
                break;
            } else if(strInput.equalsIgnoreCase("N")){
                flag = false;
                break;
            } else {
                System.out.print("Invalid choice selection. Please select Y/N only : ");
            }
        } while(!strInput.equalsIgnoreCase("Y") || !strInput.equalsIgnoreCase("N"));

        return flag;
    }

    public static boolean tableExists(String table) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet rs = dbm.getTables(null, null, table, null);
        boolean flagRS = rs.next();
        rs.close();
        conn.close();
        return flagRS;
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
        BufferedReader bwRead= null;
        BufferedWriter bwWrite = null;
        try{
            bwRead= new BufferedReader(new FileReader(strFile));
            bwWrite = new BufferedWriter(new FileWriter(file, true));

            String line;
            while((line = bwRead.readLine()) != null){
                bwWrite.write(line + "\n");
                arlList.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if (bwRead != null) bwRead.close(); } catch (Exception e) { e.printStackTrace();}
            try { if (bwWrite != null) bwWrite.close(); } catch (Exception e) { e.printStackTrace();}
        }
        runScheduler();
    }

    public static void readFile(String strCLASS_FILE) {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(strCLASS_FILE));
            while (true) {
                String line = bf.readLine();
                if (line != null)
                    arlList.add(line);
                else
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (bf != null) bf.close(); } catch (Exception e) { e.printStackTrace();}
        }
    }

    public static String fileExists(){
        String strInput = scanner.next();
        File file = new File(strInput);
        while(!file.exists()){
            file = new File(strInput);
            if(!file.exists()){
                System.out.print("File not found. Try again : ");
                strInput = scanner.next();
            }
        }
        return strInput;
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        boolean DBExists = databaseExists();
        String strInput = "";
        String strHeader = "* * * * * * James Institute of Technology - Degree Scheduling Program * * * * * *";

        boolean DELETE = false;

        System.out.println(strHeader);
        //Check if DB exists and if the schedule table exists
        if (DBExists && tableExists(SCHEDULE_TABLE)) {
            //On start, if N -> get persisted data from file and do nothing
            if (!initialBoot()) {
                readFile(COPY_FILE);
            } else {
                //On start, if Y -> clear table, and get new data. Copy new file
                System.out.println("* * * Schedule Table has been cleared of all records. * * *");
                System.out.print("Please enter new input file : ");
                strInput = fileExists();
                System.out.println("Adding records to Schedule Table . . .");
                readFile(strInput);
                copyFile(strInput);
            }
        //DB does not exist or schedule table does not exist
        } else if(DBExists && !tableExists(SCHEDULE_TABLE)){
            System.out.println("\n------ Database file found. But no scheduling data exists. ------ ");
            System.out.print("Please enter input data file : ");
            strInput = fileExists();
            readFile(strInput);
            copyFile(strInput);
        } else {
            System.out.println("\n------ Database file was not found and will be created here ------ ");
            System.out.print("Please enter input data file : ");
            strInput = fileExists();
            readFile(strInput);
            copyFile(strInput);
        }

        try {
            buildCourses(tableExists(COURSES_TABLE));
            buildProfessors(tableExists(PROFESSORS_TABLE));
            buildClassrooms(tableExists(CLASSROOM_TABLE));
            buildDatabase(tableExists(SCHEDULE_TABLE));
            String choice;
            do {
                System.out.println("\n" + strHeader);
                System.out.println("1) Sort schedule by day & time");
                System.out.println("2) Sort schedule by faculty member & total credit hours");
                System.out.println("3) Sort schedule by class & list all sections");
                System.out.println("4) Load more classes via text file");
                System.out.println("5) Exit program");

                System.out.print("\nEnter choice: ");
                choice = scanner.next();
                switch (choice) {
                    case "1":
                        sortDayTime();
                        break;
                    case "2":
                        scheduleByProfessor();
                        break;
                    case "3":
                        scheduleByClassSection();
                        break;
                    case "4":
                        System.out.print("Please enter additional data file : ");
                        strInput = scanner.next();
                            do {
                                File file = new File(strInput);
                                if (file.exists()) {
                                    appendToFile(strInput);
                                    readFile(strInput);
                                    copyFile(strInput);
                                    updateDatabase();
                                } else {
                                    System.out.print("File not found. Try again : ");
                                    strInput = scanner.next();
                                }
                            }while(!file.exists());
                        break;
                    case "5":
                        System.out.print("Would you like to delete the database? (Y/N): ");
                        while(!strInput.equalsIgnoreCase("Y") || !strInput.equalsIgnoreCase("N")){
                            strInput = scanner.next();
                            if(strInput.equalsIgnoreCase("Y")){
                                deleteDatabase();
                                DELETE = true;
                                break;
                            } else if(strInput.equalsIgnoreCase("N")){
                                System.out.print("Database left intact. Can be viewed later.");
                                break;
                            } else {
                                System.out.print("Please input only Y or N : ");
                            }
                        }
                        break;
                    default:
                        System.out.print("\n\t* * * Invalid choice. Please use only 1,2,3,4 or 5. Try Again. * * *");
                        break;
                }
            } while (!choice.equals("5"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(DELETE){
            try{
                Files.deleteIfExists(Path.of("SQLiteTest1.db"));
            } catch(Exception e){
                System.out.println(" * * * COULD NOT DELETE DATABASE FILE. PROCESS MAY BE STILL BE BEING USED OUTSIDE PROGRAM * * *");
            }
        }
    }
}
/*  In order to run:
 *       javac DBTest.java
 *       java -classpath ".;sqlite-jdbc-3.39.3.0.jar" DBTest
 *
 * */