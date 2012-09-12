package org.robby;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	String username;
	String password;
	String status;
	
	UserAction(){
		username = new String();
		password = new String();
		status = new String();
	}
	
	public String register() throws Exception{
		return SUCCESS;
	}
}
