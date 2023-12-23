package com.badie.sms.dao;

import com.badie.sms.models.Admin;
import com.badie.sms.models.User;
import com.badie.sms.utils.MyConnection;

import java.sql.*;

public class AdminDb {
    Connection con = MyConnection.connection();
    String sql;
    UserDb userDb = new UserDb();
    PreparedStatement ps;
    Statement st;
    public boolean exitAdmin(String user, String pass){
        sql="SELECT * FROM admin WHERE user = '"+user+"'";
        try {
            st =con.createStatement();
            ResultSet rs= st.executeQuery(sql);
            if (rs.next()){
                Admin admin = getAdmin(user);
                return pass.equals(admin.pass) && user.equals(admin.user);
            }
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public boolean exitAdmin(String user){
        sql = "SELECT * FROM admin WHERE user = ? ";
        try {
            ps=con.prepareStatement(sql);
            ps.setString(1,user);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public Admin getAdmin(String user){
        sql="SELECT * FROM admin WHERE user = '"+user+"'";
        Admin admin = new Admin();
        try {
            st =con.createStatement();
            ResultSet rs= st.executeQuery(sql);
            if (rs.next()){
                User userAdmin = userDb.getUser(rs.getString("id"));
                admin = new Admin(userAdmin);
                admin.user = rs.getString("user");
                return admin;
            }
            return admin;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean updateAdmin(Admin admin,String user){
        admin.id = getAdmin(user).id;
        sql ="UPDATE admin SET "
                + "user = '" + admin.user
                +"' WHERE user = '" + user + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
            userDb.updateUser(admin);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
        return exitAdmin(admin.user);
    }
}
