package org.robby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

/*
 * tab_global  param:userid
 * 
 * tab_user2id info:id
 * 
 * tab_id2user info:username, info:password
 * 
 * */

public class HbaseIf {
	Configuration conf;
	public static HbaseIf hbase = null;
	
	public static HbaseIf getInstance(){
		if(hbase == null)
			hbase = new HbaseIf();
		
		return hbase;
	}
	
	public HbaseIf() {
		conf = HBaseConfiguration.create();
	}

	public void create_table(String name, String col, int version)
			throws Exception {
		HBaseAdmin admin = new HBaseAdmin(conf);

		if (admin.tableExists(name)) {
			admin.disableTable(name);
			admin.deleteTable(name);
		}

		HTableDescriptor tableDesc = new HTableDescriptor(name);
		HColumnDescriptor hd = new HColumnDescriptor(col);
		hd.setMaxVersions(version);
		tableDesc.addFamily(hd);
		admin.createTable(tableDesc);
	}

	public void createTables() throws Exception {
		// create tag_global and initialization
		create_table("tab_global", "param", 1);

		Put put = new Put(Bytes.toBytes("row_userid"));
		long id = 0;
		put.add(Bytes.toBytes("param"), Bytes.toBytes("userid"),
				Bytes.toBytes(id));
		HTable ht = new HTable(conf, "tab_global");
		ht.put(put);

		// create tab_user2id
		create_table("tab_user2id", "info", 1);

		// create tab_id2user
		create_table("tab_id2user", "info", 1);
	}

	public boolean deleteUser(long id) throws Exception{
		String username = getNameById(id);
		if(username.equals(""))
			return false;
		
		HTable tab_user2id = new HTable(conf, "tab_user2id");
		HTable tab_id2user = new HTable(conf, "tab_id2user");
		
		Delete del = new Delete(username.getBytes());
		tab_user2id.delete(del);
		
		del = new Delete(Bytes.toBytes(id));
		tab_id2user.delete(del);
		return true;
	}

	public boolean createNewUser(String name, String password)
			throws IOException {
		HTable tab_global = new HTable(conf, "tab_global");
		HTable tab_user2id = new HTable(conf, "tab_user2id");
		HTable tab_id2user = new HTable(conf, "tab_id2user");
		
		if (tab_user2id.exists(new Get(name.getBytes())))
			return false;

		long id = tab_global.incrementColumnValue(Bytes.toBytes("row_userid"),
				Bytes.toBytes("param"), Bytes.toBytes("userid"), 1);

		// insert record in tab_user2id
		Put put = new Put(name.getBytes());
		put.add(Bytes.toBytes("info"), Bytes.toBytes("id"), Bytes.toBytes(id));
		tab_user2id.put(put);

		// insert record in tab_id2user
		put = new Put(Bytes.toBytes(id));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("username"),
				Bytes.toBytes(name));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("password"),
				Bytes.toBytes(password));
		tab_id2user.put(put);

		return true;
	}

	public String getNameById(long id) {
		try {
			HTable tab_id2user = new HTable(conf, "tab_id2user");
			Result rs = tab_id2user.get(new Get(Bytes.toBytes(id)));
			KeyValue kv = rs.getColumnLatest(Bytes.toBytes("info"),
					Bytes.toBytes("username"));
			return Bytes.toString(kv.getValue());
		} catch (Exception e) {
			return "";
		}
	}

	public long getIdByUsername(String username) {
		try {
			HTable tab_user2id = new HTable(conf, "tab_user2id");
			Result rs = searchByRowKey(tab_user2id, username);

			KeyValue kv = rs.getColumnLatest(Bytes.toBytes("info"),
					Bytes.toBytes("id"));
			byte[] bid = kv.getValue();
			return Bytes.toLong(bid);
		} catch (Exception e) {
			return 0;
		}
	}

	// return 0:not matched >0:match
	public long checkPassword(String name, String password) throws Exception {
		HTable tab_user2id = new HTable(conf, "tab_user2id");
		HTable tab_id2user = new HTable(conf, "tab_id2user");
		if (!tab_user2id.exists(new Get(name.getBytes())))
			return 0;

		Result rs = searchByRowKey(tab_user2id, name);
		KeyValue kv = rs.getColumnLatest(Bytes.toBytes("info"),
				Bytes.toBytes("id"));
		byte[] bid = kv.getValue();

		Get get = new Get(bid);
		rs = tab_id2user.get(get);
		kv = rs.getColumnLatest(Bytes.toBytes("info"),
				Bytes.toBytes("password"));
		String passwordInDb = Bytes.toString(kv.getValue());

		// System.out.println(passwordInDb);
		if (!password.equals(passwordInDb))
			return 0;

		long id = Bytes.toLong(bid);
		return id;
	}

	public Result searchByRowKey(HTable ht, String rk) throws Exception {
		Get get = new Get(rk.getBytes());
		Result rs = ht.get(get);
		return rs;
	}

	public static void main11(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HbaseIf h = new HbaseIf();
		/*
		 * h.createTables(); if(h.createNewUser("robby1", "robby"))
		 * System.out.println("add user success"); else
		 * System.out.println("add user failed");
		 */
		long id = 0;
		h.checkPassword("user1", "pwd1");
	}

}
