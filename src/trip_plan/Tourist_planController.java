/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author rohan
 */
public class Tourist_planController implements Initializable {
    
    @FXML
    private ImageView spot_pic;
    @FXML
    private JFXTimePicker time_of_spot;
    @FXML
    private Label spot_name;
    @FXML
    private JFXButton add;
    private String spot_name1;
    @FXML
    private DatePicker date_of_visit;
    @FXML
    private Label err_lab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            // TODO
            final Callback<DatePicker, DateCell> dayCelFactory
                    = new Callback<DatePicker, DateCell>() {
                        @Override
                        public DateCell call(final DatePicker datePicker) {
                            return new DateCell() {
                                @Override
                                public void updateItem(LocalDate item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item.isAfter(
                                            Trip_plan.city_end) || (item.isBefore(Trip_plan.city_start))) {
                                        //  System.out.println("hey");
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                            };
                        }
                    };
           
            date_of_visit.setDayCellFactory(dayCelFactory);
            PreparedStatement ps = ob.con.prepareStatement("select tsp_id from tourist_spot where city=? and name=?");
            ps.setString(1, Trip_plan.city);
            spot_name.setText(Trip_plan.spot_selected);
            StringTokenizer stt = new StringTokenizer(Trip_plan.spot_selected, " ");
            String sfr = "";
            while (stt.hasMoreTokens()) {
                String ghh = stt.nextToken();
                if (stt.hasMoreTokens()) {
                    sfr += ghh + "_";
                } else {
                    sfr += ghh;
                }
            }
            ps.setString(2, sfr);
            spot_name1 = sfr;
            // System.out.println(Trip_plan.hotel_selected+" "+Trip_plan.city+" "+sfr);
            ResultSet rs = ps.executeQuery();
            String st;
            rs.absolute(1);
            st = rs.getString(1);
            //    System.out.println(st);
            ps = ob.con.prepareStatement("select count(*) from tourist_spot_images where tsp_id=?");
            ps.setString(1, st);
            rs = ps.executeQuery();
            rs.absolute(1);
            int no_im_hot = rs.getInt(1);
            ps = ob.con.prepareStatement("select Location from tourist_spot_images where tsp_id=?");
            ps.setString(1, st);
            rs = ps.executeQuery();
            rs.absolute(1);
            File file = new File(rs.getString(1));
            Image it = new Image(file.toURI().toString());
            spot_pic.setImage(it);
            
        } catch (SQLException ex) {
            Logger.getLogger(Book_roomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void add_to_planner(ActionEvent event) throws SQLException {
        PreparedStatement ps = ob.con.prepareStatement("select event from day_plan where time=? and day=? and user_id_fk=?");
        System.out.println(time_of_spot.getValue().toString() + " " + date_of_visit.getValue().toString());
        ps.setString(1, time_of_spot.getValue().toString());
        ps.setString(2, date_of_visit.getValue().toString());
        ps.setString(3, Trip_plan.user_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next() == false) {
            ps = ob.con.prepareStatement("insert into day_plan values (?,?,?,?)");
            ps.setString(1, time_of_spot.getValue().toString());
            String eve = "Go to " + spot_name.getText();
            ps.setString(2, eve);
            ps.setString(3, Trip_plan.user_id);
            ps.setString(4, date_of_visit.getValue().toString());
            ps.executeUpdate();
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Congrats");
            tray.setMessage("Successfully added to Day planner!!");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
            Stage stg = (Stage) add.getScene().getWindow();
            Trip_plan.spot_booked = true;
            stg.close();
        } else {
            rs.absolute(1);
            String str = "Please select another time or day as you are going to " + rs.getString(1) + " at this time!!";
            err_lab.setText(str);
            
        }
        
    }
    
    @FXML
    private void close(ActionEvent event) {
        Stage stg = (Stage) add.getScene().getWindow();
        stg.close();
        Trip_plan.spot_booked = false;
    }
    
}
