# transformer-war
The Transformers are at war and this API will be responsible for settling the score.

To create the database:
$ sudo mysql --password
mysql> create database transformer; -- Create the new database
mysql> create user 'admin' identified by 'admin'; -- Creates the user
mysql> grant all on transformer.* to 'admin'; -- Gives all the privileges to the new database

To run the application use the command: mvn spring-boot:run

After running the applciation for the first time, you can open the file "application.properties" and
change "spring.jpa.hibernate.ddl-auto=create" to "spring.jpa.hibernate.ddl-auto=none".
And you can also do this:
mysql> revoke all on transformer.* from 'admin';
mysql> grant select, insert, delete, update on transformer.* to 'admin';
