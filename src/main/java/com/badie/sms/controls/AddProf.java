package com.badie.sms.controls;

import com.badie.sms.dao.ProfDb;
import com.badie.sms.models.Prof;
import com.badie.sms.utils.Directories;
import com.badie.sms.utils.Gender;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProf implements Initializable {
    public Button add,clear;
    ProfDb profDb = new ProfDb();
    @FXML
    TextField inPhone,inMail,inID, inLastName, inFirstName,inPass;
    @FXML
    ChoiceBox<Gender> inGender;
    @FXML
    void onInputChanged(KeyEvent e){
        if (e.getSource() == inID){
            checkCURD(profDb.exitProf(inID.getText()));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Directories.initializeGender(inGender);
        checkCURD(false);
    }
    @FXML
    public void clearInfo() {
        boolean choix = false;
        if (!inID.getText().isEmpty()
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
    public void addProf(){
        if (isInfoInputEmpty()){
            Directories.alert("Please fill all blank fields",
                    Alert.AlertType.ERROR);
        }else {
            Prof prof = new Prof();
            prof.id_prof = inID.getText();
            prof.gender = inGender.getSelectionModel().getSelectedItem();
            prof.email = inMail.getText();
            prof.firstName = inFirstName.getText();
            prof.lastName = inLastName.getText();
            prof.pass = inPass.getText();
            prof.mobileNumber = inPhone.getText();
            profDb.setProf(prof);
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
    private void clearInfoInput(){
        inID.setText("");
        inPhone.setText("");
        inMail.setText("");
        inPass.setText("");
        inLastName.setText("");
        inFirstName.setText("");
        inGender.getSelectionModel().clearSelection();
        checkCURD(false);
    }
    public void checkCURD(boolean isExiting){
        add.setDisable(isExiting);
        if (isExiting)
            inID.getStyleClass().add("active");
        else
            inID.getStyleClass().remove("active");
    }
}
