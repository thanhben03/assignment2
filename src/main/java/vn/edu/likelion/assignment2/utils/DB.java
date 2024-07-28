package vn.edu.likelion.assignment2.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private String url;
    private String user;
    private String pass;
    private Connection conn = null;

    public DB() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        user = "assignment2";
        pass = "123456";
    }

    public Connection openConnect() throws SQLException {
        conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }

    public void closeConnect() throws SQLException {
        if (conn != null) conn.close();
    }

    public Connection getConnect() {
        return conn;
    }

}
