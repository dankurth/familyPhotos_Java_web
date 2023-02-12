# familyPhotos

## Summary
Yet another web application to share photos.

## Status
Slowly porting bits and pieces to here from an old private repository. Not yet functional. 

## Prerequisites

These are what I'm using currently. 

* Debian GNU/Linux 11 (bullseye)
* Postgresql 13
* Apache Maven 3.6.3
* Java 11.0.18
* Tomcat 9.0.43-2


## Setup (for now I'm updating this as I go, as I do it myself)

After changing from peer to md5 in /etc/postgresql/13/main/pg_hba.conf as described in:
link https://djangocentral.com/how-to-fix-fatal-peer-authentication-failed-for-user-postgres-error/
difference here from link is to restart use (as root): "systemctl restart postgresql"
This change is simply so I can create a database-only username/password specifically for this application

Change to postgres: 
first su to root, then su to postgres, then type "psql" with no database specified

in psql as postgres:
postgres=# create user familyphotos_admin with encrypted password 'changeMe';
postgres=# create database familyphotos owner familyphotos_admin;

Exit psql and root, then psql as using user and password just set ("--password" so will be prompted for password):
$ psql familyphotos familyphotos_admin --password

Now in psql as familyphotos_admin:
familyphotos=> create table pictures (id integer NOT NULL, md5 character(32), mediumblob bytea, smallblob bytea);

Verify table:

familyphotos=> \dt
               List of relations
 Schema |   Name   | Type  |       Owner
--------+----------+-------+--------------------
 public | pictures | table | familyphotos_admin
(1 row)

familyphotos=> \d pictures
                   Table "public.pictures"
   Column   |     Type      | Collation | Nullable | Default 
------------+---------------+-----------+----------+---------
 id         | integer       |           | not null | 
 md5        | character(32) |           |          | 
 mediumblob | bytea         |           |          | 
 smallblob  | bytea         |           |          | 
 
 ## Test (just verifying at this point that the database exists, the table exists, can connect to it, can run maven, ....)
 cd familyPhotos_Java_web
 mvn clean
 mvn test

Expect to see:

Running test.database.TestDatabaseConnection
Configuring TestNG with: TestNGMapConfigurator
executing query: select * from pictures

number of rows: 0
finalizing
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.028 sec - in test.database.TestDatabaseConnection

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.080 s
[INFO] Finished at: 2023-02-11T17:55:21-08:00
[INFO] ------------------------------------------------------------------------


