/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Start_pageController implements Initializable {
    @FXML
    private JFXButton login;
    @FXML
    private JFXButton signup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void open_login(ActionEvent event) throws IOException {
        Parent root=login.getScene().getRoot();
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage)login.getScene().getWindow();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void open_signup(ActionEvent event) throws IOException {
              Parent root=login.getScene().getRoot();
        root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Stage stage = (Stage)login.getScene().getWindow();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
}
