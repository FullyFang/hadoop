//array
drop table tab_array;
create table tab_array(a array<string>, b array<int>) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' COLLECTION ITEMS TERMINATED BY ',';
load data local inpath '/home/robby/array.txt' into table tab_array;

select * from tab_array where a[0]='rob';
select * from tab_array where array_contains(b, 22);

insert into table tab_array select array(oaddr, daddr), array(1) from tab_cdr;

//map
drop table tab_map;
create table tab_map(name string, info map<string, string>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
COLLECTION ITEMS TERMINATED BY ','
MAP KEYS TERMINATED BY ':';
load data local inpath '/home/robby/map.txt' into table tab_map;

select * from tab_map;
select info['age'] from tab_map;

insert into table tab_map select oaddr, map('oareacode', oareacode, 'daddr', daddr) from tab_cdr;

//struct
drop table tab_st;
create table tab_st(name string, info struct<age:int, tel:string, addr:string>)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
COLLECTION ITEMS TERMINATED BY ',';
load data local inpath '/home/robby/st.txt' into table tab_st;

select * from tab_st;
select info.tel from tab_st;

insert into table tab_st select oaddr, named_struct('age',0, 'tel', daddr, 'addr', dareacode) from tab_cdr;

//if
select oaddr, if(oareacode='010', 'beijin', 'non-beijing') from tab_cdr limit 10;

//case
select oaddr, case oareacode when '010' then 'beijing' when '021' then 'shanghai' when '020' then 'guangzou' end from tab_cdr limit 50;
select oaddr, case when oareacode='010' then 'beijing' when oareacode='021' then 'shanghai' when oareacode='020' then 'guangzou' end from tab_cdr limit 50;

