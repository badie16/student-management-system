package com.badie.sms.dao;
import com.badie.sms.models.Student;
import com.badie.sms.models.User;
import com.badie.sms.utils.Gender;
import com.badie.sms.utils.MyConnection;
import java.sql.*;
import java.util.LinkedList;

public class StudentDb {
    Connection con = MyConnection.connection();
    String sql;
    PreparedStatement ps;
    UserDb userDb = new UserDb();
    Statement st;
    public boolean exitStudent(String CNE, String pass) {
        sql = "SELECT * FROM students WHERE CNE = ? AND Password = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, CNE);
            ps.setString(2, pass);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public boolean exitStudent(String CNE) {
        sql = "SELECT * FROM students WHERE CNE = ? ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, CNE);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("error " + e);
        }
        return false;
    }
    public int totalStudentGender(Gender gender){
        int count = 0;
        sql = "SELECT * FROM students INNER JOIN users ON students.id = users.id WHERE gender = '"+gender+"'";
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public Student getStudent(String CNE) {
        sql = "SELECT * FROM students WHERE CNE = '" + CNE + "'";
        Student student = new Student();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                User userStudent = userDb.getUser(rs.getString("id"));
                if (userStudent == null) return null;
                student = new Student(userStudent);
                student.CNE = rs.getString("CNE");
                student.id_class = rs.getString("id_class");
                student.age = rs.getDate("age").toLocalDate();
            }
            return student;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setStudent(Student student){
        student.id = userDb.setUser(student);
        sql = "INSERT INTO students "
                + "(CNE,age,id,id_class) "
                + "VALUES('"+student.CNE+"'," +
                "'"+student.age+"'," +
                "'"+student.id+"'," +
                "'"+student.id_class +"')";
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void removeStudent(String CNE){
        int idUser = getStudent(CNE).id;
        sql = "DELETE FROM students WHERE CNE = '"
                + CNE + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
            userDb.removeUser(idUser);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public void updateStudent(Student student){
        student.id = getStudent(student.CNE).id;
        sql ="UPDATE students SET "
                + "CNE = '" + student.CNE
                + "', age = '" + student.age
                + "', id_class = '" + student.id_class
                +"' WHERE CNE = '" + student.CNE + "'";
        try{
            st = con.createStatement();
            st.executeUpdate(sql);
            userDb.updateUser(student);
        }catch (SQLException e){
            System.out.println("error " + e);
        }
    }
    public LinkedList<Student> getStudents() {
        sql = "SELECT * FROM students";
        LinkedList<Student> students = new LinkedList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Student student = getStudent(rs.getString("CNE"));
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public LinkedList<Student> getStudents(String id_class) {
        sql = "SELECT * FROM students WHERE id_class = '"
                + id_class + "'";
        LinkedList<Student> students = new LinkedList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Student student = getStudent(rs.getString("CNE"));
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}