<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="3rd/bootstrap/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="css/weibo.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="3rd/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
			$(document).ready(function() {
				$("#btn_search").click(function() {
					$("#form_search").ajaxSubmit(function(resp) {
						$("#search_res").html(resp);
						
					});
					return false;
				});

			});
		</script>
<title>首页</title>
</head>
<body
	style="background-image: url(img/bg.png); background-repeat: no-repeat; background-color: #C0DEED">
	<div class="container">
		<div class="page-header" style="border: none">
			<h1>CDR Demo</h1>
		</div>

		<form id="form_search" class="form-inline" action="search.do">
			<input name="addr" type="text" class="input" placeholder=""> 
			<button id="btn_search" type="submit" class="btn">提交</button>
		</form>
		
		<div id="search_res" class="well rbwell"></div>
	</div>
</body>
</html>