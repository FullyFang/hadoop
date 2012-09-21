package org.robby;

import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.*;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.*;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;



public class MpLoader {

	  private static final String NAME = "SampleUploader";
	  
	  static class Uploader 
	  extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

	    private long checkpoint = 100;
	    private long count = 0;
	    
	    @Override
	    public void map(LongWritable key, Text line, Context context)
	    throws IOException {
	      
	      // Input is a CSV file
	      // Each map() is a single line, where the key is the line number
	      // Each line is comma-delimited; row,family,qualifier,value
	            
	      // Split CSV line
	      String [] values = line.toString().split(",");
	      if(values.length != 5) {
	        return;
	      }
	      
	      // Extract each value
	      byte [] row = Bytes.toBytes(values[0]);
	      byte [] family = Bytes.toBytes(values[1]);
	      byte [] qualifier = Bytes.toBytes(values[2]);
	      byte [] value = Bytes.toBytes(values[3]);
	      
	      // Create Put
	      Put put = new Put(row);
	      put.add(family, qualifier, value);
	      
	      // Uncomment below to disable WAL. This will improve performance but means
	      // you will experience data loss in the case of a RegionServer crash.
	      // put.setWriteToWAL(false);
	      
	      try {
	        context.write(new ImmutableBytesWritable(row), put);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
	      
	      // Set status every checkpoint lines
	      if(++count % checkpoint == 0) {
	        context.setStatus("Emitting Put " + count);
	      }
	    }
	  }
	  
	  /**
	   * Job configuration.
	   */
	  public static Job configureJob(Configuration conf, String [] args)
	  throws IOException {
	    Path inputPath = new Path("/home/robby/project/hadoop/testtool/output");
	    String tableName = "tab_cdr";
	    Job job = new Job(conf, NAME + "_" + tableName);
	    job.setJarByClass(Uploader.class);
	    FileInputFormat.setInputPaths(job, inputPath);
	    //job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setMapperClass(Uploader.class);
	    // No reducers.  Just write straight to table.  Call initTableReducerJob
	    // because it sets up the TableOutputFormat.
	    TableMapReduceUtil.initTableReducerJob(tableName, null, job);
	    job.setNumReduceTasks(0);
	    return job;
	  }

	  /**
	   * Main entry point.
	   * 
	   * @param args  The command line parameters.
	   * @throws Exception When running the job fails.
	   */
	  public static void main(String[] args) throws Exception {
	    Configuration conf = HBaseConfiguration.create();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    
	    Job job = configureJob(conf, otherArgs);
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }
	}