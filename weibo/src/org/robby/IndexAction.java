package org.robby;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	List<String> follow;
	List<Post> posts;
	
	public IndexAction(){
		follow = new ArrayList<String>();
		posts = new ArrayList<Post>();
	}
	
	public String execute() throws Exception{
		if(login_user.equals(""))
			return SUCCESS;
	
		posts.add(new Post("robby", "this is a test", "2012"));
		posts.add(new Post("robby", "this is a test", "2012"));
		posts.add(new Post("robby", "this is a test", "2012"));
		posts.add(new Post("robby", "this is a test", "2012"));
		posts.add(new Post("robby", "this is a test", "2012"));
		
		follow.add("test1");
		follow.add("test2");
		follow.add("test3");
		follow.add("test4");
		System.out.println(follow.size());
		return SUCCESS;
	}

	public List<String> getFollow() {
		return follow;
	}

	public void setFollow(List<String> follow) {
		this.follow = follow;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	
	
}
