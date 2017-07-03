/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Main_appviewController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private Label customer_name;
    @FXML
    private JFXButton hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXButton go_back;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScrollPane sidePane;
        //drawer.setStyle("-fx-background-color:tranparent");
        drawer.setPrefHeight(-1);
        
        try {
            customer_name.setText(Trip_plan.user_name);
            
            PreparedStatement ps = ob.con.prepareStatement("select event from day_plan where event like 'Return from city %' and day>=? and user_id_fk=?");
           // System.out.println(LocalDate.now().toString());
            ps.setString(1, LocalDate.now().toString());
            ps.setString(2, Trip_plan.user_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == false) {
                borderpane.setCenter(FXMLLoader.load(getClass().getResource("select_city.fxml")));
                sidePane = FXMLLoader.load(getClass().getResource("drawer.fxml"));
            drawer.setSidePane(sidePane);
                borderpane.setRight(drawer);
                borderpane.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (drawer.isShown()) {
                            drawer.close();
                        }
                    }
                });
            } else {
                rs.absolute(1);
                String city = rs.getString(1).substring(17);
              //  System.out.println(LocalDate.now().toString());
                Trip_plan.city = city;
                ps = ob.con.prepareStatement("select day from day_plan where event like 'Return from city %' and day>=? and user_id_fk=?");
                ps.setString(1, LocalDate.now().toString());
                ps.setString(2, Trip_plan.user_id);
                rs = ps.executeQuery();
                rs.absolute(1);
                Trip_plan.city_end=LocalDate.parse(rs.getString(1));
                ps = ob.con.prepareStatement("select day from day_plan where event like 'Go to city %' and user_id_fk=?");
                ps.setString(1, Trip_plan.user_id);
                rs = ps.executeQuery();
                rs.absolute(1);
                Trip_plan.city_start=LocalDate.parse(rs.getString(1));
                
              //  System.out.println("----"+Trip_plan.city_start);
                borderpane.setCenter(FXMLLoader.load(getClass().getResource("Inside_city.fxml")));
                sidePane = FXMLLoader.load(getClass().getResource("drawer.fxml"));
            drawer.setSidePane(sidePane);
            borderpane.setRight(drawer);
            }
        } catch (IOException ex) {
            Logger.getLogger(Main_appviewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        // TODO

    }

    @FXML
    private void open_drawer(ActionEvent event) {
        if (drawer.isShown()) {
            drawer.close();
        } else {
            drawer.open();
        }
    }

    @FXML
    void reset(ActionEvent event) throws IOException {
        Parent root = go_back.getScene().getRoot();
        Trip_plan.city = "";
        Trip_plan.city_start = LocalDate.now();
        root = FXMLLoader.load(getClass().getResource("Main_appview.fxml"));
        Stage stage = (Stage) go_back.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        Parent root = go_back.getScene().getRoot();
        Trip_plan.city = "";
        Trip_plan.user_name="";
        root = FXMLLoader.load(getClass().getResource("start_page.fxml"));
        Stage stage = (Stage) go_back.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
