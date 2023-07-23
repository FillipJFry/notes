`LetsCode Notes` is a simple and useful app to manage personal notes.

Main features:
- multiple users support
- simple and easy-to-understand design
- cross-platform
- launches on the port 9999

The app is parametrized by the environment vars:
- DB_SERVER - a name or IP-address of the PostgreSQL server, with stores the app data
- DB_SERVER_PORT - a port which is listened by the server. The standard port number for PostgreSQL is 5432
- DB_NAME - a database to store data in. Must be created beforehand
- DB_USERNAME and DB_PASSWORD - credentials to gain access to the database

Possible command to start the app:
java -jar notes.jar --spring.profiles.active=prod

Technologies used:
- Java 11
- Spring Boot 2.7 (MVC, Data, Security)
- Thymeleaf
- PostgreSQL
- Flyway