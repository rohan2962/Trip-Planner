/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip_plan;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 *
 * @author RAGHAV SABOO
 */
public class My_sql_conn {
 Connection con;   
 public My_sql_conn()
 {
     try{  
Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/trip_planner","root","2015042");
}catch(Exception e){ System.out.println(e);}  
}  
}  