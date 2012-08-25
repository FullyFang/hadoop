package org.robby.hadoop;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class GetFile {
	public static void main(String[] args) throws Exception {
 
		String src = args[0];
		String dst = args[1];

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(src), conf);

		
		OutputStream out = new FileOutputStream(dst);

		InputStream in = null;
		try {
			in = fs.open(new Path(src));
			IOUtils.copyBytes(in, out, 40960, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}
}
