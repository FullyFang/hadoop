package org.robby;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;


public class DailyCdr {

    static class Mapper1 extends TableMapper<ImmutableBytesWritable, IntWritable> {

        private int numRecords = 0;
        private static final IntWritable one = new IntWritable(1);

        @Override
        public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException {
            // extract userKey from the compositeKey (userId + counter)
        	//KeyValue kv = values.getColumnLatest(Bytes.toBytes("field"), Bytes.toBytes("time"));
        	KeyValue kv = values.getColumnLatest(Bytes.toBytes("data"), null);
        	
        	CdrPro.SmCdr cdr = CdrPro.SmCdr.parseFrom(kv.getValue());
        	String ts = cdr.getTimestamp();
        	//System.out.println(ts);
            //ImmutableBytesWritable userKey = new ImmutableBytesWritable(row.get(), 0, Bytes.SIZEOF_INT);
        	ImmutableBytesWritable userKey = new ImmutableBytesWritable(ts.getBytes(), 0, 8);
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

    public static class Reducer1 extends TableReducer<ImmutableBytesWritable, IntWritable, ImmutableBytesWritable> {

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
    
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Job job = new Job(conf, "Hbase_FreqCounter1");
        job.setJarByClass(DailyCdr.class);
        Scan scan = new Scan();
        
        //scan.addColumn(Bytes.toBytes("data"), null);
        
        //scan.setFilter(new FirstKeyOnlyFilter());
        TableMapReduceUtil.initTableMapperJob("tab_cdr", scan, Mapper1.class, ImmutableBytesWritable.class,
                IntWritable.class, job);
        TableMapReduceUtil.initTableReducerJob("tab_cdr_daily", Reducer1.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}