package org.robby.hadoop;
import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class UploadFile {
	public static void main(String[] args) throws Exception {
		try{
		String localSrc = args[0];
		String dst = args[1];
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		
		OutputStream out = fs.create(new Path(dst));
		
		IOUtils.copyBytes(in, out, 4096, true);
		System.out.print("success");
		}catch(Exception e){
			System.out.print("fail" + e.toString());
		}
	}
}
