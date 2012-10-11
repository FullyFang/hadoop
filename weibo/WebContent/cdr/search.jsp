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

</head>
<body>
	<table class="table">
		<thead>
			<tr>
				<th>类型</th>
				<th>本方号码</th>
				<th>对方号码</th>
				<th>发送时间</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="cdrs" id="cdr">
				<tr>
				<s:if test='type == "0"'>
					<td>主叫</td>
				</s:if>
				<s:else>
					<td>被叫</td>
				</s:else>
			
				<!-- 	<td><s:property value="type" /></td> -->
					<td><s:property value="oaddr" /></td>
					<td><s:property value="daddr" /></td>
					<td><s:property value="timestamp" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>