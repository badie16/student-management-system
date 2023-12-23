package com.badie.sms.controls;
import com.badie.sms.dao.*;
import com.badie.sms.models.Admin;
import com.badie.sms.models.Class;
import com.badie.sms.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
public class AdminHome implements Initializable{
    static Admin admin = new Admin();
    @FXML
    public AnchorPane home;
    @FXML
    public Label fullName,totalStudent,totalClass,totalFaculty,totalCourse;
    @FXML
    public PieChart pieCharOfClass;
    @FXML
    public PieChart pieCharOfGenderOfStudent;

    @FXML
    public Label email;
    public TextField lastNameProfile,firstNameProfile,emailProfile,phoneProfile,userProfile,passwordProfile;
    public TitledPane StudentSubMenu,ProfessorSubMenu;
    public Button ProfessorBtnMenu,StudentBtnMenu;
    @FXML
    Button btnAddStudent,btnManageStudents,btnHome, btnAddProf,btnManageProfs,btnCourse,btnClass,myProfile,signOut,changePass;
    LinkedList<Button> listMenu = new LinkedList<>();
    @FXML
    BorderPane pages;
    @FXML
    StackPane centerContent;
    @FXML
    Pane menuProfile;
    @FXML
    AnchorPane profileContent,profilePage;
    public void  start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Directories.url(Directories.AdminHome));
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Home");
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeProfile();
        initializeStat();
        listMenu.add(btnHome);
        listMenu.add(btnAddStudent);
        listMenu.add((btnClass));
        listMenu.add(btnAddProf);
        checkBtnActive(btnHome);
        showSubMenu(new ActionEvent());
    }
    @FXML
    private void handleButtonOfNavClicks(ActionEvent e) throws IOException {
        centerContent.getChildren().clear();
        if(e.getSource() == btnAddStudent){
            centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.AddStudentPage)));
        }else if (e.getSource() == btnManageStudents){
            centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.ManageStudentPage)));
        } else if (e.getSource() == btnHome){
            centerContent.getChildren().add(home);
            initializeStat();
        } else if (e.getSource() == btnAddProf) {
            centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.AddProfPage)));
        }else if (e.getSource() == btnManageProfs) {
                centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.ManageProfPage)));
        }else if (e.getSource() == btnClass) {
            centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.ClassPage)));
        } else if (e.getSource() == btnCourse) {
            centerContent.getChildren().add(FXMLLoader.load(Directories.url(Directories.CoursePage)));
        }
        profileContent.setVisible(false);
        profilePage.setVisible(false);
        menuProfile.setVisible(false);
        unShowSunsMenu();
        centerContent.getChildren().add(profileContent);
        checkBtnActive((Button) e.getSource());

    }
    private void initializeProfile() {
        email.setText(admin.email);
        fullName.setText(admin.toString());
        lastNameProfile.setText(admin.lastName);
        firstNameProfile.setText(admin.firstName);
        emailProfile.setText(admin.email);
        phoneProfile.setText(admin.mobileNumber);
        userProfile.setText(admin.user);
        passwordProfile.setText(admin.pass);
    }
    private void initializeStat() {
        StudentDb studentDb = new StudentDb();
        int studentMale = studentDb.totalStudentGender(Gender.male);
        int studentFemale = studentDb.totalStudentGender(Gender.Female);
        int TotalClass = new ClassDb().getListClass().size();
        int TotalFaculty = new ProfDb().getProfs().size();
        int TotalCourse = new CourseDb().getCourses().size();
//        totalStudentMale.setText(String.valueOf(studentMale));
//        totalStudentFemale.setText(String.valueOf(studentFemale));
        totalStudent.setText(String.valueOf(studentFemale+studentMale));
        totalFaculty.setText(String.valueOf(TotalFaculty));
        totalClass.setText(String.valueOf(TotalClass));
        totalCourse.setText(String.valueOf(TotalCourse));
        ObservableList<PieChart.Data> observableList = FXCollections.observableArrayList();
        for (Class _class : new ClassDb().getListClass()) {
            PieChart.Data data = new PieChart.Data(_class.Name,_class.numberOfStudent);
            observableList.add(data);
        }
        pieCharOfClass.setData(observableList);
        PieChart.Data dataMale = new PieChart.Data(Gender.male.toString(),studentMale);
        PieChart.Data dataFemale = new PieChart.Data(Gender.Female.toString(),studentFemale);
        pieCharOfGenderOfStudent.setData(FXCollections.observableArrayList(dataMale,dataFemale));
    }
    private void checkBtnActive(Button btn){
        for (Button child : listMenu) {
            child.getParent().getStyleClass().remove("active");
            if (child== btn){
                child.getParent().getStyleClass().add("active");
            }
        }
    }
    public void showMenuProfile() {
        if (!profilePage.isVisible()){
            profileContent.setVisible(!menuProfile.isVisible());
        }
        menuProfile.setVisible(!menuProfile.isVisible());
        unShowSunsMenu();
    }
    public void updateProfile() {
        if (!emailProfile.getText().isEmpty() &&
                !phoneProfile.getText().isEmpty() &&
                !userProfile.getText().isEmpty() &&
                !lastNameProfile.getText().isEmpty() &&
                !passwordProfile.getText().isEmpty() &&
                !firstNameProfile.getText().isEmpty()){
            String user = admin.user;
            admin.user = userProfile.getText();
            admin.lastName = lastNameProfile.getText();
            admin.firstName = firstNameProfile.getText();
            admin.mobileNumber = phoneProfile.getText();
            admin.email = emailProfile.getText();
            admin.pass = passwordProfile.getText();
            boolean chick = new AdminDb().updateAdmin(admin,user);
            if (chick){
                Directories.alert("Admin Profile has successfully updated",
                        Alert.AlertType.INFORMATION);
            }
        }else {
            Directories.alert("Please fill all blank fields",
                    Alert.AlertType.ERROR);
        }
    }

    public void clickBtnMenuProfile(ActionEvent e) {
        profileContent.setVisible(true);
        menuProfile.setVisible(false);
        if (e.getSource() == myProfile){
            profilePage.setVisible(true);
        }else if (e.getSource() == changePass){
            System.out.println("change pass");
        } else if (e.getSource() == signOut) {
            Stage stage = Directories.stageFromEvent(e);
            stage.hide();
            try {
                new AdminLogin().start(stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void showSubMenu(ActionEvent e) {
        if (e.getSource() == StudentBtnMenu){
            StudentSubMenu.setExpanded(!StudentSubMenu.isExpanded());
            ProfessorSubMenu.setExpanded(false);
        }else if (e.getSource() == ProfessorBtnMenu){
            ProfessorSubMenu.setExpanded(!ProfessorSubMenu.isExpanded());
            StudentSubMenu.setExpanded(false);

        }
    }
    private void unShowSunsMenu(){
        ProfessorSubMenu.setExpanded(false);
        StudentSubMenu.setExpanded(false);
    }
}
