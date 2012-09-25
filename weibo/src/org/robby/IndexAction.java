package org.robby;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	Set<String> follow;
	Set<String> unfollow;
	List<Post> posts;
	
	public IndexAction(){
		follow = new HashSet<String>();
		unfollow = new HashSet<String>();
		posts = new ArrayList<Post>();
	}
	
	public String execute() throws Exception{
		if(login_user.equals(""))
			return SUCCESS;
		
		HbaseIf hbase = HbaseIf.getInstance();
		follow = hbase.getFollow(login_user);
		Set<String> all = hbase.getAllUser();
		
		for(String s:all){
			if(!follow.contains(s))
				unfollow.add(s);
		}
		
		posts = hbase.getPost(login_user);
		
		System.out.println(follow.size());
		return SUCCESS;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Set<String> getFollow() {
		return follow;
	}

	public void setFollow(Set<String> follow) {
		this.follow = follow;
	}

	public Set<String> getUnfollow() {
		return unfollow;
	}

	public void setUnfollow(Set<String> unfollow) {
		this.unfollow = unfollow;
	}

	
	
}
