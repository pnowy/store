# Store application

## About

The store application is a SpringBoot application which allows to manage a list of products (create / update and retrieve)
and place an orders of given products.

The products are versioned so the modifications of the products don't affects created orders.

The following technologies are used for the project:
    
- Spring Boot
- Spring Data JPA
- Liquibase / H2 / Postgresql for store data
- Javers library for product versioning
- JUnit 5.X / SpringBooot Test / AssertJ

The JDK 8 or higher version is required.

## How to build & run the application

By default application use H2 database (file based).

In order to build & runt the application use the following command:
 
`./gradlew build && java -jar build/libs/*.jar`

The application will run on embedded Tomcat server on port 8080.

You could check it by open: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) or `curl http://localhost:8080/actuator/health`.

You should receive the following response:

```json
{
  "status": "UP"
}
```

##### H2 Console

The application on default (developer) configuration expose H2 console. You will find it under: `http://localhost:8080/h2-console`

##### API documentation

The API documentation is exposed under: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) by Swagger project.

## How to use postgres SQL

If you would like to use PostgreSQL database you could use docker compose file saved on `docker` directory.

Just run:

`docker-compose -f postgres.yml up`

This command will run local Postgres database on port 5432.

To connect locally application to postgres you could used prepared `postgres` profile. Just run the application with:

`java -jar build/libs/*.jar -Dspring.profiles.active=postgres` 

During the initialization you should see on the logs:

```text
2018-12-09 16:37:51.614  INFO 42863 --- [           main] com.github.pnowy.store.StoreApplication  : The following profiles are active: postgres
```

## Notes

The default configuration properties are designed for local development. 
In order to use it on production please prepare the production configuration.

For product versioning the Javers library is used. For this purpose you could also use Hibernate Envers (but it is designed 
only for ORM) or create own solution (which could be desired from performance and model perspective).

For API documentation you could also use Spring Rest Docs project.
