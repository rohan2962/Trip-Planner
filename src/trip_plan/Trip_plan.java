/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author RAGHAV SABOO
 */
public class Trip_plan extends Application {
    public static int nofr;
    public static int nodr;
    public static My_sql_conn ob = new My_sql_conn();
    public static String user_name= new String(); 
    public static String city;
    public static int cost_sr;
    public static int cost_dr;
    public static String type;
    public static String hot_nam;
    public static String hotel_id;
    public static int cnt_hotel;
    public static boolean hotel_available;
    public static int cnt_spots;
    public static String spot_type;
    public static String spot_desc;
    public static String spot_name;
    public static String spot_id;
    public static String user_id;
    public static String hotel_selected;
    public static LocalDate cid;
    public static LocalDate cod;
    public static LocalDate city_start;
    public static LocalDate city_end;
    public static String spot_selected;
    public static boolean room_booked=true;
    public static boolean spot_booked=true;
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("start_page.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("trip-planner-footer-logo.png")));
        stage.setTitle("Trip Planner");
      //  stage.setResizable(false);
       stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
