/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import static trip_plan.Trip_plan.hotel_id;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author rohan
 */
public class Book_roomController implements Initializable {

    @FXML
    private ImageView hotel_main_pic;
    @FXML
    private Label hotel_name;
    @FXML
    private Label no_of_single;
    @FXML
    private Label no_of_double;
    @FXML
    private Label price_of_single;
    @FXML
    private Label tax;
    @FXML
    private Label total_price;
    @FXML
    private Label price_of_double;
    @FXML
    private JFXButton confirm_booking;
    @FXML
    private JFXButton cancel;
    private String hotel_name1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            PreparedStatement ps = ob.con.prepareStatement("select hotel_id,cost_of_single_room,cost_of_double_room from hotel where city=? and name=?");
            ps.setString(1, Trip_plan.city);

            StringTokenizer stt = new StringTokenizer(Trip_plan.hotel_selected, " ");
            String sfr = "Hotel_";
            while (stt.hasMoreTokens()) {
                String ghh = stt.nextToken();
                if (stt.hasMoreTokens()) {
                    sfr += ghh + "_";
                } else {
                    sfr += ghh;
                }
            }
            ps.setString(2, sfr);
            hotel_name1 = sfr;
            // System.out.println(Trip_plan.hotel_selected+" "+Trip_plan.city+" "+sfr);
            ResultSet rs = ps.executeQuery();
            String st;
            rs.absolute(1);
            st = rs.getString(1);
            int cs, cd;
            cs = rs.getInt(2);
            cd = rs.getInt(3);
            //    System.out.println(st);
            ps = ob.con.prepareStatement("select count(*) from hotel_images where hotel_id=?");
            ps.setString(1, st);
            rs = ps.executeQuery();
            rs.absolute(1);
            int no_im_hot = rs.getInt(1);
            ps = ob.con.prepareStatement("select Location from hotel_images where hotel_id=?");
            ps.setString(1, st);
            rs = ps.executeQuery();
            rs.absolute(1);
            File file = new File(rs.getString(1));
            Image it = new Image(file.toURI().toString());
            hotel_main_pic.setImage(it);
            hotel_name.setText(Trip_plan.hotel_selected);
            no_of_single.setText(Integer.toString(Trip_plan.nofr));
            no_of_double.setText(Integer.toString(Trip_plan.nodr));
            price_of_single.setText(Integer.toString(cs));
            price_of_double.setText(Integer.toString(cd));
            float pr = Integer.parseInt(no_of_single.getText()) * Integer.parseInt(price_of_single.getText()) + Integer.parseInt(no_of_double.getText()) * Integer.parseInt(price_of_double.getText());
            float taxx = (float) (14.5 * pr);
            taxx /= 100;
            tax.setText(Float.toString(taxx));
            pr += taxx;
            total_price.setText(Float.toString(pr));
            
        } catch (SQLException ex) {
            Logger.getLogger(Book_roomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void add_to_bookings(ActionEvent event) {
        try {
            PreparedStatement ps;
            String g = "";
            for (int i = 0; i < 5; i++) {
                Random rand = new Random();
                int x = 65 + rand.nextInt(26);
                char ch = (char) x;
                g += ch;
            }
            // System.out.println(g);
            ps = ob.con.prepareStatement("select booking_id from booking_details");
            ResultSet rs = ps.executeQuery();
            int x = 0;
            while (rs.next()) {
                x++;
            }
            g += Integer.toString(x);
            for (int i = 0; i < 5; i++) {
                Random rand = new Random();
                x = 65 + rand.nextInt(26);
                char ch = (char) x;
                g += ch;
            }
            ps = ob.con.prepareStatement("select hotel_id from hotel where city=? and name=?");
            ps.setString(1, Trip_plan.city);

            ps.setString(2, hotel_name1);
            rs = ps.executeQuery();
            rs.absolute(1);
            String g1 = rs.getString(1);
            ps = ob.con.prepareStatement("insert into booking_details values (?,?,?,?,?,?,?,?)");
            ps.setString(1, g);
            g = no_of_single.getText();
            ps.setString(2, g);
            g = no_of_double.getText();
            ps.setString(3, g);
            g = total_price.getText();
            ps.setString(4, g);
            g = Trip_plan.user_id;
            ps.setString(5, g);
            ps.setString(6, g1);
            g = Trip_plan.cid.getYear() + "-" + Trip_plan.cid.getMonthValue() + "-" + Trip_plan.cid.getDayOfMonth();
            ps.setString(7, g);
            g = Trip_plan.cod.getYear() + "-" + Trip_plan.cod.getMonthValue() + "-" + Trip_plan.cod.getDayOfMonth();
            ps.setString(8, g);
            ps.executeUpdate();
            Stage stg = (Stage) tax.getScene().getWindow();
            stg.close();
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Congrats");
            tray.setMessage("Successfully booked room!!");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
            String ysr = "11:00";
            ps = ob.con.prepareStatement("select event from day_plan where time=? and day=? and user_id_fk=?");
            ps.setString(1, ysr);
            ps.setString(2, Trip_plan.cid.toString());
            ps.setString(3, Trip_plan.user_id);
            rs = ps.executeQuery();
            ps = ob.con.prepareStatement("insert into day_plan values (?,?,?,?)");
            ps.setString(1, ysr);
            String eve = "Check in to " + hotel_name.getText();
            ps.setString(2, eve);
            ps.setString(3, Trip_plan.user_id);
            ps.setString(4, Trip_plan.cid.toString());
            ps.executeUpdate();
            ps = ob.con.prepareStatement("select event from day_plan where time=? and day=? and user_id_fk=?");
            ps.setString(1, ysr);
            ps.setString(2, Trip_plan.cid.toString());
            ps.setString(3, Trip_plan.user_id);
            rs = ps.executeQuery();
            ps = ob.con.prepareStatement("insert into day_plan values (?,?,?,?)");
            ps.setString(1, ysr);
            eve = "Check out from " + hotel_name.getText();
            ps.setString(2, eve);
            ps.setString(3, Trip_plan.user_id);
            ps.setString(4, Trip_plan.cod.toString());
            ps.executeUpdate();
            tray = new TrayNotification();
            tray.setTitle("Congrats");
            tray.setMessage("Successfully added to Day planner!!");
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
            Trip_plan.room_booked = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    void close(ActionEvent event) {
        Stage stg = (Stage) tax.getScene().getWindow();
        stg.close();
        Trip_plan.room_booked = false;

    }

}
