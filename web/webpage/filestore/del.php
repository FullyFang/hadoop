<?php
$file=$_GET["name"];
echo $file;
system("rm -f ./tmp/".$file);
system("rm -f ./upload/".$file);
system("java -jar delfile.jar  hdfs://hadoop.main:9000/upload/".$file);
header("location:index.php");
?>