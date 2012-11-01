package org.robby.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public final class RobBigger3 extends UDF {
  public static int total = 0;
  
  public Boolean evaluate(final Text s) {
    if (s != null) {
    	int t = Integer.parseInt(s.toString());
    	if(t>3)
    		return true;
    }
    return null;
  }
}

//add jar hiverob.jar
//create temporary function my_bigger3 as 'org.robby.hive.RobBigger3';