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

import com.google.common.primitives.Bytes;

public class ImportCdr {

	HTablePool pool;

	public ImportCdr() {
		Configuration conf = HBaseConfiguration.create();
		pool = new HTablePool(conf, 20);
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
				String cf = "field";
				String f0 = "oaddr";
				String f1 = "ocode";
				String f2 = "daddr";
				String f3 = "dcode";
				String f4 = "time";
				
				byte[] bcf = cf.getBytes();
				byte[] bf0 = f0.getBytes();
				byte[] bf1 = f1.getBytes();
				byte[] bf2 = f2.getBytes();
				byte[] bf3 = f3.getBytes();
				byte[] bf4 = f4.getBytes();
				
				
				
				HTable tab = (HTable) pool.getTable("tab_cdr");
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
						p.add(bcf, bf0, arr[0].getBytes());
						p.add(bcf, bf1, arr[1].getBytes());
						p.add(bcf, bf2, arr[2].getBytes());
						p.add(bcf, bf3, arr[3].getBytes());
						p.add(bcf, bf4, arr[4].getBytes());
						l.add(p);
						if(i%100 == 0){
							tab.put(l);
							l = new ArrayList<Put>();
						}
						i++;
						//System.out.println(arr[4]);
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
		HColumnDescriptor hd = new HColumnDescriptor("field");
		hd.setMaxVersions(1);
		tableDesc.addFamily(hd);
		admin.createTable(tableDesc);
	}

	public void startThread(Vector<String> vec) {
		System.out.println("Thread started");
		ImportThread t = new ImportThread(this.pool, vec);
		t.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ImportCdr im = new ImportCdr();
		im.createTable();
		// TODO Auto-generated method stub
		String p = "/home/robby/project/hadoop/testtool/output/";
		File path = new File(p);
		String[] list = path.list();
		int i = 0;

		Vector<String> vec = new Vector<String>();
		for (String s : list) {
			vec.add(p + s);
			if (i % 10 == 0) {
				im.startThread(vec);
				vec = new Vector<String>();
			}
			i++;
		}
		im.startThread(vec);
	}

}
