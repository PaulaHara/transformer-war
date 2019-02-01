# Transformer Battle
The Transformers are at war and this API will be responsible for settling the score.

### To create the database:
```
$ sudo mysql --password
mysql> create database transformer; // Create the new database
mysql> create user 'admin' identified by 'admin'; // Creates the user
mysql> grant all on transformer.* to 'admin'; // Gives all the privileges to the new database
```
### To run the application:
```
  mvn spring-boot:run
```

After running the applciation for the first time, you can open the file "application.properties" and
change "spring.jpa.hibernate.ddl-auto=create" to "spring.jpa.hibernate.ddl-auto=none".

### To test using a browser:
- http://localhost:8080/
> **NOTE**: I create a simple HTML page that will allow to create, update, delete and calculate the battle score.

### To test using Postman:
Retrieve all transformers: (GET) http://localhost:8080/transformer/all  <br>
Search by Id: (GET) http://localhost:8080/transformer/{id}  <br>
Create: (POST) http://localhost:8080/transformer/create  <br>
         
```json
(Body) { 
            "name": "Autobot",
            "type": "A", 
            "strength": 7, 
            "intelligence": 10, 
            "speed": 5,  
            "endurance": 10, 
            "rank": 5,  
            "courage": 10, 
            "firepower": 8,  
            "skill": 5 
        }
```
Update: (PUT) http://localhost:8080/transformer/update/{id}  <br>
         
```json
(Body) {
            "name": "Decepticon",
            "type": "D",
            "strength": 7,
            "intelligence": 8,
            "speed": 5,
            "endurance": 8,
            "rank": 4,
            "courage": 5,
            "firepower": 5,
            "skill": 7
        }
```
Delete by Id: (DELETE) http://localhost:8080/transformer/delete/{id}  <br>
Delete all: (DELETE) http://localhost:8080/transformer/deleteAll  <br>
Get battle score: (GET) http://localhost:8080/transformer/battle/{id},{id},... // List of ids
