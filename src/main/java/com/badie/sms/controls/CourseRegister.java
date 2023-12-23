package com.badie.sms.controls;

import com.badie.sms.dao.ClassDb;
import com.badie.sms.dao.CourseDb;
import com.badie.sms.dao.ProfDb;

import com.badie.sms.models.Course;
import com.badie.sms.models.Prof;
import com.badie.sms.utils.Directories;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CourseRegister implements Initializable {
    static HashMap<String,Course> courses = new HashMap<>();
    static HashMap<String, Prof> faculties = new HashMap<>();
    public Button addCourse, updateCourse, clearCourse, deleteCourse;
    CourseDb courseDb = new CourseDb();
    ProfDb profDb = new ProfDb();
    public TextField inNameCourse,inIDCourse;
    public ChoiceBox<String> inProfChoice;
    public TableColumn<Course,String> colNameCourse, colFacultyCourse,colIDCourse;
    public TableView<Course> tableCourse;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        faculties = profDb.getProfs();
        Directories.initializeChoixBox(inProfChoice,faculties);
        showCourseInTable();
        checkCURD(false);
    }
    @FXML
    public void handleMouseAction(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == tableCourse) {
            Course info = tableCourse.getSelectionModel().getSelectedItem();
            if (info != null) {
                Course course = courseDb.getCourse(info.id_course);
                inNameCourse.setText(course.Name);
                inIDCourse.setText(course.id_course);
                inProfChoice.getSelectionModel().select(profDb.getProf(course.id_prof).toString());
                checkCURD(true);
            }
        }
    }
    private void showCourseInTable(){
        courses = courseDb.getMapCourses();
        ObservableList<Course> observableList = FXCollections.observableArrayList(courses.values());
        colNameCourse.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().Name));
        colIDCourse.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().id_course));
        colFacultyCourse.setCellValueFactory(e -> new SimpleStringProperty(profDb.getProf(e.getValue().id_prof).toString()));
        tableCourse.setItems(observableList);
    }
    @FXML
    public void clearInfo() {
        boolean choix = false;
            if (!inIDCourse.getText().isEmpty()
                    ||!inNameCourse.getText().isEmpty()
                    || inProfChoice.getSelectionModel().getSelectedItem() != null)
                choix = Directories.alert("clearClass info of Course", Alert.AlertType.CONFIRMATION);
            if (choix) {
                clearInfoInput();
            }
    }
    private void clearInfoInput(){
        inNameCourse.setText("");
        inIDCourse.setText("");
        inProfChoice.getSelectionModel().clearSelection();
        tableCourse.getSelectionModel().clearSelection();
        checkCURD(false);
    }
    public void add() {
            if (isInfoInputEmpty()){
                Directories.alert("Please fill all blank fields",
                        Alert.AlertType.ERROR);
            }else {
                Course course = new Course();
                course.Name = inNameCourse.getText();
                course.id_course = inIDCourse.getText();
                course.id_prof = faculties.keySet().stream().toList().get(inProfChoice.getSelectionModel().getSelectedIndex());
                courseDb.setCourse(course);
                showCourseInTable();
                clearInfoInput();
            }
    }
    public void update() {
            if (isInfoInputEmpty()){
                Directories.alert("Please fill all blank fields",
                        Alert.AlertType.ERROR);
            }else {
                boolean choix = Directories.alert("Update Course ?", Alert.AlertType.CONFIRMATION);
                if (choix) {
                    Course course = new Course();
                    course.Name = inNameCourse.getText();
                    course.id_course = inIDCourse.getText();
                    course.id_prof = faculties.keySet().stream().toList().get(inProfChoice.getSelectionModel().getSelectedIndex());
                    courseDb.updateCourse(course);
                    showCourseInTable();
                    clearInfoInput();
                }
            }

    }
    public void delete() {
            if (courseDb.exitCourseInClass(inIDCourse.getText())){
                Directories.alert("you can't  delete Course " + inNameCourse.getText() + " \n its a course of class ", Alert.AlertType.WARNING);
                return;
            }
            boolean choix = Directories.alert("Are you sure you want to DELETE Course " + inNameCourse.getText() + " ?", Alert.AlertType.CONFIRMATION);
            if (choix) {
                courseDb.removeCourse(inIDCourse.getText());
                clearInfoInput();
                showCourseInTable();
            }
    }
    @FXML
    public void onInputChanged() {
            checkCURD(courseDb.exitCourse(inIDCourse.getText()));
    }

    private boolean isInfoInputEmpty() {
        return inIDCourse.getText().isEmpty()
                ||inNameCourse.getText().isEmpty()
                || inProfChoice.getSelectionModel().getSelectedItem() == null;
    }
    private void checkCURD(boolean isExiting){

            addCourse.setDisable(isExiting);
            updateCourse.setDisable(!isExiting);
            deleteCourse.setDisable(!isExiting);
    }
}
