package org.robby.filter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.robby.CdrPro;
import org.robby.Post;

//export HBASE_CLASSPATH="/home/robby/cusfilter.jar"
public class cusfilter implements Filter{

	byte[] v1;
	
	cusfilter(){
		super();
	}
	
	cusfilter(String a){		
		this.v1 = a.getBytes();
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		this.v1 = Bytes.readByteArray(arg0);
		//this.v2 = Bytes.readByteArray(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		Bytes.writeByteArray(arg0, this.v1);
		//Bytes.writeByteArray(arg0, this.v2);
	}

	@Override
	public boolean filterAllRemaining() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReturnCode filterKeyValue(KeyValue arg0) {
		// TODO Auto-generated method stub
		if(Arrays.equals(arg0.getQualifier(), v1))
			return ReturnCode.INCLUDE;
		return ReturnCode.SKIP;
	}

	@Override
	public boolean filterRow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void filterRow(List<KeyValue> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean filterRowKey(byte[] arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KeyValue getNextKeyHint(KeyValue arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean hasFilterRow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KeyValue transform(KeyValue arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	

	

}
