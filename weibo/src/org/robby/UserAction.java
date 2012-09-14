package org.robby;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	String username;
	String password;
	String password2;
	String errmsg;

	HbaseIf hbase;
	public UserAction(){
		hbase = HbaseIf.getInstance();
		username = new String();
		password = new String();
	}
	
	public String Login() throws Exception {
		if(username.equals(""))
			return ERROR;
		
		
		
		if(hbase.checkPassword(username, password) > 0)
			return SUCCESS;
		else
			return ERROR;
	}

	public String Register() throws Exception{
		System.out.println("user:"+username);
		if(username.equals(""))
			return ERROR;
		
		if(username.length()<4){
			errmsg = "用户名长度不能小于4位！";
			return ERROR;
		}
		
		if(password.length()<4){
			errmsg = "密码长度不能小于4位！";
			return ERROR;
		}
		
		if(!password.equals(password2)){
			errmsg = "密码不一致！";
			return ERROR;
		}
		
		if(hbase.createNewUser(username, password))
			return SUCCESS;
		else
			return ERROR;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String execute() throws Exception{
		
		return SUCCESS;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	
}
