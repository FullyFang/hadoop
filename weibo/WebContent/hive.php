<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="3rd/bootstrap/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="css/weibo.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="3rd/bootstrap/js/bootstrap.js"></script>
<title>首页</title>
</head>
<body
	style="background-image: url(img/bg.png); background-repeat: no-repeat; background-color: #C0DEED">
	<div class="container">
		<div class="page-header" style="border: none">
			<h1>Hive PHP Demo</h1>
			<table class="table">
					<thead>
						<th>oaddr</th>
						<th>oareacode</th>
						<th>daddr</th>
						<th>dareacode</th>
						<th>ts</th>
						<th>type</th>
					</thead>
					<tbody>
						<?php

$GLOBALS['THRIFT_ROOT'] = '/home/robby/Downloads/tmp/hive-0.9.0-bin/lib/php/';

require_once $GLOBALS['THRIFT_ROOT'] . 'packages/hive_metastore/hive_metastore/ThriftHiveMetastore.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'packages/hive_service/ThriftHive.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'protocol/TBinaryProtocol.php';

$transport = new TSocket('localhost', 10000);
$protocol = new TBinaryProtocol($transport);

$client = new ThriftHiveClient($protocol);
$transport->open();

$client->execute('select * from tab_cdr limit 10');
//var_dump($client->fetchAll());
$result = $client->fetchAll();
foreach($result as $k => $v){
	print("<tr>");
	$arrTemp = explode("\t", $v);
	print "<td>" . $arrTemp[0] . "</td>";
	print "<td>" . $arrTemp[1] . "</td>";
	print "<td>" . $arrTemp[2] . "</td>";
	print "<td>" . $arrTemp[3] . "</td>";
	print "<td>" . $arrTemp[4] . "</td>";
	print "<td>" . $arrTemp[5] . "</td>";
	print("</tr>");
}

$transport->close();

?>
				  </tbody>
			</table>
		</div>

</body>
</html>