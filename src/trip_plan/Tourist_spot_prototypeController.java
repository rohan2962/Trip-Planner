/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTabPane;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static trip_plan.Trip_plan.hotel_id;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class Tourist_spot_prototypeController implements Initializable {

    @FXML
    private Label spot_name;
    @FXML
    private Label spot_type;
    @FXML
    private ImageView spot_main_pic;
    @FXML
    private TextArea spot_desc;
    @FXML
    private JFXButton add_to_planner;
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
        spot_desc.setText(Trip_plan.spot_desc);
        StringTokenizer st = new StringTokenizer(Trip_plan.spot_name, "_");
        String g = "";
        while (st.hasMoreTokens()) {
            g += st.nextToken();
            g += " ";
        }
        spot_name.setText(g);
        spot_type.setText(Trip_plan.spot_type);
        spot_desc.setWrapText(true);
        try {
            PreparedStatement ps = ob.con.prepareStatement("select count(*) from tourist_spot_images where tsp_id=?");
            // System.out.println(Trip_plan.spot_id);
            ps.setString(1, Trip_plan.spot_id);
            ResultSet rs = ps.executeQuery();
            rs.absolute(1);
            int no_im_hot = rs.getInt(1);
            //System.out.println(no_im_hot);
            ps = ob.con.prepareStatement("select Location from tourist_spot_images where tsp_id=?");
            ps.setString(1, Trip_plan.spot_id);
            rs = ps.executeQuery();
            rs.absolute(1);
            file = new File(rs.getString(1));
            //System.out.println(rs.getString(1));
            it = new Image(file.toURI().toString());
            spot_main_pic.setImage(it);
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
            //  System.out.println(no_im_hot);
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
            AnchorPane parent = (AnchorPane) spot_desc.getParent();
            parent.getChildren().add(impane);
        } catch (SQLException ex) {
            Logger.getLogger(Hotel_display_prototypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void open_tab(MouseEvent event) {
        Accordion ac = (Accordion) spot_main_pic.getParent().getParent().getParent().getParent();
        TitledPane t1 = (TitledPane) spot_main_pic.getParent().getParent().getParent();
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
    void add_to_plan(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Trip_plan.spot_selected = spot_name.getText();
        Parent root = FXMLLoader.load(getClass().getResource("tourist_plan.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("trip-planner-footer-logo.png")));
        stage.setTitle("Trip Planner");
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        if(Trip_plan.spot_booked==true)
        {
            BorderPane bp = find1(spot_desc);
               JFXDrawer dr=(JFXDrawer)bp.getRight();
               ScrollPane sidePane = FXMLLoader.load(getClass().getResource("drawer.fxml"));
            dr.setSidePane(sidePane);
        }
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
