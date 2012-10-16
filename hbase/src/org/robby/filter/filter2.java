package org.robby.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.robby.CdrPro;
import org.robby.Post;

public class filter2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Configuration conf = HBaseConfiguration.create();
		HTable tab = new HTable(conf, "tab_cdr");
		
		Scan s = new Scan();

		//FilterList l = new FilterList(FilterList.Operator.MUST_PASS_ONE);
		FilterList l = new FilterList();
		
		Filter f1 = new RowFilter(CompareFilter.CompareOp.EQUAL,
      	      new RegexStringComparator(".*_.*_2012080."));
		l.addFilter(f1);
      
		SingleColumnValueFilter f2 = new SingleColumnValueFilter(
			      Bytes.toBytes("data"),
			      Bytes.toBytes("oareacode"),
			      CompareFilter.CompareOp.NOT_EQUAL,
			      new SubstringComparator("010"));
		l.addFilter(f2);
		
		s.setFilter(l);
		
		ResultScanner rs = tab.getScanner(s);
		Get get = null;
		
		for (Result r : rs) {
			String out = Bytes.toString(r.getRow()) + "\t";
			
			byte[] data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("oaddr"));
			out += Bytes.toString(data) + "\t";
			
			data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("oareacode"));
			out += Bytes.toString(data) + "\t";
		
			data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("daddr"));
			out += Bytes.toString(data) + "\t";
			
			data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("dareacode"));
			out += Bytes.toString(data) + "\t";
			
			data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("ts"));
			out += Bytes.toString(data) + "\t";
			
			data = r.getValue(Bytes.toBytes("data"), Bytes.toBytes("type"));
			out += Bytes.toString(data) + "\t";
			
			System.out.println(out);
		}
	}

}
