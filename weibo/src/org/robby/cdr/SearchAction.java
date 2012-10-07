package org.robby.cdr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.robby.CdrPro;
import org.robby.HbaseCdrIf;

import com.opensymphony.xwork2.ActionSupport;

public class SearchAction extends ActionSupport{
	String addr;
	List<CdrPro.SmCdr> cdrs;
	
	public SearchAction(){
		addr = new String();
	}
	
	public String execute() throws Exception{
		if(addr.length()!=11)
			return SUCCESS;
			
		HbaseCdrIf hif  = HbaseCdrIf.getInstance();
		cdrs = hif.getSmCdr(addr);
		return SUCCESS;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public List<CdrPro.SmCdr> getCdrs() {
		return cdrs;
	}

	public void setCdrs(List<CdrPro.SmCdr> cdrs) {
		this.cdrs = cdrs;
	}
	
	
}
