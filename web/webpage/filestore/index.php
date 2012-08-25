<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

		<title>Weibo Demo</title>
		<link href="../../css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="../../js/jquery.form.js"></script>
		<script type="text/javascript" src="../../js/jquery.cookie.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {

			});
		</script>
	</head>

	<body>
		<div class="container">
			<div class="page-header">
				<h1>File Upload&Download Demo</h1>
			</div>

			<form class="well" action="upload_file.php" method="post"
			enctype="multipart/form-data">
				<label for="file">Filename:</label>
				<input type="file" name="file" id="file" />
				<br />
				<input type="submit" name="submit" value="Submit" />
			</form>

			<div>
				<a href="upload/1.jpg"><img style="height:100px; weight:100px; border: 2px solid #FFCC66" src="upload/1.jpg"></img></a>
				<a href="upload/1.jpg"><img style="height:100px; weight:100px; border: 2px solid #FFCC66" src="upload/1.jpg"></img></a>
				<a href="upload/1.jpg"><img style="height:100px; weight:100px; border: 2px solid #FFCC66" src="upload/1.jpg"></img></a>
				
			</div>
		</div>

	</body>
</html>
