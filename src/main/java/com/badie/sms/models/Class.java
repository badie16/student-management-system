package com.badie.sms.models;

import java.util.LinkedList;

public class Class {
    public String Name;
    public String id_class;
    public int numberOfStudent;
    public int numberOfStudentMax;
    public String admin;
    public LinkedList <Course>courses= new LinkedList<>();
    @Override
    public String toString() {
        return Name;
    }
}
