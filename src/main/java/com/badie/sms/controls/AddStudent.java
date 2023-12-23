package com.badie.sms.controls;

import com.badie.sms.dao.ClassDb;
import com.badie.sms.dao.StudentDb;
import com.badie.sms.dao.UserDb;
import com.badie.sms.models.Class;
import com.badie.sms.models.Student;
import com.badie.sms.utils.Directories;
import com.badie.sms.utils.Gender;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AddStudent implements Initializable {
    static LinkedList<Student> students = new LinkedList<>();
    public Button add,update,clear,delete;
    StudentDb studentDb = new StudentDb();
    ClassDb classDb = new ClassDb();
    HashMap<String, Class> MapClass = classDb.getMapClass();
    @FXML
    TextField inPhone,inMail,inCNE, inLastName, inFirstName,inPass;
    @FXML
    DatePicker inAge;
    @FXML
    ChoiceBox<Gender> inGender;
    @FXML
    ChoiceBox<String> inClass;
    @FXML
    void onInputChanged(KeyEvent e){
        if (e.getSource() == inCNE){
            checkCURD(studentDb.exitStudent(inCNE.getText()));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Directories.initializeGender(inGender);
        Directories.initializeChoixBox(inClass, classDb.getMapClass());
        students = studentDb.getStudents();
        checkCURD(false);
    }
    @FXML
    public void clearInfo() {
        boolean choix = false;
        if (!inCNE.getText().isEmpty()
                || inGender.getSelectionModel().getSelectedItem() != null
                || !inFirstName.getText().isEmpty()
                || !inLastName.getText().isEmpty()
                || !inPass.getText().isEmpty()
                || !inMail.getText().isEmpty()
                || !inPhone.getText().isEmpty())
            choix = Directories.alert("clearClass info of student", Alert.AlertType.CONFIRMATION);
        if (choix) {
           clearInfoInput();
        }
    }
    @FXML
    public void addStudent(){
        if (isInfoInputEmpty()){
            Directories.alert("Please fill all blank fields",
                    Alert.AlertType.ERROR);
        }else {
                Student student = new Student();
                student.CNE = inCNE.getText();
                student.age = inAge.getValue();
                student.gender = inGender.getSelectionModel().getSelectedItem();
                student.email = inMail.getText();
                student.firstName = inFirstName.getText();
                student.lastName = inLastName.getText();
                student.id_class =  MapClass.keySet().stream().toList().get(inClass.getSelectionModel().getSelectedIndex());
                student.pass = inPass.getText();
                student.mobileNumber =inPhone.getText();
                studentDb.setStudent(student);
                if (studentDb.exitStudent(student.CNE)){
                    Directories.alert("Student has successfully added",
                            Alert.AlertType.INFORMATION);
                }
                clearInfoInput();
        }
    }
    private boolean isInfoInputEmpty() {
        return inCNE.getText().isEmpty()
                || inGender.getSelectionModel().getSelectedItem() == null
                || inFirstName.getText().isEmpty()
                || inLastName.getText().isEmpty()
                || inPass.getText().isEmpty()
                || inAge.getValue() == null
                || inMail.getText().isEmpty()
                || inPhone.getText().isEmpty();
    }
    private void clearInfoInput(){
        inCNE.setText("");
        inPhone.setText("");
        inMail.setText("");
        inAge.setValue(null);
        inPass.setText("");
        inLastName.setText("");
        inFirstName.setText("");
        inClass.getSelectionModel().clearSelection();
        inGender.getSelectionModel().clearSelection();
        checkCURD(false);
    }
    public void checkCURD(boolean isExiting){
            add.setDisable(isExiting);
            if (isExiting)
                inCNE.getStyleClass().add("active");
            else
                inCNE.getStyleClass().remove("active");
    }

}
