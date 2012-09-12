package org.robby;


import java.io.IOException;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;


public class HbaseIf {
	Configuration conf;
	
	HbaseIf(){
		conf = HBaseConfiguration.create();
	}
	
	public long getNewUserId() throws IOException{
		HTable table =new HTable(conf, "tab_global");
		long n=table.incrementColumnValue(Bytes.toBytes("row1"), Bytes.toBytes("param"), Bytes.toBytes("userid"), 1);
		table.close();
		return n;
	}
	
	public boolean create_new_user(String name, String password) throws IOException{
		HTable tab_global = new HTable(conf, "tab_global");
		HTable tab_users = new HTable(conf, "tab_users");

		if(tab_users.exists(new Get(name.getBytes())))
			return false;
		
		long id=tab_global.incrementColumnValue(Bytes.toBytes("row1"), Bytes.toBytes("param"), Bytes.toBytes("userid"), 1);
		
		Put put =new Put(name.getBytes());
		put.add(Bytes.toBytes("id"), Bytes.toBytes("id"), Bytes.toBytes(id));
		tab_users.put(put);
		
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HbaseIf h=new HbaseIf();
		h.create_new_user("robby2", "robby");
	}

}
