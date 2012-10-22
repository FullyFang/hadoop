create database test;
use test;
drop database test;

dfs -ls;

DROP TABLE [IF EXISTS] table_name
//clolume type
load data local inpath '/home/robby/a.txt' into table tab_name;

drop table tab_name;
create table tab_name(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load data local inpath '/home/robby/a.txt' into table tab_name;

