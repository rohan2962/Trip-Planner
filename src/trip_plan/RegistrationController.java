/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.management.Notification;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class RegistrationController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField email_id;
    @FXML
    private TextField phone;
    @FXML
    private JFXButton register;
    @FXML
    private Label user_error;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void register_cutomer(ActionEvent event) throws SQLException, IOException, InterruptedException {
        String nam = name.getText();
        String user_id = username.getText();
        PreparedStatement st = ob.con.prepareStatement("select count(user_id) from Customer where user_id = ?");
        st.setString(1, user_id);
        ResultSet rs = st.executeQuery();
        int c;
        if (rs.next()) {
            c = rs.getInt(1);
            if (c > 0) {
                username.setText(" ");
                user_error.setText("User Name Already Exists");
            } else {
                String email = email_id.getText();
                String pwd = password.getText();
                Double phone_no = Double.parseDouble(phone.getText());
                st = ob.con.prepareStatement("insert into Customer values(? ,? ,? ,? ,?)");
                String g=phone.getText();
                String y=email_id.getText();
                if(g.length()<10)
                {
                    user_error.setText("Please enter a valid phone no!!");
                }
                else if(y.contains("@")==false||y.contains(".")==false)
                {
                    user_error.setText("Please enter a valid Email id!!");
                }
                else
                {
                st.setString(1, nam);
                st.setString(2, user_id);
                st.setString(3, pwd);
                st.setString(4, email);
                st.setDouble(5, phone_no);
                int i = st.executeUpdate();
                TrayNotification tray=new TrayNotification();
                tray.setTitle("Congrats");
                tray.setMessage("Successfully Registered!!");
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
                Parent root = register.getScene().getRoot();
                root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = (Stage) register.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();}
            }
        }
    }
    
}
