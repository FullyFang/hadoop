package org.robby;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Vector;

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
 * tab_users user:follow user:followd user:inbox user:sent
 * 
 * tab_post post:content
 * 
 * */

public class HbaseIf {
	Configuration conf;
	public static HbaseIf ghbase = null;
	
	public static HbaseIf getInstance(){
		if(ghbase == null)
			ghbase = new HbaseIf();
		return ghbase;
	}

	HbaseIf() {
		
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
		admin.close();
	}
	
	
	public List<Post> getPost(String username) throws Exception{
		List<Post> list = new ArrayList<Post>();
		long id = this.getIdByUsername(username);
		
		byte[] begin = Bytes.add(Bytes.toBytes(id), Bytes.toBytes(Long.MAX_VALUE-Long.MAX_VALUE));
		byte[] end = Bytes.add(Bytes.toBytes(id), Bytes.toBytes(Long.MAX_VALUE));
		
		Scan s = new Scan();
		s.setStartRow(begin);
		s.setStopRow(end);
		
		HTable tab_post = new HTable(conf, "tab_post");
		HTable tab_inbox = new HTable(conf, "tab_inbox");
		ResultScanner ss = tab_inbox.getScanner(s);
		Get get = null;
		Post p = null;
		
		for (Result r : ss) {
			byte[] postid = r.getValue(Bytes.toBytes("postid"), null);
			
			get = new Get(postid);
			Result rs = tab_post.get(get);
			String post_username = Bytes.toString(rs.getValue(Bytes.toBytes("post"), Bytes.toBytes("username")));
			String post_content =  Bytes.toString(rs.getValue(Bytes.toBytes("post"), Bytes.toBytes("content")));
			String post_ts =       Bytes.toString(rs.getValue(Bytes.toBytes("post"), Bytes.toBytes("ts")));
			p = new Post(post_username, post_content, post_ts);
			list.add(0,p);
		}
		
		return list;
	}
	
	public boolean post(String username, String content)
			throws Exception {
		HTable tab_global = new HTable(conf, "tab_global");
		HTable tab_post = new HTable(conf, "tab_post");
		

		long id = tab_global.incrementColumnValue(Bytes.toBytes("row_postid"),
				Bytes.toBytes("param"), Bytes.toBytes("postid"), 1);

		byte[] postid = Bytes.toBytes(id);
		
		// insert record in tab_post
		Put put = new Put(postid);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ts = dateFormat.format(new Date());
		
		put.add(Bytes.toBytes("post"), Bytes.toBytes("username"), username.getBytes());
		put.add(Bytes.toBytes("post"), Bytes.toBytes("content"), content.getBytes());
		put.add(Bytes.toBytes("post"), Bytes.toBytes("ts"), ts.getBytes());
		tab_post.put(put);

		tab_global.close();
		tab_post.close();
		
		// send the post
		long senderid = this.getIdByUsername(username);
		System.out.println("sender id:" + senderid);
		
		
		byte[] begin = Bytes.add(Bytes.toBytes(senderid), Bytes.toBytes(Long.MAX_VALUE-Long.MAX_VALUE));
		byte[] end = Bytes.add(Bytes.toBytes(senderid), Bytes.toBytes(Long.MAX_VALUE));
		
		
		Scan s = new Scan();
		s.setStartRow(begin);
		s.setStopRow(end);
		
		
		HTable tab_followed = new HTable(conf, "tab_followed");
		HTable tab_inbox = new HTable(conf, "tab_inbox");
		ResultScanner ss = tab_followed.getScanner(s);
		
		put = new Put(Bytes.add(Bytes.toBytes(senderid), postid));
		put.add(Bytes.toBytes("postid"), null, postid);
		tab_inbox.put(put);
		
		for (Result r : ss) {
			byte[] did = r.getValue(Bytes.toBytes("userid"), null);
			
			put = new Put(Bytes.add(did, postid));
			put.add(Bytes.toBytes("postid"), null, postid);
			tab_inbox.put(put);
		}
		
		tab_followed.close();
		tab_inbox.close();
		
		return true;
	}

	public void createTables() throws Exception {
		// create tag_global and initialization
		create_table("tab_global", "param", 1);

		HTable ht = new HTable(conf, "tab_global");
		Put put = new Put(Bytes.toBytes("row_userid"));
		long id = 0;
		put.add(Bytes.toBytes("param"), Bytes.toBytes("userid"),
				Bytes.toBytes(id));
		ht.put(put);

		put = new Put(Bytes.toBytes("row_postid"));
		put.add(Bytes.toBytes("param"), Bytes.toBytes("postid"),
				Bytes.toBytes(id));
		ht.put(put);

		// create tab_user2id
		create_table("tab_user2id", "info", 1);

		// create tab_id2user
		create_table("tab_id2user", "info", 1);

		/*
		 * tab_follow rowkey:userid CF:name:userid => username version => 1
		 */
		create_table("tab_follow", "name", 1);

		/*
		 * tab_followed rowkey:userid_{userid} CF:userid => userid
		 */
		create_table("tab_followed", "userid", 1);
		
		/*
		 * tab_post
		 * rowkey:postid
		 * CF:content
		 * */
		create_table("tab_post", "post", 1);
		
		/*
		 * tab_inbox
		 * rowkey:userid+postid
		 * CF:postid
		 */
		create_table("tab_inbox", "postid", 1);
	}

	public Set<String> getAllUser() throws Exception {
		Set<String> set = new HashSet<String>();
		HTable tab_user2id = new HTable(conf, "tab_user2id");
		Scan s = new Scan();

		ResultScanner ss = tab_user2id.getScanner(s);
		for (Result r : ss) {
			String name = new String(r.getRow());
			set.add(name);
			System.out.print(name);
		}
		return set;
	}

	public Set<String> getFollow(String username) throws Exception {
		long id = this.getIdByUsername(username);
		Set<String> set = new HashSet<String>();

		HTable tab_follow = new HTable(conf, "tab_follow");
		Get get = new Get(Bytes.toBytes(id));
		Result rs = tab_follow.get(get);

		for (KeyValue kv : rs.raw()) {
			String s = new String(kv.getValue());
			set.add(s);
			System.out.println(s);
		}

		tab_follow.close();
		return set;
	}

	public boolean alreadyFollow(long oid, long did) throws Exception {
		HTable tab_users = new HTable(conf, "tab_users");

		Get get = new Get(Bytes.toBytes(oid));
		get.setMaxVersions(500);
		Result rs = tab_users.get(get);

		List<KeyValue> list = rs.getColumn(Bytes.toBytes("user"),
				Bytes.toBytes("follow"));

		tab_users.close();
		for (KeyValue kv : list) {
			if (did == Bytes.toLong(kv.getValue()))
				return true;
		}
		return false;
	}

	public boolean follow(String oname, String dname) throws Exception {
		long oid = this.getIdByUsername(oname);
		long did = this.getIdByUsername(dname);

		if (oid == 0 || did == 0 || oid == did)
			return false;

		/*
		 * tab_follow rowkey:userid CF:name:userid => username version => 1
		 */
		HTable tab_follow = new HTable(conf, "tab_follow");

		Put put = new Put(Bytes.toBytes(oid));
		put.add(Bytes.toBytes("name"), Bytes.toBytes(did), dname.getBytes());
		tab_follow.put(put);
		tab_follow.close();

		/*
		 * tab_followed rowkey:userid_{userid} CF:userid => userid
		 */
		HTable tab_followed = new HTable(conf, "tab_followed");
		put = new Put(Bytes.add(Bytes.toBytes(did), Bytes.toBytes(oid)));
		put.add(Bytes.toBytes("userid"), null, Bytes.toBytes(oid));
		tab_followed.put(put);
		tab_followed.close();
		return true;
	}

	public boolean unfollow(String oname, String dname) throws Exception {
		long oid = this.getIdByUsername(oname);
		long did = this.getIdByUsername(dname);

		if (oid == 0 || did == 0 || oid == did)
			return false;

		/*
		 * tab_follow rowkey:userid CF:name:userid => username version => 1
		 */
		HTable tab_follow = new HTable(conf, "tab_follow");

		Delete del = new Delete(Bytes.toBytes(oid));
		del.deleteColumns(Bytes.toBytes("name"), Bytes.toBytes(did));
		tab_follow.delete(del);
		tab_follow.close();

		/*
		 * tab_followed rowkey:userid_{userid} CF:userid => userid
		 */
		HTable tab_followed = new HTable(conf, "tab_followed");

		del = new Delete(Bytes.add(Bytes.toBytes(did), Bytes.toBytes(oid)));
		tab_followed.delete(del);
		tab_followed.close();
		return true;
	}

	public boolean deleteUser(long id) throws Exception {
		String username = getNameById(id);
		if (username.equals(""))
			return false;

		HTable tab_user2id = new HTable(conf, "tab_user2id");
		HTable tab_id2user = new HTable(conf, "tab_id2user");

		Delete del = new Delete(username.getBytes());
		tab_user2id.delete(del);

		del = new Delete(Bytes.toBytes(id));
		tab_id2user.delete(del);

		tab_user2id.close();
		tab_id2user.close();
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

		tab_global.close();
		tab_user2id.close();
		tab_id2user.close();
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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HbaseIf hbase = new HbaseIf();
		// hbase.createTables();
		/*
		 * h.createTables(); if(h.createNewUser("robby1", "robby"))
		 * System.out.println("add user success"); else
		 * System.out.println("add user failed");
		 */

		
		hbase.createTables();
		hbase.createNewUser("user1", "pwd1");
		hbase.createNewUser("user2", "pwd1");
		hbase.createNewUser("user3", "pwd1");
		hbase.createNewUser("user4", "pwd1");
		hbase.createNewUser("user5", "pwd1");

		
		
		hbase.follow("user1", "user2");
		hbase.follow("user3", "user2");
		hbase.follow("user4", "user2");


		
		
		
	}

}
