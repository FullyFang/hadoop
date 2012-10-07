package org.robby;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;



public class ImportCdr {

	HTablePool pool;
	Vector<Thread> thpool;

	public ImportCdr() {
		Configuration conf = HBaseConfiguration.create();
		pool = new HTablePool(conf, 20);
		thpool = new Vector<Thread>();
	}

	public class ImportThread extends Thread {
		Vector<String> files;
		HTablePool pool;

		public ImportThread(HTablePool pool, Vector<String> files) {
			this.files = files;
			this.pool = pool;
		}

		public void run() {
			try {
				HTable tab = (HTable) pool.getTable("tab_cdr");
				tab.setAutoFlush(false);
				//tab.setWriteBufferSize(10*1024*1024);
				
				for (String f : files) {
					System.out.println(f);
					BufferedReader in = new BufferedReader(new FileReader(f));
					String s;
					List<Put> l = new ArrayList<Put>();
					int i = 0;
					while ((s = in.readLine()) != null) {
						String[] arr = s.split(",");
						String row = arr[0] + "_" + arr[2] + "_" + arr[4];
						Put p = new Put(row.getBytes());
						CdrPro.SmCdr cdr = CdrPro.SmCdr.newBuilder()
								.setOaddr(arr[0])
								.setOareacode(arr[1])
								.setDaddr(arr[2])
								.setDareacode(arr[3])
								.setTimestamp(arr[4])
								.setType(arr[5])
								.build();
						
						
						p.add(Bytes.toBytes("data"), null, cdr.toByteArray());
						
						
						l.add(p);
						if (i % 1000 == 0) {
							tab.put(l);
							l = new ArrayList<Put>();
						}
						i++;
						// System.out.println(arr[4]);
					}
					tab.put(l);
					
				}
				tab.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createTable() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);

		String name = "tab_cdr";
		if (admin.tableExists(name)) {
			admin.disableTable(name);
			admin.deleteTable(name);
		}

		HTableDescriptor tableDesc = new HTableDescriptor(name);
		HColumnDescriptor hd = new HColumnDescriptor("data");
		hd.setMaxVersions(1);
		tableDesc.addFamily(hd);
		admin.createTable(tableDesc);
		
		name = "tab_cdr_daily";
		if (admin.tableExists(name)) {
			admin.disableTable(name);
			admin.deleteTable(name);
		}

		tableDesc = new HTableDescriptor(name);
		hd = new HColumnDescriptor("data");
		hd.setMaxVersions(1);
		tableDesc.addFamily(hd);
		admin.createTable(tableDesc);
	}

	public void startThread(Vector<String> vec) {
		System.out.println("Thread started");
		ImportThread t = new ImportThread(this.pool, vec);
		t.start();
		thpool.add(t);
	}

	public boolean isAllThreadStopped() {
		for (Thread t : thpool) {
			if (t.isAlive())
				return false;
		}
		return true;
	}

	public void single() throws Exception {
		// TODO Auto-generated method stub
		String p = "/home/robby/project/hadoop/testtool/output/";
		File path = new File(p);
		String[] list = path.list();
		int i = 0;

		Vector<String> vec = new Vector<String>();
		for (String s : list) {
			vec.add(p + s);
		}
		startThread(vec);
		while (!this.isAllThreadStopped())
			Thread.sleep(1000);
	}

	public void multi() throws Exception {
		// TODO Auto-generated method stub
		String p = "/var/www/hadoop/testtool/output/";
		File path = new File(p);
		String[] list = path.list();
		int i = 0;

		Vector<String> vec = new Vector<String>();
		for (String s : list) {
			vec.add(p + s);
			if (i % 10 == 0) {
				startThread(vec);
				vec = new Vector<String>();
			}
			i++;
		}
		startThread(vec);
		while (!this.isAllThreadStopped())
			Thread.sleep(1000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ImportCdr im = new ImportCdr();
		im.createTable();
		im.multi();
	}

}
