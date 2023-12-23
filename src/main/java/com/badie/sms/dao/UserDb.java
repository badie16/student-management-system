package com.badie.sms.dao;

import com.badie.sms.models.Admin;
import com.badie.sms.models.User;
import com.badie.sms.utils.Gender;
import com.badie.sms.utils.MyConnection;

import java.sql.*;

public class UserDb {
    Connection con = MyConnection.connection();
    String sql;
    PreparedStatement ps;
    Statement st;
    Boolean exitUser(String id){
        sql = "SELECT * FROM users WHERE id = ?";
        try {
            ps=con.prepareStatement(sql);
            ps.setString(1,id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public User getUser(String id){
        if (!exitUser(id)) return null; //chick is user exited before start function getUser
        sql="SELECT * FROM users WHERE id = '"+id+"'";
        User user = new Admin();
        try {
            st =con.createStatement();
            ResultSet rs= st.executeQuery(sql);
            if (rs.next()){
                user.id = rs.getInt("id");
                user.firstName = rs.getString("firstName");
                user.lastName = rs.getString("lastName");
                user.pass = rs.getString("passWord");
                user.email = rs.getString("email");
                user.mobileNumber = rs.getString("mobileNumber");
                user.gender = Gender.valueOf(rs.getString("gender"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public int setUser(User user) {
        String sql = "INSERT INTO `users` " +
                "(`firstName`, `mobileNumber`, `email`, `passWord`, `lastName`, `gender`) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.firstName);
            ps.setString(2, user.mobileNumber);
            ps.setString(3, user.email);
            ps.setString(4, user.pass);
            ps.setString(5, user.lastName);
            ps.setString(6, user.gender.toString());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.id =  generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return user.id;
    }
    public void updateUser(User user) {
        String sql = "UPDATE users " +
                "SET password = ?, firstName = ?, lastName = ?, email = ?, mobileNumber = ?, gender = ? " +
                "WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, user.pass);
            ps.setString(2, user.firstName);
            ps.setString(3, user.lastName);
            ps.setString(4, user.email);
            ps.setString(5, user.mobileNumber);
            ps.setString(6, user.gender.toString());
            ps.setInt(7, user.id);  // Assuming 'id' is the primary key
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void removeUser(int id){

        sql = "DELETE FROM users WHERE id = '"
                + id + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
}
