 CREATE KEYSPACE ycsb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
 USE ycsb ;
 CREATE COLUMNFAMILY usertable ( y_id varchar PRIMARY KEY,
 field0 varchar ,
 field1 varchar ,
 field2 varchar ,
 field3 varchar ,
 field4 varchar ,
 field5 varchar ,
 field6 varchar ,
 field7 varchar ,
 field8 varchar ,
 field9 varchar );
