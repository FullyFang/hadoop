<?php
if (($_FILES["file"]["type"] == "image/jpeg")
		&& ($_FILES["file"]["size"] < 4*1024*1024))
{
	if ($_FILES["file"]["error"] > 0)
	{
		echo "Return Code: " . $_FILES["file"]["error"] . "<br />";
	}
	else
	{
		echo "Upload: " . $_FILES["file"]["name"] . "<br />";
		echo "Type: " . $_FILES["file"]["type"] . "<br />";
		echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
		echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br />";

		if (file_exists("upload/" . $_FILES["file"]["name"]))
		{
			echo $_FILES["file"]["name"] . " already exists. ";
		}
		else
		{
			move_uploaded_file($_FILES["file"]["tmp_name"],
					"upload/" . $_FILES["file"]["name"]);
			echo "Stored in: " . "upload/" . $_FILES["file"]["name"];
			$cmd = "java -jar ./uploadfile.jar ./upload/" . $_FILES["file"]["name"] . " hdfs://hadoop.main:9000/upload/" . $_FILES["file"]["name"] ;
			system($cmd);
			
			header("location:index.php");
			//echo $cmd;
		}
	}
}
else
{
	print_r($_FILES["file"]);
	echo "Invalid file";
}
?>
