package org.robby.cdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.robby.HbaseCdrIf;

import com.opensymphony.xwork2.ActionSupport;

public class CdrDailyACAction extends ActionSupport{
	

	Map<String, Integer> dailyReport;
	
	public CdrDailyACAction(){
		dailyReport = new LinkedHashMap<String, Integer>();
	}
	
	public String execute() throws Exception{
		HbaseCdrIf hif = HbaseCdrIf.getInstance();
		dailyReport = hif.getDailyReportAC();
		return SUCCESS;
	}

	public Map<String, Integer> getDailyReport() {
		return dailyReport;
	}

	public void setDailyReport(Map<String, Integer> dailyReport) {
		this.dailyReport = dailyReport;
	}

	
}
