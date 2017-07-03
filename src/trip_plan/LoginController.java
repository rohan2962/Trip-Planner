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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import static trip_plan.Trip_plan.ob;
/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class LoginController implements Initializable {
    @FXML
    private ImageView labelid;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwd;
    @FXML
    private JFXButton loginid;
    @FXML
    private Label invalid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        passwd.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ENTER){
                    try {
                        loginhandle();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            
        });
    }    

    @FXML
    private void loginhandle() throws SQLException, IOException {
        String un=username.getText();
        Trip_plan.user_id=un;
        String pwd=passwd.getText();
        PreparedStatement st=ob.con.prepareStatement("select Name from Customer where User_id=? and password = ?");
        st.setString(1,un);
        st.setString(2,pwd);
        ResultSet rs=st.executeQuery();
        
       if(rs.next()==false)
       {
            invalid.setVisible(true);
            invalid.setText("Invalid Credentials");
       }
       else
        {
        Parent root=loginid.getScene().getRoot();
        Trip_plan.user_name=rs.getString(1);
        root = FXMLLoader.load(getClass().getResource("Main_appview.fxml"));
        Stage stage = (Stage)loginid.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        }
    }
    
}
