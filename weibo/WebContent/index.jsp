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
					123
				</div>
				<div class="span2">
					123
				</div>
			</div>
		</div>

	</div>


</body>
</html>