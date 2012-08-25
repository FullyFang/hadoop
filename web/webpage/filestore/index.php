<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

		<title>Hadoop</title>
		<link href="../../3rd/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />
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

			<form class="well form-inline" action="upload_file.php" method="post"
			enctype="multipart/form-data">
				<label for="file">Filename:</label>
				<input class="input-small" type="file" name="file" id="file" />
				<input class="btn"  type="submit" name="submit" value="Submit" />
			</form>

			<ul class="thumbnails">
				<?php
				exec("java -jar ./listfiles.jar hdfs://hadoop.main:9000/upload/", $files);
				foreach ($files as $f) {
					$dst = "./tmp/" . basename($f);
					$cmd = "java -jar ./getfile.jar ".$f." ".$dst;
					if(!file_exists($dst))
						exec($cmd);
				?>
				<li class="span3">

					<div class="thumbnail">
						<a href=<?php echo "$dst"?> class="thumbnail"> <img src=<?php echo "$dst"?> alt=""> </a>
						<div class="caption">
							<a class="btn btn-primary" href="del.php?name=<?php echo basename($f)?>">Delete</a>
						</div>
					</div>
				</li>
				<?php
				}
				//print_r($files);
				?>
				
				
			</ul>

		</div>

	</body>
</html>
