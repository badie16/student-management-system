package com.badie.sms.utils;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyConnection {
    final static String DB_URL="jdbc:mysql://localhost/sms";
    final static String DbUSER="root";
    final static  String DbPASS="";
    static Connection connect;
    public static Connection connection()
    {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost/smsdb", DbUSER, DbPASS);
            return connect;
        }catch(SQLException e) {
            Directories.alert("Error - data base is not found", Alert.AlertType.ERROR);
            return null;
        }
    }


}
