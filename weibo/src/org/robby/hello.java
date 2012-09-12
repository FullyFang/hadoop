package org.robby;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;

import com.opensymphony.xwork2.ActionSupport;

public class hello extends ActionSupport {
	String str;
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String execute() throws Exception {
		Configuration conf=HBaseConfiguration.create();
		HTable table = new HTable(conf, "tab_global");
		String rowKey = "id";
		Get g = new Get(rowKey.getBytes());
	    Result rs = table.get(g);
	    
	    String a = "param";
	    String b = "id";
	    KeyValue kv = rs.getColumnLatest(a.getBytes(), b.getBytes());
	    
		
		str = new String(kv.getValue());
		return SUCCESS;
	}
}
