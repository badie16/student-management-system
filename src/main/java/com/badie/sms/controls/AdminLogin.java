package com.badie.sms.controls;

import com.badie.sms.dao.AdminDb;
import com.badie.sms.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
public class AdminLogin{
    @FXML
    TextField user;
    @FXML
    PasswordField pass;
    AdminDb adminDB = new AdminDb();
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Directories.url(Directories.loginAdmin));
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.getIcons().add(0,new Image(Directories.url(Directories.IconLogin).toString()));
        stage.show();
    }
    @FXML
    public void btnSubmit(ActionEvent e){
       if (pass.getText().isEmpty() || user.getText().isEmpty()){
           Directories.alert("User or Pass is empty ", Alert.AlertType.ERROR);
       }else {
           if (adminDB.exitAdmin(user.getText(),pass.getText())){
                   AdminHome adminHome = new AdminHome();
                   AdminHome.admin = adminDB.getAdmin(user.getText());
                   Stage stage = Directories.stageFromEvent(e);
                   try {
                       stage.hide();
                       adminHome.start(stage);
                   }catch (IOException ex){
                       System.out.println(ex);
                   }

           }else {
               Directories.alert("Please check user name / password", Alert.AlertType.WARNING);
           }
       }
    }

    public void OnForgotPass(MouseEvent mouseEvent) {
        Directories.alert("Take a deep breath and relax, 😊" +
                " and you will remember the password 😏", Alert.AlertType.WARNING);
    }
}
