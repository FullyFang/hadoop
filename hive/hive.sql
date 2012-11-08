http://blog.csdn.net/wh62592855/article/details/6409680

//basic usage
create database test;
use test;
drop database test;

dfs -ls;

create table tab_name(id int, name string);
show tables;
DROP TABLE [IF EXISTS] table_name
//clolume type
load data local inpath '/home/robby/a.txt' into table tab_name;

//
drop table tab_name;
create table tab_name(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load data local inpath '/home/robby/a.txt' into table tab_name;


describe tab_name;

//
drop table tab_cdr;
create table tab_cdr(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) row format delimited fields terminated by ',';
load data local inpath '/var/www/hadoop/testtool/output/cdr00.txt' into table tab_cdr;


select substr(ts, 0, 8), oareacode, count(*) from tab_cdr group by substr(ts, 0, 8), oareacode;

select substr(ts, 0, 8), count(*) from tab_cdr group by substr(ts, 0, 8);

create table tab_daily(ts string, num int);

insert into table tab_daily select substr(ts, 0, 8), count(*) from tab_cdr group by substr(ts, 0, 8);

//if not exists
create table if not exists tab_cdr(i int);
create table test.tt(i int);

//external table
drop table tab_name;
create external table tab_name(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' location "/tmp/test";

alter table test1 add partition (aa="11");
//external data will not be delele while table is dropped.


//key word like
drop table tab_name;
create external table tab_name(id int, name string) partitioned by(aa string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ;

load data local inpath '/home/robby/a.txt' into table tab_name partition (aa=11);
select * from tab_name partition (aa="11");

//STORED AS SEQUENCEFILE;
drop table tab_name;
create table tab_name(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS SEQUENCEFILE;
load data local inpath '/home/robby/a.txt' into table tab_name;



//
set mapred.reduce.tasks=2;


//bucketing
drop table tab_cdr_buc;
CREATE TABLE tab_cdr_buc(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string)
CLUSTERED BY(oaddr) INTO 10 BUCKETS;



set hive.enforce.bucketing = true;  
INSERT OVERWRITE TABLE tab_cdr_buc
SELECT * from tab_cdr;

//sequence file
drop table tab_cdr_seq; 
CREATE TABLE tab_cdr_seq(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) STORED AS SEQUENCEFILE;
set hive.exec.compress.output=true;
set mapred.output.compress=true;
set mapred.output.compression.type=BLOCK;
set mapred.output.compression.codec=org.apache.hadoop.io.compress.LzoCodec;
insert overwrite table tab_cdr_seq select * from tab_cdr;



//rcfile
drop table tab_cdr_rc; 
CREATE TABLE tab_cdr_rc(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) STORED AS rcfile;
set hive.exec.compress.output=true;
set mapred.output.compress=true;
set mapred.output.compression.codec=org.apache.hadoop.io.compress.LzoCodec;
insert overwrite table tab_cdr_rc select * from tab_cdr;


//
select count(*) from tab_cdr;
select count(*) from tab_cdr_seq;
select count(*) from tab_cdr_rc;
