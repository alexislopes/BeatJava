

import java.sql.*;
import java.util.ArrayList;

public class Connector {
    public static Connection conn;

    public Connector() {
        getConnection();
    }


    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/bot?autoReconnect=true&useSSL=false";
    private final String USER = "root";
    private final String PW = "";

    public void getConnection() {
        try {
            Class.forName(DRIVER);
            System.out.println("Connected Successfully with the DB.\n");
            conn =  DriverManager.getConnection(URL, USER, PW);

        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Connection error: ", ex);
        }
    }







    public static void closeConnection(Connection con, PreparedStatement stmt) throws SQLException {
        if (con != null) {
            con.close();
        }
    }


}
