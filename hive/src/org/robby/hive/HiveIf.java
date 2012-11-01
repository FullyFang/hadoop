package org.robby.hive;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;


public class HiveIf {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
		Connection con = DriverManager.getConnection("jdbc:hive://localhost:10000/default", "", "");
		Statement stmt = con.createStatement();
	    
	    //
	    ResultSet res = stmt.executeQuery("select * from tab_cdr limit 10");
	    while (res.next()) {
	    	  System.out.println(res.getString(1) + "\t" +res.getString(2) + "\t" +res.getString(3) + "\t" +res.getString(4) + "\t" +res.getString(5) + "\t" +res.getString(6));
	    }
	}
}
