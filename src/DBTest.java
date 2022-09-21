import java.sql.*;

public class DBTest {
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection con;
        //Database path -- if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
        return con;
    }

    private static void buildDatabase(boolean DBExists) throws ClassNotFoundException, SQLException {
        Connection con;
        Statement state, state2;
        ResultSet res;
        PreparedStatement prep;

        if(!DBExists){
            DBExists = true;
            con = getConnection();
            //check for database table existence and if it's not there, create it and add 2 records
            state = con.createStatement();
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            if(!res.next()){
                System.out.println("Building the USER table");
                state2 = con.createStatement();
                state2.executeUpdate("CREATE TABLE User(" +
                        "ID INTEGER, " +
                        "FirstNAme VARCHAR(60)," +
                        "LastName VARCHAR(60)," +
                        "PRIMARY KEY (ID));");
                //Add a couple of records using parameters
                System.out.println("Add record 1 to USER table");
                prep = con.prepareStatement("INSERT INTO User VALUES(?,?,?);");
                prep.setInt(1,1001);
                prep.setString(2, "Sue");
                prep.setString(3, "Smith");
                prep.execute();

                System.out.println("Add record 2 to USER table");
                prep = con.prepareStatement("INSERT INTO USER VALUES(?,?,?);");
                prep.setInt(1,1002);
                prep.setString(2, "John");
                prep.setString(3, "Jones");
                prep.execute();
            }
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
        res = state.executeQuery("SELECT ID, Firstname, Lastname FROM User");
        return res;
    }

    public static void main(String[] args) {
        ResultSet res;
        Connection con;
        boolean DBExists = false;

        try {
            getConnection();
            buildDatabase(DBExists);
            //Bring back the set of user from the database
            res = displayUsers();
            //Iterate over the resultSet, print out each record's details
            while(res.next()){
                System.out.println("ID: " + res.getInt("ID") + " -- User: " +
                        res.getString("FirstName") + " " + res.getString("LastName"));
            }
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
