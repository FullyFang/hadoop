package org.robby;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	String username;
	String password;
	String password2;
	String content;

	HbaseIf hbase;
	public UserAction(){
		hbase = new HbaseIf();
		username = new String();
		password = new String();

	}
	
	public String Post() throws Exception{
		if(hbase.post(login_user, content))
			errmsg = "post success";
		else
			errmsg = "post error";
		
		return SUCCESS;
	}
	
	public String Unfollow() throws Exception {
		if(hbase.unfollow(login_user, username))
			errmsg = "success";
		else
			errmsg = "error";

		return SUCCESS;
	}
	
	public String Follow() throws Exception {
		if(hbase.follow(login_user, username))
			errmsg = "success";
		else
			errmsg = "error";

		return SUCCESS;
	}
	
	public String Logout() throws Exception {
		login_user = "";
		login_id = 0;
		ActionContext actionContext = ActionContext.getContext();
		Map session = actionContext.getSession();
		session.put("login_user", login_user);
		session.put("login_id", login_id);
		return SUCCESS;
	}
	
	public String Login() throws Exception {
		long id = hbase.checkPassword(username, password); 
		if( id > 0){
			login_user = username;
			login_id = id;
			ActionContext actionContext = ActionContext.getContext();
			Map session = actionContext.getSession();
			session.put("login_user", login_user);
			session.put("login_id", login_id);
		}else{
			errmsg = "登录失败！";
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String SubmitPost() throws Exception{
		errmsg = "微薄发布成功！";
		return SUCCESS;
	}

	public String Register() throws Exception{
		if(username.equals(""))
			return SUCCESS;
		
		if(username.length()<4){
			errmsg = "用户名长度不能小于4位！";
			return SUCCESS;
		}
		
		if(password.length()<4){
			errmsg = "密码长度不能小于4位！";
			return SUCCESS;
		}
		
		if(!password.equals(password2)){
			errmsg = "密码不一致！";
			return SUCCESS;
		}
		
		if(hbase.createNewUser(username, password)){
			errmsg = "注册成功！";
			return SUCCESS;
		}
		else{
			errmsg = "注册失败！";
			return SUCCESS;
		}
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
}
