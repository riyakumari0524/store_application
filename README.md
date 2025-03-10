# Store Application

**Capstone Project** for a **Store Application** to manage inventory, orders, and user authentication.

## Features
- User registration & login
- Product management (add, update, delete)
- Order management


## Prerequisites

- **Java 17+**
- **Maven**
- **IDE** (IntelliJ IDEA, Eclipse, etc.)

## Database 

H2 Database

## Setup Process

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/store_application.git
cd store_application
```

### 2. Configure the Database

- **H2 Database**
```properties
spring.datasource.url=jdbc:h2:mem:testdb;
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.platform=h2
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
hibernate.hbm2ddl.auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```


### 3. Build the Project

#### With Maven:
```bash
mvn clean install
```


### 4. Run the Application

#### With Maven:
```bash
mvn spring-boot:run
```


The application will be available at [http://localhost:8081](http://localhost:8081).

- **Swagger URL**: Point to where the Swagger UI can be accessed by http://localhost:8081/swagger-ui/index.html.


## Testing

Run all tests:

#### With Maven:
```bash
mvn test
```



## Troubleshooting


1. **Port already in use**:  
   Change port in `application.properties`:
   ```properties
   server.port=8082
   ```

lights the key setup and usage details. Let me know if you need more adjustments!
