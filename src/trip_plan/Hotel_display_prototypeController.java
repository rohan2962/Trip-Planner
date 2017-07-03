/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTabPane;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static trip_plan.Trip_plan.cnt_hotel;
import static trip_plan.Trip_plan.hotel_id;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Hotel_display_prototypeController implements Initializable {

    @FXML
    private ImageView hotel_main_pic;
    @FXML
    private Label hotel_name;
    @FXML
    private Label main_price;
    @FXML
    private Label five_star;
    @FXML
    private Label four_star;
    @FXML
    private Label three_star;
    @FXML
    private Label two_star;
    @FXML
    private Label two_bed_price;
    @FXML
    private JFXButton book_hotel;
    @FXML
    private Label one_bed_price;
    @FXML
    public Label Not_avai;
    AnchorPane hotel_pane;
    ObservableList<String> options
            = FXCollections.observableArrayList();
    int selected;
    File file;
    Image it;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        two_bed_price.setText("Rs. " + Integer.toString(Trip_plan.cost_dr));
        main_price.setText("Rs. " + Integer.toString(Trip_plan.cost_dr));
        one_bed_price.setText("Rs. " + Integer.toString(Trip_plan.cost_sr));
        hotel_name.setText(Trip_plan.hot_nam);
        if(Trip_plan.hotel_available==false)
        {
            Not_avai.setVisible(true);
            book_hotel.setDisable(true);
        }
        
        int x = Trip_plan.type.charAt(0) - 48;
        //System.out.println(x);
        if (x == 2) {
            two_star.setVisible(true);
        }
        if (x == 3) {
            three_star.setVisible(true);
        }
        if (x == 4) {
            four_star.setVisible(true);
        }
        if (x == 5) {
            five_star.setVisible(true);
        }
        try {
            PreparedStatement ps = ob.con.prepareStatement("select count(*) from hotel_images where hotel_id=?");
            ps.setString(1, hotel_id);
            ResultSet rs = ps.executeQuery();
            rs.absolute(1);
            int no_im_hot = rs.getInt(1);
            ps = ob.con.prepareStatement("select Location from hotel_images where hotel_id=?");
            ps.setString(1, hotel_id);
            rs = ps.executeQuery();
            rs.absolute(1);
            file = new File(rs.getString(1));
            it = new Image(file.toURI().toString());
            hotel_main_pic.setImage(it);
            AnchorPane impane = new AnchorPane();
            ImageView im = new ImageView();
            im.setFitHeight(330);
            im.setFitWidth(460);
            JFXButton left = new JFXButton("<");
            JFXButton right = new JFXButton(">");
            impane.getChildren().add(im);
            impane.getChildren().add(left);
            impane.getChildren().add(right);
            impane.setMaxSize(460, 330);
            AnchorPane.setTopAnchor(im, 0.0);
            AnchorPane.setLeftAnchor(im, 0.0);
            AnchorPane.setTopAnchor(left, 165.0);
            AnchorPane.setLeftAnchor(left, 10.0);
            AnchorPane.setTopAnchor(right, 165.0);
            AnchorPane.setRightAnchor(right, 10.0);
            for (int i = 0; i < no_im_hot; i++) {
                rs.absolute(i + 1);
                options.add(rs.getString(1));
            }
            selected = 0;
            file = new File(options.get(selected));
            it = new Image(file.toURI().toString());
            im.setImage(it);
            left.setStyle("-fx-background-color:white");
            left.setOpacity(0.75);
            right.setStyle("-fx-background-color:white");
            right.setOpacity(0.75);
            left.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (selected > 0) {
                        selected--;
                        file = new File(options.get(selected));
                        it = new Image(file.toURI().toString());
                        im.setImage(it);
                    }
                }
            });
            right.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (selected < options.size()-1) {
                        selected++;
                        file = new File(options.get(selected));
                        it = new Image(file.toURI().toString());
                        im.setImage(it);
                    }
                }
            });
            AnchorPane parent = (AnchorPane) one_bed_price.getParent();
            parent.getChildren().add(impane);

        } catch (SQLException ex) {
            Logger.getLogger(Hotel_display_prototypeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void open_tab(MouseEvent event) {
        Accordion ac = (Accordion) hotel_main_pic.getParent().getParent().getParent().getParent();
        TitledPane t1 = (TitledPane) hotel_main_pic.getParent().getParent().getParent();
        for (int i = 0; i < ac.getChildrenUnmodifiable().size(); i++) {
            Node e = (Node) ac.getChildrenUnmodifiable().get(i);
            TitledPane t = (TitledPane) e;
            if (t.isExpanded()) {
                t.setExpanded(false);
            }

        }
        t1.setExpanded(true);
    }

    @FXML
    void book_room(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Trip_plan.hotel_selected = hotel_name.getText();
        
        Parent root = FXMLLoader.load(getClass().getResource("book_room.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("trip-planner-footer-logo.png")));
        stage.setTitle("Trip Planner");
        // stage.initStyle(StageStyle.UNDECORATED);

        stage.showAndWait();
       if(Trip_plan.room_booked){
                JFXTabPane tabpane = find(one_bed_price);
            Tab hotel_tab = tabpane.getTabs().get(1);
             hotel_pane = new AnchorPane();
                    hotel_pane=FXMLLoader.load(getClass().getResource("hotel_details.fxml"));
                    hotel_tab.setContent(hotel_pane);
               BorderPane bp = find1(tabpane);
               JFXDrawer dr=(JFXDrawer)bp.getRight();
               ScrollPane sidePane = FXMLLoader.load(getClass().getResource("drawer.fxml"));
            dr.setSidePane(sidePane);
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
    private BorderPane find1(Node node) {
        BorderPane tp = null;
        for (Node n = node.getParent(); n != null; n = n.getParent()) {
            if (n instanceof BorderPane) {
                tp = (BorderPane) n;
            }
        }
        return tp;
    }

}
