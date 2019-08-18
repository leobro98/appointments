# Appointment Management Service

This is solution to the test assignment for appointments management REST service. The assignment itself can be found in `coding-exercise.md`.

## Architecture

The application consists of three components: `rest`, `service` and `data` and is built with Spring Boot. HSQLDB is used as a data storage. The core component is `service`, two other components are plug-ins for it. `rest` is the "main" application partition and packages other components together with itself into the executable `jar` file.

### Service

The `service` component contains definition of the business object `Appointment`, `ServiceResponse` passed to a client and the `AppointmentService` class handling requests from the `rest` component. Business logic for handling the request for creating test random appointments is separated to the `RandomHelper`. The component contains `AppointmentStorage` interface, which is an API that the data layer needs to implement.

### Data

`AppointmentStorageImpl` implements `AppointmentStorage` interface and uses in turn `AppointmentRepository` which Spring implements behind the scenes. `AppointmentStorageImpl` is injected by Spring into the `AppointmentService` in the `service` component. As `service` component has no references to the `data` component, the last can be developed and deployed independently (if packaged separately). The database engine and the database schema can thus be changed without affecting the business logic component.

### Rest

`AppointmentController` accepts HTTP requests from a client and makes corresponding requests to the `service` component. `ResponseFactory` translates responses from `service` into the `ResponseEntity` with appropriate status, headers and body. The component can be developed and deployed separately from other components.

### Database

HSQLDB database is used in the in-memory mode with the consequence that all the data is lost after the application shutdown. Spring boot starts the database at the application start and makes a connection to it behind the scenes. After starting the database, it runs the `data.sql` script from the `rest/src/main/resources` folder to create the database. The database contains the single `appointments` table.

## Building the application

To be sure that you have the correct version of Maven installed and set up, you can use Maven Wrapper contained in this project. Open the project folder (`appointments`). On Linux, you can run

```
./mvnw clean package
```

On Windows, you can run

```
mvnw.cmd clean package
```

A normal Maven build will be executed with the one important change that if the user doesn't have the necessary version of Maven specified in `.mvn/wrapper/maven-wrapper.properties` it will be downloaded for the user first, installed and then used.

Subsequent uses of `mvn/mvnw.cmd` use the previously downloaded, specific version as needed.

## Running the application

The application can be run from any IDE like Eclipse or IntelliJ IDEA like a normal Java application specifying `AppointmentApplication` as a main class. As it is packaged as an executable `jar`, you can also run it directly from a command line. Build the application, open the application folder (`appointments`) and type

```
java -jar rest/target/rest-1.0-SNAPSHOT.jar
```

Alternatively, the packaging can be changed to `war` in the `pom.xml` file in the `rest` component to allow the application deployment into a standalone Web server like Tomcat.

The working application is available on the address `http://localhost:8080/`. You can try, for example, to make a request

```
http://localhost:8080/schedule?quantity=30&enddate=2019-08-23
```

to create random appointments or read read them using

```
http://localhost:8080/appointments?startdate=2019-08-20&enddate=2019-08-22
```
