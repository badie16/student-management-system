package com.badie.sms.models;
import java.time.LocalDate;
public class Student extends User{
    public String CNE;
    public String id_class;
    public LocalDate age;
    public Student(){

    }
    public Student(User user){
        super(user);
    }
}
