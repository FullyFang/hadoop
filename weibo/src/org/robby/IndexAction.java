package org.robby;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	public String execute() throws Exception{
		return SUCCESS;
	}
}
