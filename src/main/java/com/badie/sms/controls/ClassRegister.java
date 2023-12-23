package com.badie.sms.controls;
import com.badie.sms.dao.ClassDb;
import com.badie.sms.dao.CourseDb;
import com.badie.sms.dao.ProfDb;
import com.badie.sms.dao.StudentDb;
import com.badie.sms.models.Class;
import com.badie.sms.models.Course;
import com.badie.sms.models.Prof;
import com.badie.sms.models.Student;
import com.badie.sms.utils.Directories;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

public class ClassRegister implements Initializable {
    static List<Class> listClass = new LinkedList<>();
    static HashMap<String,Course> courses = new HashMap<>();
    static HashMap<String, Prof> faculties = new HashMap<>();
    public Button addClass, updateClass, clearClass, deleteClass;
    ClassDb classDb = new ClassDb();
    CourseDb courseDb = new CourseDb();
    ProfDb profDb = new ProfDb();
    public ChoiceBox<String> inCourse1,inCourse2,inCourse3,inCourse4,inCourse5,inCourse6;
    public LinkedList<ChoiceBox<String>> inCoursesChoice = new LinkedList<>();
    public TextField inName,inID, inCapacity;
    public ChoiceBox<String> inResponsibleChoice;
    public TableColumn<Class,String> colName, colCapacity, colId;
    public TableView<Class> tableClass;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inCoursesChoice.add(inCourse1);
        inCoursesChoice.add(inCourse2);
        inCoursesChoice.add(inCourse3);
        inCoursesChoice.add(inCourse4);
        inCoursesChoice.add(inCourse5);
        inCoursesChoice.add(inCourse6);
        listClass = classDb.getListClass();
        faculties = profDb.getProfs();
        courses = courseDb.getMapCourses();
        Directories.initializeChoixBox(inResponsibleChoice,faculties);
        initializeCourse();
        showClassInTable();
        checkCURD(false);
    }
    private  void initializeCourse(){
        for (ChoiceBox<String> inCourse : inCoursesChoice) {
            Directories.initializeChoixBox(inCourse,courseDb.getMapCourses());
        }
    }
    @FXML
    public void handleMouseAction(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == tableClass) {
            Class info = tableClass.getSelectionModel().getSelectedItem();
            if (info != null) {
                Class _class = classDb.getClass(info.id_class);
                inID.setText(_class.id_class);
                inName.setText(_class.Name);
                inName.setText(_class.Name);
                inCapacity.setText(String.valueOf(_class.numberOfStudentMax));
                inResponsibleChoice.getSelectionModel().select(profDb.getProf(_class.admin).toString());
                for (int i = 0; i < _class.courses.size(); i++) {
                    inCoursesChoice.get(i).getSelectionModel().select(_class.courses.get(i).toString());
                }
                checkCURD(true);
            }
        }
    }
    private void showClassInTable() {
        listClass = classDb.getListClass();
        ObservableList<Class> observableList = FXCollections.observableArrayList();
        observableList.addAll(listClass);
        colId.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().id_class));
        colName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().Name));
        colCapacity.setCellValueFactory(e->new SimpleStringProperty(e.getValue().numberOfStudent+"/"+e.getValue().numberOfStudentMax));
        tableClass.setItems(observableList);
    }
    @FXML
    public void clearInfo() {
        boolean choix = false;

            if (!inName.getText().isEmpty()
                    || inResponsibleChoice.getSelectionModel().getSelectedItem() != null
                    || !inCapacity.getText().isEmpty())
                choix = Directories.alert("clearClass info of Class", Alert.AlertType.CONFIRMATION);
            if (choix) {
                clearInfoInputClass();
            }
    }
    private void clearInfoInputClass(){
        inName.setText("");
        inID.setText("");
        inCapacity.setText("");
        inResponsibleChoice.getSelectionModel().clearSelection();
        for (ChoiceBox<String> inCourse : inCoursesChoice) {
            inCourse.getSelectionModel().clearSelection();
        }
        tableClass.getSelectionModel().clearSelection();
        checkCURD(false);
    }
    public void add() {
            if (isInfoInputEmpty()){
                Directories.alert("Please fill all blank fields",
                        Alert.AlertType.ERROR);
            }else {
                Class _class = new Class();
                _class.id_class = inID.getText();
                _class.Name = inName.getText();
                _class.numberOfStudentMax = Integer.parseInt(inCapacity.getText());
                _class.admin = faculties.keySet().stream().toList().get(inResponsibleChoice.getSelectionModel().getSelectedIndex());
                _class.courses.clear();
                for (ChoiceBox<String> inCourse : inCoursesChoice) {
                    Course course= courseDb.getCourse(courses.keySet().stream().toList().get(inCourse.getSelectionModel().getSelectedIndex()));
                    _class.courses.add(course);
                }
                classDb.setClass(_class);
                showClassInTable();
                clearInfoInputClass();
            }
    }
    public void update() {
            if (isInfoInputEmpty()){
                Directories.alert("Please fill all blank fields",
                        Alert.AlertType.ERROR);
            }else {
                boolean choix = Directories.alert("Update Class ?", Alert.AlertType.CONFIRMATION);
                if (choix) {
                    Class _class = new Class();
                    _class.Name = inName.getText();
                    _class.id_class = inID.getText();
                    _class.numberOfStudentMax = Integer.parseInt(inCapacity.getText());
                    _class.admin = faculties.keySet().stream().toList().get(inResponsibleChoice.getSelectionModel().getSelectedIndex());
                    _class.courses.clear();
                    for (ChoiceBox<String> inCourse : inCoursesChoice) {
                        Course course= courseDb.getCourse(courses.keySet().stream().toList().get(inCourse.getSelectionModel().getSelectedIndex()));
                        _class.courses.add(course);
                    }
                    classDb.updateClass(_class);
                    showClassInTable();
                    clearInfoInputClass();
                }
            }
    }
    public void delete() {
            LinkedList<Student> students= new StudentDb().getStudents(inID.getText());
            if (students.size() != 0){
                Directories.alert("this class have a students ", Alert.AlertType.WARNING);
            }
            boolean choix = Directories.alert("Are you sure you want to DELETE Class " + inName.getText() + " ?", Alert.AlertType.CONFIRMATION);
            if (choix) {
                for (Student student : students) {
                    new StudentDb().removeStudent(student.CNE);
                }
                classDb.removeClass(inID.getText());
                clearInfoInputClass();
                showClassInTable();

            }
    }
    @FXML
    public void onInputChanged() {
            checkCURD(classDb.exitClass(inID.getText()));
    }
    private boolean isInfoInputEmpty() {
        for (ChoiceBox<String> inCourse : inCoursesChoice) {
            if (inCourse.getSelectionModel().getSelectedItem() == null)
                return true;
        }
        return inName.getText().isEmpty()
                || inResponsibleChoice.getSelectionModel().getSelectedItem() == null
                || inCapacity.getText().isEmpty()
                || inID.getText().isEmpty();
    }

    private void checkCURD(boolean isExiting){
            addClass.setDisable(isExiting);
            updateClass.setDisable(!isExiting);
            deleteClass.setDisable(!isExiting);
    }
}
