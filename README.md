# Dlocal-batchApplication

**Batch Application**

Batch process which execute every 30 seconds.
The process update the Sales records to Paid and Rejected.
In order to execute the process every 30 seconds, i used Spring.io TaskScheduler.
To update the records in DB, i used bulk of 50 records each time.
When scale up the application need to update the bulk size as well.


**Flow:**

Every 30 seconds, the annotated method start() of SchedulerTask is invoked.
the annotation @Scheduled(fixedRate = 30000) means that this method will be executed.
in addition, the main class BatchApplication.java is annotated with @EnableScheduling in order to let spring scan for @Scheduled methods.

for each execution, the Sale dao (repository) will catch the relevant records with Pending status and update to Paid/Rejected.

for better performance, the application update the database for each 10 records in bulk, in order to minimize the database connection.

since this application is for demo usage, the size bulk is small enough

**Probability**

In order to update records with equal probability of: Paid in 70% and Rejected in 30%, i used a map that contain 3 indexes(out of 10) which represent for each 10 DB records which indexes will be set to REJECTED.
for example: my map contain indexes after randomize (0,3,6),thus:

records from db with indexes 0,3,6 will be set to REJECTED
records from db with indexes 1,2,4,5,7,8,9 will be set to PAID.

since my Bulk size is 10 records, this probability remain for each 10 records.

**Technology:**

Spring Boot 2 - The application is managed by Spring Boot 2.

Hibernate implementation for JPA and MySql as the SQL server.

Task Scheduler- using spring.io TaskScheduler implementation.


  





**__Insert Sale records:__**

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(1,100,'2018-06-20',1,1,10) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(2,200,'2018-06-20',1,1,20) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(3,300,'2018-06-20',1,1,30) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(4,500,'2018-06-20',1,1,40) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(5,500,'2018-06-20',1,1,50) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(6,600,'2018-06-20',1,1,60) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(7,700,'2018-06-20',1,1,70) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(8,800,'2018-06-20',1,1,80) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(9,900,'2018-06-20',1,1,90) ;

insert into SALE  (id, amount,creation_date,merchant_id,status,transaction_id)   values(10,1000,'2018-06-20’,1,1,100) ;


