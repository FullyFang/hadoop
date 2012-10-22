create table tab_cdr(oaddr string, oareacode string, daddr string, dareacode string, ts string, type string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' STORED AS TEXTFILE LOCATION '/cdr00.txt';

create table tab_res(name string, num int);

insert overwrite table tab_res select 'tab_cdr', count(*) from tab_cdr;

insert into table tab_res select 'tab_cdr', count(*) from tab_cdr where type='0';

