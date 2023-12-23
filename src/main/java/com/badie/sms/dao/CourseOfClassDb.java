package com.badie.sms.dao;

import com.badie.sms.models.Course;
import com.badie.sms.utils.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class CourseOfClassDb {
    Connection con = MyConnection.connection();
    String sql;
    PreparedStatement ps;
    Statement st;
    public void setCourseOFClass(String id_class,String id_course){
        sql = "INSERT INTO courseofclass (id_course,id_class)" +
                " VALUES ('"+id_course+"', '"+id_class+"');";
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void updateCourseOFClass(String id_class, LinkedList<Course> courses){
        removeCourseOFClass(id_class);
        for (Course course : courses) {
            setCourseOFClass(id_class,course.id_course);
        }
    }
    public void removeCourseOFClass(String id_class){
        sql = "DELETE FROM courseofclass WHERE id_class = '"
                + id_class + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
}
