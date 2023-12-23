package com.badie.sms.dao;

import com.badie.sms.models.Class;
import com.badie.sms.models.Course;
import com.badie.sms.utils.MyConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

public class ClassDb {
    Connection con = MyConnection.connection();
    String sql;
    CourseDb courseDb = new CourseDb();
    CourseOfClassDb courseOfClassDb = new CourseOfClassDb();
    PreparedStatement ps;
    Statement st;
    public boolean exitClass(String id_class) {
        sql = "SELECT * FROM class WHERE id_class = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id_class);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }

    public Class getClass(String id_class) {
        sql = "SELECT * FROM class WHERE id_class = '" + id_class + "'";
        Class _class = new Class();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                _class.id_class = rs.getString("id_class");
                _class.Name = rs.getString("Name");
                _class.numberOfStudentMax = Integer.parseInt(rs.getString("numberOfStudentMax"));
                _class.numberOfStudent = new StudentDb().getStudents(_class.id_class).size();
                _class.admin = rs.getString("id_prof");
                _class.courses = courseDb.getCourses(_class);
            }
            return _class;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setClass(Class aClass){
        sql = "INSERT INTO class "
                + "(id_class,Name,numberOfStudentMax,id_prof) "
                + "VALUES('"+ aClass.id_class+"'," +
                "'"+ aClass.Name+"'," +
                "'"+ aClass.numberOfStudentMax+"'," +
                "'"+ aClass.admin+"')";
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
            for (Course course : aClass.courses) {
                courseOfClassDb.setCourseOFClass(aClass.id_class,course.id_course);
            }
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void removeClass(String id_class){
        sql = "DELETE FROM class WHERE id_class = '"
                + id_class + "'";
        try{
            courseOfClassDb.removeCourseOFClass(id_class);
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void updateClass(Class aClass){
        sql ="UPDATE class SET "
                + "Name = '" + aClass.Name
                + "', numberOfStudentMax = '" + aClass.numberOfStudentMax
                + "', id_prof = '" + aClass.admin
                + "' WHERE id_class = '" + aClass.id_class + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
            courseOfClassDb.updateCourseOFClass(aClass.id_class,aClass.courses);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public LinkedList<Class> getListClass() {
        sql = "SELECT * FROM class ";
        LinkedList<Class> listClass = new LinkedList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Class _class = getClass(rs.getString("id_class"));
                listClass.add(_class);
            }
            return listClass;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public HashMap<String,Class> getMapClass() {
        sql = "SELECT * FROM class ";
        HashMap<String,Class> listClass = new HashMap<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Class _class = getClass(rs.getString("id_class"));
                listClass.put(_class.id_class,_class);
            }
            return listClass;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
