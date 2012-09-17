package org.robby;

import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	Vector<String> follow;
	Vector<String> unfollow;
	
	

	public String execute() throws Exception{
		
		return SUCCESS;
	}
	
	
}
