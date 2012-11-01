drop table tab_areaname;
create table tab_areaname(areacode string, cityname string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load data local inpath '/var/www/hadoop/hive/areaname.txt' overwrite into table tab_areaname;

//join
drop table tab_res1;
create table tab_res1(oaddr string, cityname string);
insert overwrite table tab_res1 select b.oaddr, a.cityname from tab_areaname a join tab_cdr b on a.areacode = b.oareacode;

//map join
insert overwrite table tab_res1 select /*+ MAPJOIN(a) */ b.oaddr, a.cityname from tab_areaname a join tab_cdr b on a.areacode = b.oareacode;

//two tables join
drop table tab_user;
create table tab_user(addr string);
insert overwrite table tab_user select distinct(oaddr) from tab_cdr limit 100;

insert overwrite table tab_res1 select b.oaddr, a.cityname from tab_areaname a join tab_cdr b on a.areacode = b.oareacode join tab_user c on b.oaddr=c.addr;

//LEFT SEMI JOIN
insert overwrite table tab_res1 select a.oaddr, a.oareacode from tab_cdr a join tab_user b on (a.oaddr=b.addr);
insert overwrite table tab_res1 select a.oaddr, a.oareacode from tab_cdr a LEFT SEMI JOIN tab_user b on (a.oaddr=b.addr);
