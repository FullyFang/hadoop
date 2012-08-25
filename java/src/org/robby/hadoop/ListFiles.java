package org.robby.hadoop;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class ListFiles {
	public static void main(String[] args) throws Exception {
		String dir = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dir), conf);
		
		Path[] paths = new Path[1];
		paths[0] = new Path(dir);

		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}
}
