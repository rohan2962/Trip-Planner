/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import static trip_plan.Trip_plan.cnt_hotel;
import static trip_plan.Trip_plan.cost_dr;
import static trip_plan.Trip_plan.cost_sr;
import static trip_plan.Trip_plan.hot_nam;
import static trip_plan.Trip_plan.hotel_id;
import static trip_plan.Trip_plan.ob;
import static trip_plan.Trip_plan.type;

/**
 * FXML Controller class
 *
 * @author rohan
 */
public class Hotel_detailsController implements Initializable {

    @FXML
    private DatePicker from_date;
    @FXML
    private DatePicker to_date;
    @FXML
    private JFXComboBox<String> single_count;
    @FXML
    private JFXComboBox<String> double_count;
    @FXML
    private JFXButton search;
    @FXML
    private Label Error_label;
    AnchorPane hotel_pane;
    ScrollPane scroll;
    ObservableList<String> options
            = FXCollections.observableArrayList(
                    "0", "1", "2", "3", "4"
            );

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        single_count.setItems(options);
        single_count.setValue(options.get(0));
        double_count.setItems(options);
        double_count.setValue(options.get(0));
        from_date.getChronology().dateNow();
        from_date.setValue(Trip_plan.city_start);
        //System.out.println(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(
                                        Trip_plan.city_start)) {
                                    //  System.out.println("hey");
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        from_date.setDayCellFactory(dayCellFactory);
        final Callback<DatePicker, DateCell> dayCelFactory
                = new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isAfter(
                                        Trip_plan.city_end)||(item.isBefore(from_date.getValue()))) {
                                    //  System.out.println("hey");
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        to_date.setDayCellFactory(dayCelFactory);
    }

    @FXML
    private void search_for_rooms(ActionEvent event) throws IOException {
        try {
            String tpp;
            if (from_date.getValue().getMonthValue() < 10 && from_date.getValue().getDayOfMonth() < 10) {
                tpp = from_date.getValue().getYear() + "-0" + from_date.getValue().getMonthValue() + "-0" + from_date.getValue().getDayOfMonth();
            } else if (from_date.getValue().getMonthValue() < 10) {
                tpp = from_date.getValue().getYear() + "-0" + from_date.getValue().getMonthValue() + "-" + from_date.getValue().getDayOfMonth();

            } else if (from_date.getValue().getDayOfMonth() < 10) {
                tpp = from_date.getValue().getYear() + "-" + from_date.getValue().getMonthValue() + "-0" + from_date.getValue().getDayOfMonth();

            } else {
                tpp = from_date.getValue().getYear() + "-" + from_date.getValue().getMonthValue() + "-" + from_date.getValue().getDayOfMonth();
            }
           // System.out.println("hey  " + tpp);
            // date.plusDays(1L);
            //System.out.println(tpp+" "+date);
            if (single_count.getSelectionModel().isEmpty() || double_count.getSelectionModel().isEmpty()) {
                //  System.out.println("Hello");
                Error_label.setText("Please select rooms!!");
                return;
            } else if (single_count.getValue().compareTo("0") == 0 && double_count.getValue().compareTo("0") == 0) {
                Error_label.setText("Please select atleast one room!!");
                return;
            }
            if (to_date.getValue() == null) {
                Error_label.setText("Please select check out date!!");
                return;
            }
            Trip_plan.cid = from_date.getValue();
            Trip_plan.cod = to_date.getValue();
            Trip_plan.nofr = Integer.parseInt(single_count.getValue());
            Trip_plan.nodr = Integer.parseInt(double_count.getValue());
            scroll = new ScrollPane();
            hotel_pane = new AnchorPane();
            scroll.setContent(hotel_pane);
            JFXTabPane tabpane = find(single_count);
            Tab hotel_tab = tabpane.getTabs().get(1);
            hotel_tab.setContent(scroll);
            Accordion accordion = new Accordion();
            accordion.setMinWidth(900);
            hotel_pane.getChildren().add(accordion);
            PreparedStatement ps = ob.con.prepareStatement("select count(*) from hotel where city=?");
            ps.setString(1, Trip_plan.city);
            ResultSet rs = ps.executeQuery();
            rs.absolute(1);
            cnt_hotel = rs.getInt(1);
            ps = ob.con.prepareStatement("select cost_of_single_room,cost_of_double_room,type,name,hotel_id from hotel where city=? order by type desc ");
            ps.setString(1, Trip_plan.city);
            rs = ps.executeQuery();
            TitledPane[] tp = new TitledPane[cnt_hotel];
            //  System.out.println(cnt_hotel);
            int al[]={31,29,31,30,31,30,31,31,30,31,30,31};
            for (int i = 0; i < cnt_hotel; i++) {
                rs.absolute(i + 1);
                String hid = rs.getString(5);
                int flag = 0;
                long date1 = from_date.getValue().toEpochDay();
                long date2 = to_date.getValue().toEpochDay();
                int days = (int) Math.abs(date1 - date2);
                LocalDate date = LocalDate.parse(tpp);
                int year=date.getYear(),month=date.getMonthValue(),day=date.getDayOfMonth();
                //System.out.println(days);
                for (int j = 0; j <= days; j++) {
                    int nsr,ndr;
                    ps=ob.con.prepareStatement("select no_of_single_rooms_available,no_of_double_rooms_available from hotel where hotel_id=?");
                    ps.setString(1,hid);
                    ResultSet prs=ps.executeQuery();
                    prs.absolute(1);
                    nsr=prs.getInt(1);
                    ndr=prs.getInt(2);
                    ps = ob.con.prepareStatement("select sum(No_of_single_rooms),sum(No_of_double_rooms) from booking_details where hotel_id=? and ? between Check_in_date and Check_out_date");
                    ps.setString(1, hid);
                    String did;
                    did=Integer.toString(year);
                    if(month<10)
                    {
                        did+="-0"+Integer.toString(month);
                    }
                    else
                    {
                        did+="-"+Integer.toString(month);
                    }
                    if(day<10)
                    {
                        did+="-0"+Integer.toString(day);
                    }
                    else
                    {
                        did+="-"+Integer.toString(day);
                    }
                    if(al[month-1]==day)
                    {
                        day=1;
                        month++;
                    }
                    else
                    {
                        day++;
                    }
                    ps.setString(2, did);
                    ResultSet rt = ps.executeQuery();
                    if(rt.next()==false)
                    {
                        continue;
                    }
                    rt.absolute(1);
                   // System.out.println(nsr+" "+ndr+" "+did+" "+rt.getInt(2)+" "+double_count.getValue());
                    if (nsr-rt.getInt(1) < Integer.parseInt(single_count.getValue()) || ndr-rt.getInt(2) < Integer.parseInt(double_count.getValue())) {
                        flag = 1;
                       // System.out.println(flag+" "+hid);
                    }
                    
                }
                //System.out.println(flag);
                    cost_sr = rs.getInt(1);
                    cost_dr = rs.getInt(2);
                    hot_nam = rs.getString(4);
                    StringTokenizer stz = new StringTokenizer(hot_nam, "_");
                    hot_nam = stz.nextToken();
                    hot_nam = "";
                    while (stz.hasMoreTokens()) {
                        hot_nam += stz.nextToken() + " ";
                    }
                    // System.out.println(hot_nam);
                    type = rs.getString(3);
                    hotel_id = rs.getString(5);
                    //System.out.println(cost_sr + " " + cost_dr + " " + hot_nam + " " + type);
                    tp[i] = new TitledPane();
                    if(flag==0)
                    {
                    Trip_plan.hotel_available=true;
                }
                else
                {
                    Trip_plan.hotel_available=false;
                }
                    tp[i] = FXMLLoader.load(getClass().getResource("hotel_display_prototype.fxml"));
                    
            }
            accordion.getPanes().addAll(tp);
            accordion.setExpandedPane(tp[0]);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Inside_cityController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private JFXTabPane find(Node node) {
        JFXTabPane tp = null;
        for (Node n = node.getParent(); n != null; n = n.getParent()) {
            if (n instanceof JFXTabPane) {
                tp = (JFXTabPane) n;
            }
        }
        return tp;
    }

}
