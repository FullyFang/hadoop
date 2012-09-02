package org.robby.hadoop.mapr.maxtemp;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
	private int max;
	MaxTemperatureMapper(){
		max = 0;
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		System.out.println("map:" + key);
		String line = value.toString();
		int m = Integer.parseInt(line);
		if(m>max){
			max = m;
			context.write(new Text("max"), new IntWritable(m));
		}
	}
}
