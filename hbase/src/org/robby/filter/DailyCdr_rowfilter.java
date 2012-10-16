package org.robby.filter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.robby.CdrPro;


public class DailyCdr_rowfilter {

    static class Mapper extends TableMapper<ImmutableBytesWritable, IntWritable> {

        private int numRecords = 0;
        private static final IntWritable one = new IntWritable(1);

        @Override
        public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException {
            // extract userKey from the compositeKey (userId + counter)
        	//KeyValue kv = values.getColumnLatest(Bytes.toBytes("field"), Bytes.toBytes("time"));
        	KeyValue kv = values.getColumnLatest(Bytes.toBytes("data"), Bytes.toBytes("ts"));
        	
        	
        	//System.out.println(ts);
            //ImmutableBytesWritable userKey = new ImmutableBytesWritable(row.get(), 0, Bytes.SIZEOF_INT);
        	ImmutableBytesWritable userKey = new ImmutableBytesWritable(kv.getValue(), 0, 8);
            try {
                context.write(userKey, one);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            numRecords++;
            if ((numRecords % 10000) == 0) {
                context.setStatus("mapper processed " + numRecords + " records so far");
            }
        }
    }

    public static class Reducer extends TableReducer<ImmutableBytesWritable, IntWritable, ImmutableBytesWritable> {

        public void reduce(ImmutableBytesWritable key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }

            Put put = new Put(key.get()); 
            put.add(Bytes.toBytes("data"), Bytes.toBytes("total"), Bytes.toBytes(sum));
            System.out.println("stats :   key : " +  Bytes.toString(key.get()) + ",  count : "+ sum);
            context.write(key, put);
        }
    }
    
    public static void createTable() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(conf);

		String name = "tab_cdr_daily";
		if (admin.tableExists(name)) {
			admin.disableTable(name);
			admin.deleteTable(name);
		}

		HTableDescriptor tableDesc = new HTableDescriptor(name);
		HColumnDescriptor hd = new HColumnDescriptor("data");
		hd.setMaxVersions(1);
		tableDesc.addFamily(hd);
		admin.createTable(tableDesc);
		
	}
    
    public static void main(String[] args) throws Exception {
    	createTable();
        Configuration conf = HBaseConfiguration.create();
        Job job = new Job(conf, "daily report");
        job.setJarByClass(DailyCdr_rowfilter.class);
        Scan scan = new Scan();
        
        Filter f = new RowFilter(CompareFilter.CompareOp.EQUAL,
        	      new RegexStringComparator(".*_.*_2012080."));
        scan.setFilter(f);
        
        TableMapReduceUtil.initTableMapperJob("tab_cdr", scan, Mapper.class, ImmutableBytesWritable.class,
                IntWritable.class, job);
        TableMapReduceUtil.initTableReducerJob("tab_cdr_daily", Reducer.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}