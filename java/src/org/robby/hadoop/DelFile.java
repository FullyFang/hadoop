package org.robby.hadoop;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class DelFile {
	public static void main(String[] args) throws Exception {

		String src = args[0];

		String url = "hdfs://hadoop.main:9000/";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(url), conf);

		fs.delete(new Path(src), false);
	}
}
