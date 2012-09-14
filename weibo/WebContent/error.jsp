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
			<a href = "index.do">返回首页</a>
		</div>


		<s:if test="errmsg.length() > 0">
			<div class="alert alert-error">
				<s:property value="errmsg" />
			</div>
		</s:if>

	</div>


</body>
</html>