module com.badie.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.badie.sms to javafx.fxml;
    exports com.badie.sms;
    exports com.badie.sms.models;
    opens com.badie.sms.models to javafx.fxml;
    exports com.badie.sms.controls;
    opens com.badie.sms.controls to javafx.fxml;
    exports com.badie.sms.utils;
    opens com.badie.sms.utils to javafx.fxml;
}