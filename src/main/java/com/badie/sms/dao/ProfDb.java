package com.badie.sms.dao;

import com.badie.sms.models.Prof;
import com.badie.sms.models.User;
import com.badie.sms.utils.MyConnection;

import java.sql.*;
import java.util.HashMap;

public class ProfDb {
    Connection con = MyConnection.connection();
    String sql;
    PreparedStatement ps;
    UserDb userDb = new UserDb();
    Statement st;
    public boolean exitProf(String id_prof) {
        sql = "SELECT * FROM  prof WHERE id_prof = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id_prof);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }

    public Prof getProf(String id_prof) {
        sql = "SELECT * FROM prof WHERE id_prof = '" + id_prof + "'";
        Prof prof = new Prof();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                User userProf = userDb.getUser(rs.getString("id"));
                if (userProf == null) return null;
                prof = new Prof(userProf);
                prof.id_prof = rs.getString("id_prof");
               }
            return prof;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setProf(Prof prof){
        prof.id = userDb.setUser(prof);
        sql = "INSERT INTO prof "
                + "(id_prof,id) "
                + "VALUES('"+ prof.id_prof +"'," +
                "'"+ prof.id+"')";
        try {
            st = con.createStatement();
            int check = ps.executeUpdate(sql);
            System.out.println(check);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void removeProf(String id_prof){
        int idUser = getProf(id_prof).id;
        sql = "DELETE FROM prof WHERE id_prof = '"
                + id_prof + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
            userDb.removeUser(idUser);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void updateProf(Prof prof){
        prof.id = getProf(prof.id_prof).id;
        userDb.updateUser(prof);
    }
    public HashMap<String, Prof> getProfs() {
        sql = "SELECT * FROM prof ";
        HashMap<String, Prof> faculties = new HashMap<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Prof prof = getProf(rs.getString("id_prof"));
                faculties.put(prof.id_prof, prof);
            }
            return faculties;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
