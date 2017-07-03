/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Select_cityController implements Initializable {

    @FXML
    private ComboBox<String> combo_city;
    ObservableList<String> options
            = FXCollections.observableArrayList(
                    "Goa",
                    "Delhi",
                    "Mumbai",
                    "Agra",
                    "Jaipur",
                    "Bangalore"
            );
    @FXML
    private JFXButton Sumbit;
    @FXML
    private DatePicker from_date;
    @FXML
    private DatePicker to_date;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        combo_city.setItems(options);
        from_date.getChronology().dateNow();
        from_date.setValue(LocalDate.now());
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
                                        LocalDate.now())) {
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
                                if (!(item.isAfter(
                                        from_date.getValue()))) {
                                    //  System.out.println("hey");
                                    setDisable(true);
                                    setStyle("-fx-background-color: #2196f3;");
                                }
                            }
                        };
                    }
                };
        to_date.setDayCellFactory(dayCelFactory);

        Sumbit.setDisable(true);

    }

    @FXML
    private void combo_city_func(ActionEvent event)  {
        try {
            Trip_plan.city = combo_city.getValue();
            // System.out.println(Trip_plan.city);
            Trip_plan.city_start=from_date.getValue();
            Trip_plan.city_end=to_date.getValue();
            PreparedStatement ps=ob.con.prepareStatement("insert into day_plan values(?,?,?,?)");
            ps.setString(1,"09:00");
            String str="Go to city "+Trip_plan.city;
            ps.setString(2,str);
            ps.setString(3,Trip_plan.user_id);
            ps.setString(4,Trip_plan.city_start.toString());
            ps.executeUpdate();
            ps=ob.con.prepareStatement("insert into day_plan values(?,?,?,?)");
            ps.setString(1,"09:00");
            str="Return from city "+Trip_plan.city;
            ps.setString(2,str);
            ps.setString(3,Trip_plan.user_id);
            ps.setString(4,Trip_plan.city_end.toString());
            ps.executeUpdate();
            BorderPane parent = (BorderPane) combo_city.getParent().getParent();
            parent.setCenter(FXMLLoader.load(getClass().getResource("Inside_city.fxml")));
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(Select_cityController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void enable_check(ActionEvent event) {
        if (from_date.getValue() != null && to_date.getValue() != null) {
            Sumbit.setDisable(false);
        }
    }

    @FXML
    void enable_check1(ActionEvent event) {
        if (combo_city.getValue() != null && to_date.getValue() != null) {
            Sumbit.setDisable(false);
        }
    }

    @FXML
    void enable_check2(ActionEvent event) {
        if (from_date.getValue() != null && combo_city.getValue() != null) {
            Sumbit.setDisable(false);
        }
    }
}
