package com.badie.sms.controls;

import com.badie.sms.dao.ClassDb;
import com.badie.sms.dao.StudentDb;
import com.badie.sms.models.Class;
import com.badie.sms.models.Student;
import com.badie.sms.utils.Directories;
import com.badie.sms.utils.Gender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ManageStudents implements Initializable {
    static LinkedList<Student> students = new LinkedList<>();
    public Button update,delete,view;
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
    TableView <Student>tableStudent;
    @FXML
    TableColumn<Student,String>colCNE, colLastName, colFirstName,colEmail,colClass,colGender;
    Student studentFocus = new Student();
    @FXML
    AnchorPane viewPage;
    @FXML
    void handleMouseAction(MouseEvent e){
        if (e.getSource() == tableStudent) {
            Student info = tableStudent.getSelectionModel().getSelectedItem();
            if (info != null) {
                studentFocus = studentDb.getStudent(info.CNE);
                checkClickOnTable(true);
            }else{
                checkClickOnTable(false);
                tableStudent.getSelectionModel().clearSelection();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Directories.initializeGender(inGender);
        Directories.initializeChoixBox(inClass, classDb.getMapClass());
        students = studentDb.getStudents();
        showStudentsInTable();
    }
    public void showStudentsInTable(){
        students = studentDb.getStudents();
        ObservableList<Student> observableList = FXCollections.observableArrayList();
        observableList.addAll(students);
        colCNE.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().CNE));
        colClass.setCellValueFactory(e->new SimpleStringProperty(classDb.getClass(e.getValue().id_class).Name));
        colEmail.setCellValueFactory(e->new SimpleStringProperty(e.getValue().email));
        colLastName.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().lastName));
        colFirstName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().firstName));
        colGender.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().gender.toString()));
        tableStudent.setItems(observableList);
        checkClickOnTable(false);
    }
    @FXML
    public void updateStudent(){
        if (isInfoInputEmpty()){
            Directories.alert("Please fill all blank fields",
                    Alert.AlertType.ERROR);
        }else {
            boolean choix = Directories.alert("Update Student ?", Alert.AlertType.CONFIRMATION);
            if (choix) {
                Student student = new Student();
                student.CNE = inCNE.getText();
                student.age = inAge.getValue();
                student.gender = inGender.getValue();
                student.email = inMail.getText();
                student.firstName = inFirstName.getText();
                student.lastName = inLastName.getText();
                student.id_class =  MapClass.keySet().stream().toList().get(inClass.getSelectionModel().getSelectedIndex());
                student.pass = inPass.getText();
                student.mobileNumber =inPhone.getText();
                studentDb.updateStudent(student);
                Directories.alert("Student has successfully update",
                        Alert.AlertType.INFORMATION);
            }
        }
    }
    @FXML
    private void deleteStudent(){
        boolean choix = Directories.alert("Are you sure you want to DELETE Student CNE = " + studentFocus.CNE + " ?", Alert.AlertType.CONFIRMATION);
        if (choix){
            studentDb.removeStudent(studentFocus.CNE);
            if (!studentDb.exitStudent(studentFocus.CNE))
                Directories.alert("Student has successfully removed",
                        Alert.AlertType.INFORMATION);
            showStudentsInTable();
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
    public void checkClickOnTable(boolean isClick){
        delete.setDisable(!isClick);
        update.setDisable(!isClick);
    }

    public void viewStudent(ActionEvent actionEvent) {
        if (!studentDb.exitStudent(studentFocus.CNE))return;
        inCNE.setText(studentFocus.CNE);
        inLastName.setText(studentFocus.lastName);
        inFirstName.setText(studentFocus.firstName);
        inPass.setText(studentFocus.pass);
        inMail.setText(studentFocus.email);
        inPhone.setText(studentFocus.mobileNumber);
        inAge.setValue(studentFocus.age);
        inGender.getSelectionModel().select(studentFocus.gender);
        inClass.getSelectionModel().select(classDb.getClass(studentFocus.id_class).Name);
        viewPage.setVisible(true);
    }
}
