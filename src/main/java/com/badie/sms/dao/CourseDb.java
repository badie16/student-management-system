package com.badie.sms.dao;

import com.badie.sms.models.Class;
import com.badie.sms.models.Course;
import com.badie.sms.utils.MyConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

public class CourseDb {
    Connection con = MyConnection.connection();
    String sql;
    PreparedStatement ps;
    Statement st;
    public boolean exitCourseInClass(String id_course){
        LinkedList<Class> listOfClass = new ClassDb().getListClass();
        for (com.badie.sms.models.Class Class : listOfClass) {
            for (Course course : Class.courses) {
                if (course.id_course.equals(id_course)) return true;
            }
        }
        return false;
    }
    public boolean exitCourse(String id_course) {
        sql = "SELECT * FROM course WHERE id_course = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id_course);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public Course getCourse(String id_course) {
        sql = "SELECT * FROM course WHERE id_course = '" + id_course + "'";
        Course course = new Course();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                course.Name = rs.getString("Name");
                course.id_course = rs.getString("id_course");
                course.id_prof = rs.getString("id_prof");
            }
            return course;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setCourse(Course course){
        sql = "INSERT INTO course "
                + "(id_course,Name,id_prof) "
                + "VALUES('"+course.id_course +"'," +
                "'"+course.Name+"'," +
                "'"+course.id_prof +"')";
        try {
            st = con.createStatement();
            int check = ps.executeUpdate(sql);
            System.out.println(check);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void removeCourse(String id_course){
        sql = "DELETE FROM course WHERE id_course = '"
                + id_course + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void updateCourse(Course course){
        sql ="UPDATE course SET "
                + "id_course = '" + course.id_course
                + "', Name = '" + course.Name
                + "', id_prof = '" + course.id_prof
                + "' WHERE id_course = '" + course.id_course + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void removeProfFromClass(String id_course){
        sql ="UPDATE course SET id_prof = 'NULL' WHERE id_course = '"
                + id_course + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public LinkedList<Course>getCourses(){
        sql = "SELECT * FROM course ";
        LinkedList<Course> courses = new LinkedList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Course course = getCourse(rs.getString("id_course"));
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public LinkedList<Course>getCourses(String id_prof){
        sql = "SELECT * FROM course  WHERE id_prof = '"
                + id_prof + "'";
        LinkedList<Course> courses = new LinkedList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Course course = getCourse(rs.getString("id_course"));
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public LinkedList<Course>getCourses(Class aclass){
        LinkedList<Course> courses = getCourses();
        LinkedList<Course>coursesOfClass = new LinkedList<>();
        sql = "SELECT * FROM courseofclass WHERE id_course = ? AND id_class = '"+aclass.id_class +"'";
        try {
            for (Course course : courses) {
                ps = con.prepareStatement(sql);
                ps.setString(1, course.id_course);
                if(ps.executeQuery().next()){
                    coursesOfClass.add(course);
                }
            }
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return coursesOfClass;
    }
    public HashMap<String,Course>getMapCourses(){
        sql = "SELECT * FROM course ";
        HashMap<String,Course> courses = new HashMap<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Course course = getCourse(rs.getString("id_course"));
                courses.put(course.id_course,course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
