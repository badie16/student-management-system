package com.badie.sms;
import com.badie.sms.controls.AdminLogin;
import com.badie.sms.utils.MyConnection;
import javafx.application.Application;
import javafx.stage.Stage;
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        if (MyConnection.connection() != null){
            new AdminLogin().start(stage);
        }
    }
}
