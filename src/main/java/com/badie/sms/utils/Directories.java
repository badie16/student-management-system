package com.badie.sms.utils;
import com.badie.sms.MainJar;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public interface Directories {
    String loginAdmin = "fxml/AdminLogin.fxml";
    String AdminHome = "fxml/AdminHome.fxml";
    String AddStudentPage = "fxml/AddStudent.fxml";
    String AddProfPage = "fxml/AddProf.fxml";
    String ManageStudentPage = "fxml/ManageStudents.fxml";
    String ManageProfPage = "fxml/ManageProfs.fxml";
    String ClassPage = "fxml/ClassRegister.fxml";
    String CoursePage = "fxml/CourseRegister.fxml";
    String IconLogin = "img/login.png";
    String IconHome = "img/icons/home.png " ;
    String IconStudent = "img/icons8_user_64px.png";
    int NbrOfCourseInOnClass = 6;
    static URL url(String str){
        return MainJar.class.getResource(str);
    }
    static Stage stageFromEvent(ActionEvent e){
        return  (Stage) (((Node)e.getSource()).getScene().getWindow());
    }
    static void initializeGender(ChoiceBox<Gender> choiceBox){
        choiceBox.setItems(FXCollections.observableArrayList(Gender.male,Gender.Female));
    }
    static void initializeChoixBox(ChoiceBox<String> choiceBox, LinkedList<?> list) {
        ArrayList<String> list2 = new ArrayList<>();
        for (Object o : list) {
            list2.add(o.toString());
        }
        choiceBox.setItems(FXCollections.observableArrayList(list2));
    }
    static void initializeChoixBox(ChoiceBox<String> choiceBox, HashMap<String,?> map) {
        ArrayList<String> list = new ArrayList<>();
        for (Object value : map.values()) {
            list.add(value.toString());
        }
        choiceBox.setItems(FXCollections.observableArrayList(list));
    }
    static boolean alert(String text, Alert.AlertType type){
        Alert alert;
        alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(text);
        Optional<ButtonType> option = alert.showAndWait();
        return option.get().equals(ButtonType.OK);
    }
}
