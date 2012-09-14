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
<title>Register</title>
</head>
<body>
	<div class="container">
		<div class="page-header">
			<h1>Weibo Demo</h1>
		</div>



		<form class="form-horizontal" action="register.do">
			<legend>注册新用户</legend>
			<div class="control-group">
				<label class="control-label">用户名</label>
				<div class="controls">
					<input type="text" name="username">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"">密码</label>
				<div class="controls">
					<input type="password" name="password">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">重复密码</label>
				<div class="controls">
					<input type="password" id="password2">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn">注册</button>
				</div>
			</div>

		</form>
		<s:if test="errmsg.length() > 0">
			<div class="alert alert-error">
				<s:property value="errmsg" />
			</div>
		</s:if>

	</div>


</body>
</html>