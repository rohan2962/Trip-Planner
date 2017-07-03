/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import static trip_plan.Trip_plan.city;
import static trip_plan.Trip_plan.cnt_hotel;
import static trip_plan.Trip_plan.cnt_spots;
import static trip_plan.Trip_plan.spot_type;
import static trip_plan.Trip_plan.spot_desc;
import static trip_plan.Trip_plan.spot_name;
import static trip_plan.Trip_plan.spot_id;
import static trip_plan.Trip_plan.ob;
import static trip_plan.Trip_plan.type;
import static trip_plan.Trip_plan.hotel_id;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Inside_cityController implements Initializable {

    @FXML
    private AnchorPane about_pane;
    @FXML 
    private Tab hotel_tab;
    @FXML 
    private Tab tourist_tab;
    AnchorPane hotel_pane;
    ScrollPane scroll;
    AnchorPane tourist_pane;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {
                // TODO
                Accordion accordion = new Accordion();
                //accordion.setStyle("-fx-background-color:transparent");
                accordion.setMinWidth(900);
                tourist_pane=new AnchorPane();
                tourist_pane.getChildren().add(accordion);
                //tourist_pane.setStyle("-fx-background-color:transparent");
                scroll=new ScrollPane();
                scroll.setContent(tourist_pane);
                //scroll.setStyle("-fx-background-color:transparent");
                tourist_tab.setContent(scroll);
                //tourist_tab.setStyle("-fx-background-color:transparent");
                if(Trip_plan.city.compareTo("Goa")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("goa_about.fxml")));
                }
                else if(Trip_plan.city.compareTo("Agra")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("agra_about.fxml")));
                }
                else if(Trip_plan.city.compareTo("Delhi")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("delhi_about.fxml")));
                }
                else if(Trip_plan.city.compareTo("Mumbai")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("mumbai_about.fxml")));
                }
                else if(Trip_plan.city.compareTo("Jaipur")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("jaipur_about.fxml")));
                }
                else if(Trip_plan.city.compareTo("Bangalore")==0){
                    about_pane.getChildren().add(FXMLLoader.load(getClass().getResource("bangalore_about.fxml")));
                }
                PreparedStatement ps = ob.con.prepareStatement("select count(*) from tourist_spot where city=?");
                ps.setString(1, Trip_plan.city);
                ResultSet rs = ps.executeQuery();
                rs.absolute(1);
                cnt_spots = rs.getInt(1);
               // System.out.println(Trip_plan.city);
                ps = ob.con.prepareStatement("select name,type,description,tsp_id from tourist_spot where city=? order by type desc ");
                ps.setString(1, Trip_plan.city);
                rs = ps.executeQuery();
                TitledPane[] tp = new TitledPane[cnt_spots];
                //System.out.println(cnt_spots); 
                for (int i = 0; i < cnt_spots; i++) {

                    rs.absolute(i + 1);

                    spot_name = rs.getString(1);
                    spot_type = rs.getString(2);
                    spot_desc = rs.getString(3);
                    spot_id = rs.getString(4);
                    tp[i] = new TitledPane();
                    //tp[i].setStyle("-fx-background-color:transparent");
                    tp[i] = FXMLLoader.load(getClass().getResource("tourist_spot_prototype.fxml"));
                }
                accordion.getPanes().addAll(tp);
                accordion.setExpandedPane(tp[0]);
                //accordion.setStyle(type);
                try {
                    hotel_pane = new AnchorPane();
                    hotel_pane=FXMLLoader.load(getClass().getResource("hotel_details.fxml"));
                    hotel_tab.setContent(hotel_pane);
                    //hotel_pane.setStyle("-fx-background-color:transparent");
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(Inside_cityController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException | IOException ex) {
               System.out.println(ex);
            }
        } 
            
      

}
