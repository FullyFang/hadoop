package org.robby.hive;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*/
 * hive --service hiveserver
 * 
 * drop table tab_indb_tmp;
 * create table tab_indb_tmp(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
 * 
 * drop table tab_indb_cdr;
 * create table tab_indb_cdr(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) partitioned by(time string) STORED AS SEQUENCEFILE;
 * 
 * 
 * from tab_cdr insert into table tab_indb_cdr partition (time="1") select * where substr(ts, 7, 2) <= "10" insert into table tab_indb_cdr partition (time="2") select * where substr(ts, 7, 2) > "10";
 */


public class HiveIndb {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
		Connection con = DriverManager.getConnection("jdbc:hive://localhost:10000/default", "", "");
		Statement stmt = con.createStatement();
	    
		String inpath = "/home/robby/indb/";
		File path = new File(inpath);
		while(true){
			String[] list = path.list();
			if(list.length == 0){
				System.out.println("no file, sleep!");
				Thread.sleep(2000);
			}
			for(String f:list){
				long time1 = System.currentTimeMillis();
				String fullfile = inpath + f;
				String sql = "load data local inpath '" + fullfile + "' overwrite into table tab_indb_tmp";
				System.out.println(sql);
				ResultSet res = stmt.executeQuery(sql);
				
				/*
				sql = "insert into table tab_indb_cdr partition (time=\"1st\") select * from tab_indb_tmp where substr(ts,7,2)<=\"10\"";
				res = stmt.executeQuery(sql);
				
				sql = "insert into table tab_indb_cdr partition (time=\"2ed\") select * from tab_indb_tmp where substr(ts,7,2)>\"10\" and substr(ts,7,2)<=\"20\" ";
				res = stmt.executeQuery(sql);
				
				sql = "insert into table tab_indb_cdr partition (time=\"3th\") select * from tab_indb_tmp where substr(ts,7,2)>\"20\"";
				res = stmt.executeQuery(sql);
				*/
				
				/*
				from tab_indb_tmp insert into table tab_indb_cdr partition(time="1st") select * where substr(ts,7,2)<="10"
						          insert into table tab_indb_cdr partition(time="2ed") select * where substr(ts,7,2)>"10" and substr(ts,7,2)<="20"
						          insert into table tab_indb_cdr partition(time="3th") select * where substr(ts,7,2)>"20";
						       */
				
				sql = "from tab_indb_tmp insert into table tab_indb_cdr partition(time=\"1st\") select * where substr(ts,7,2)<=\"10\" " + 
				          "insert into table tab_indb_cdr partition(time=\"2ed\") select * where substr(ts,7,2)>\"10\" and substr(ts,7,2)<=\"20\" " +
				          "insert into table tab_indb_cdr partition(time=\"3th\") select * where substr(ts,7,2)>\"20\"";
				res = stmt.executeQuery(sql);
				
				
				File file = new File(fullfile);
				file.delete();
				long time2 = System.currentTimeMillis();
				
				System.out.println("time:" + (time2 - time1)/1000); 
			}
		}
		
		
	    //ResultSet res = stmt.executeQuery(sql);
	    
	}

}
