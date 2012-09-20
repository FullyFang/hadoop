<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="3rd/bootstrap/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="3rd/bootstrap/js/bootstrap.js"></script>
<title>首页</title>
</head>
<body>
	<div class="container">
		<div class="page-header">
			<h1>Weibo Demo</h1>
			<form class="form-inline" action="login.do" method="post">
				<s:if test="login_user.length() > 0">
					<button type="button" class="btn btn-primary disabled"
						disabled="disabled">
						当前用户：
						<s:property value="login_user" />
					</button>
				</s:if>
				<s:else>
					<input type="text" name="username" class="input-small"
						placeholder="用户名">
					<input type="password" name="password" class="input-small"
						placeholder="密码">
					<button type="submit" class="btn">登录</button>
					<a href="register.do">注册新用户</a>
				</s:else>

				<s:if test="login_user.length() > 0">
					<a href="login!Logout.do">注销</a>
				</s:if>

			</form>
		</div>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span8">
					<div class="well">
						<form action="submitpost.do">
							<textarea rows="3"></textarea>
							<button type="submit" class="btn">Submit</button>
						</form>
					</div>
					<div class="well">
						<table class="table">
							<thead>
								<tr>
									<th>发送人</th>
									<th>内容</th>
									<th>时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="posts" id="Post">
									<tr>
										<td><s:property value="sender" /></td>
										<td><s:property value="content" /></td>
										<td><s:property value="ts" /></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
				<div class="span4 well ">
					<table class="table">
						<tbody>
							<s:iterator value="follow" id="name">
								<tr>
									<td><s:property value="name" /></td>
									<td><a>取消关注</a></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>

				</div>
			</div>
		</div>

	</div>


</body>
</html>