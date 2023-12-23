package com.badie.sms.controls;

import com.badie.sms.dao.CourseDb;
import com.badie.sms.dao.ProfDb;
import com.badie.sms.models.Course;
import com.badie.sms.models.Prof;
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
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ManageProfs implements Initializable {
    public Button update,view,delete;
    ProfDb profDb = new ProfDb();
    CourseDb courseDb = new CourseDb();
    @FXML
    TextField inPhone,inMail,inID, inLastName, inFirstName,inPass;
    @FXML
    ChoiceBox<Gender> inGender;
    @FXML
    TableView<Prof> tableProf;
    @FXML
    TableColumn<Prof,String> colID, colLastName, colFirstName,colEmail, colNbrOfCourse,colGender;
    Prof profFocus =new Prof();
    @FXML
    AnchorPane viewPage;
    @FXML
    void handleMouseAction(MouseEvent e){
        if (e.getSource() == tableProf) {
            Prof info = tableProf.getSelectionModel().getSelectedItem();
            if (info != null) {
                profFocus = profDb.getProf(info.id_prof);
                checkClickOnTable(true);
            }else{
                checkClickOnTable(false);
                tableProf.getSelectionModel().clearSelection();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Directories.initializeGender(inGender);
        showProfsInTable();
    }
    public void showProfsInTable(){
        ObservableList<Prof> observableList = FXCollections.observableArrayList(profDb.getProfs().values());
        colID.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().id_prof));
        colEmail.setCellValueFactory(e->new SimpleStringProperty(e.getValue().email));
        colLastName.setCellValueFactory(e-> new SimpleStringProperty(e.getValue().lastName));
        colFirstName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().firstName));
        colNbrOfCourse.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(courseDb.getCourses(e.getValue().id_prof).size())));
        colGender.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().gender.toString()));
        tableProf.setItems(observableList);
        checkClickOnTable(false);
    }
    @FXML
    public void updateProf(){
        if (isInfoInputEmpty()){
            Directories.alert("Please fill all blank fields",
                    Alert.AlertType.ERROR);
        }else {
            boolean choix = Directories.alert("Update Prof ?", Alert.AlertType.CONFIRMATION);
            if (choix) {
                Prof prof = new Prof();
                prof.id_prof = inID.getText();
                prof.gender = inGender.getSelectionModel().getSelectedItem();
                prof.email = inMail.getText();
                prof.firstName = inFirstName.getText();
                prof.lastName = inLastName.getText();
                prof.pass = inPass.getText();
                prof.mobileNumber = inPhone.getText();
                profDb.updateProf(prof);
                Directories.alert("Professor has successfully update",
                        Alert.AlertType.INFORMATION);
            }
        }
    }
    @FXML
    private void deleteProf(){
        boolean choix = Directories.alert("Are you sure you want to DELETE Prof ID = " + profFocus.id_prof + " ?", Alert.AlertType.CONFIRMATION);
        if (choix){
            LinkedList<Course> courses = courseDb.getCourses(profFocus.id_prof);
            for (Course course : courses) {
                course.id_prof = "";
                courseDb.updateCourse(course);
            }
            profDb.removeProf(profFocus.id_prof);
            if (!profDb.exitProf(profFocus.id_prof))
                Directories.alert("Professor has successfully removed",
                        Alert.AlertType.INFORMATION);
            showProfsInTable();
        }
    }
    private boolean isInfoInputEmpty() {
        return inID.getText().isEmpty()
                || inGender.getSelectionModel().getSelectedItem() == null
                || inFirstName.getText().isEmpty()
                || inLastName.getText().isEmpty()
                || inPass.getText().isEmpty()
                || inMail.getText().isEmpty()
                || inPhone.getText().isEmpty();
    }
    public void checkClickOnTable(boolean isClick){
        delete.setDisable(!isClick);
        update.setDisable(!isClick);
    }
    public void viewProf(ActionEvent actionEvent) {
        if (!profDb.exitProf(profFocus.id_prof))return;
        inID.setText(profFocus.id_prof);
        inLastName.setText(profFocus.lastName);
        inFirstName.setText(profFocus.firstName);
        inPass.setText(profFocus.pass);
        inMail.setText(profFocus.email);
        inPhone.setText(profFocus.mobileNumber);
        inGender.getSelectionModel().select(profFocus.gender);
        viewPage.setVisible(true);
    }
}
