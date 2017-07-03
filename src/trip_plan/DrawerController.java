/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import static trip_plan.Trip_plan.ob;

/**
 * FXML Controller class
 *
 * @author RAGHAV SABOO
 */
public class DrawerController implements Initializable {
    @FXML
    private Accordion day_plan_list;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            if(Trip_plan.city_start!=null){
            PreparedStatement ps=ob.con.prepareStatement("select time,event,user_id_fk,day from day_plan where user_id_fk=?");
            ps.setString(1,Trip_plan.user_id);
            ResultSet rs=ps.executeQuery();
            String time,event,user_id,day;
            long c1,c2;
            c1=Trip_plan.city_start.toEpochDay();
            c2=Trip_plan.city_end.toEpochDay();
            long ans=c2-c1;
            //System.out.println(ans);
            TitledPane tp[]=new TitledPane[(int)ans+1];
            ObservableList<String> options;
            ListView lv;
            day_plan_list.setStyle("-fx-backgrond-color:white");
            day_plan_list.setVisible(true);
            for(int i=0;i<=ans;i++)
            {
                tp[i]=new TitledPane();
                tp[i].setText(Trip_plan.city_start.plusDays(i).toString());
                tp[i].setContent(new ListView<String>());
                tp[i].setStyle("-fx-backgrond-color:white");
            }
            while(rs.next())
            {
                time=rs.getString(1);
                event=rs.getString(2);
                user_id=rs.getString(3);
                day=rs.getString(4);
                LocalDate dl=LocalDate.parse(day);
                c2=dl.toEpochDay();
                ans=c2-c1;
                //System.out.println(ans);
                lv=(ListView)tp[(int)ans].getContent();
                options=lv.getItems();
                if(options==null)
                {
                    options = FXCollections.observableArrayList();
                }
                options.add(time+" "+event);
                options.sort(new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        int x1=0,x2=0;
                        for(int i=0;i<o1.length();i++)
                        {
                            if(o1.charAt(i)>=48&&o1.charAt(i)<=57)
                            {
                                x1*=10;
                                x1+=o1.charAt(i)-'0';
                            }
                        }
                        for(int i=0;i<o2.length();i++)
                        {
                            if(o2.charAt(i)>=48&&o2.charAt(i)<=57)
                            {
                                x1*=10;
                                x1+=o2.charAt(i)-'0';
                            }
                        }
                        if(x1<x2)
                        {
                            return 1;
                        }
                        else if(x1>x2)
                        {
                            return -1;
                        }
                        else
                        {
                            return 0;
                        }
                    }
                });
                lv.setItems(options);
            }
            day_plan_list.getPanes().addAll(tp);
            }
            //System.out.println(Trip_plan.city_start);
        } catch (SQLException ex) {
            Logger.getLogger(DrawerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
