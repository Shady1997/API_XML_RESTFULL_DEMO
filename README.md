# Spring Boot XML API Demo

A comprehensive RESTful API built with Spring Boot that handles XML requests and responses, featuring complete CRUD operations for user management with H2 in-memory database.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [API Documentation](#api-documentation)
- [Testing Examples](#testing-examples)
- [Database Access](#database-access)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## Features

- Complete CRUD operations (Create, Read, Update, Delete)
- XML request/response format
- Data validation with proper error handling
- H2 in-memory database for development
- Search functionality
- Global exception handling
- Pre-loaded sample data

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (In-memory)
- **Data Binding**: JAXB for XML
- **Build Tool**: Maven
- **Server**: Embedded Tomcat

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- cURL or Postman for testing

## Installation & Setup

### 1. Clone/Download the Project
```bash
git clone <repository-url>
cd xml-api-demo
```

### 2. Fix H2 Database Dependency
Update your `pom.xml` to fix the H2 dependency:
```xml
<dependency>
   <groupId>com.h2database</groupId>
   <artifactId>h2</artifactId>
   <version>2.2.224</version>
</dependency>
```

### 3. Create Application Configuration
Create `src/main/resources/application.yml`:
```yaml
server:
   port: 8086

spring:
   application:
      name: xmlapidemo
   datasource:
      url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
      driver-class-name: org.h2.Driver
      username: sa
      password: password
   jpa:
      database-platform: org.hibernate.dialect.H2Dialect
      hibernate:
         ddl-auto: create-drop
      show-sql: true
      defer-datasource-initialization: true
   h2:
      console:
         enabled: true
         path: /h2-console

logging:
   level:
      com.example: INFO
      org.springframework.web: INFO
```

### 4. Build and Run
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

### 5. Verify Installation
- Application: http://localhost:8086
- H2 Database Console: http://localhost:8086/h2-console
- Test endpoint: http://localhost:8086/api/users

## API Documentation

### Base URL
```
http://localhost:8086/api/users
```

### User Object Structure
```xml
<user>
    <id>1</id>
    <name>John Doe</name>
    <email>john.doe@example.com</email>
    <phone>+1234567890</phone>
    <address>123 Main St, City, Country</address>
    <active>true</active>
</user>
```

### HTTP Methods Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/search?name={name}` | Search users by name |
| GET | `/api/users/active` | Get active users only |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user completely |
| PATCH | `/api/users/{id}` | Partially update user |
| DELETE | `/api/users/{id}` | Delete user |

## Testing Examples

### 1. GET - Retrieve All Users
```bash
curl -X GET http://localhost:8086/api/users \
  -H "Accept: application/xml"
```

**Expected Response:**
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<users count="5" status="success">
    <user>
        <id>1</id>
        <name>John Doe</name>
        <email>john.doe@example.com</email>
        <phone>+1234567890</phone>
        <address>123 Main St, City, Country</address>
        <active>true</active>
    </user>
    <!-- More users... -->
</users>
```

### 2. GET - Retrieve User by ID
```bash
curl -X GET http://localhost:8086/api/users/1 \
  -H "Accept: application/xml"
```

**Expected Response:**
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>success</status>
    <message>User found successfully</message>
    <data>
        <id>1</id>
        <name>John Doe</name>
        <email>john.doe@example.com</email>
        <phone>+1234567890</phone>
        <address>123 Main St, City, Country</address>
        <active>true</active>
    </data>
    <timestamp>2025-09-19T10:30:45.123</timestamp>
</response>
```

### 3. GET - Search Users by Name
```bash
curl -X GET "http://localhost:8086/api/users/search?name=john" \
  -H "Accept: application/xml"
```

### 4. GET - Get Active Users Only
```bash
curl -X GET http://localhost:8086/api/users/active \
  -H "Accept: application/xml"
```

### 5. POST - Create New User
```bash
curl -X POST http://localhost:8086/api/users \
  -H "Content-Type: application/xml" \
  -H "Accept: application/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
      <user>
          <name>Test User</name>
          <email>test@example.com</email>
          <phone>+1234567890</phone>
          <address>Test Address</address>
          <active>true</active>
      </user>'
```

**Expected Response:**
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>success</status>
    <message>User created successfully</message>
    <data>
        <id>6</id>
        <name>Test User</name>
        <email>test@example.com</email>
        <phone>+1234567890</phone>
        <address>Test Address</address>
        <active>true</active>
    </data>
    <timestamp>2025-09-19T10:35:22.456</timestamp>
</response>
```

### 6. PUT - Update User Completely
```bash
curl -X PUT http://localhost:8086/api/users/1 \
  -H "Content-Type: application/xml" \
  -H "Accept: application/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
      <user>
          <name>Updated User Name</name>
          <email>updated@example.com</email>
          <phone>+9876543210</phone>
          <address>Updated Address</address>
          <active>true</active>
      </user>'
```

### 7. PATCH - Partially Update User
```bash
curl -X PATCH http://localhost:8086/api/users/1 \
  -H "Content-Type: application/xml" \
  -H "Accept: application/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
      <user>
          <name>Partially Updated Name</name>
      </user>'
```

### 8. DELETE - Remove User
```bash
curl -X DELETE http://localhost:8086/api/users/1 \
  -H "Accept: application/xml"\
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

**Expected Response:**
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>success</status>
    <message>User deleted successfully</message>
    <data>User with ID 1 has been deleted</data>
    <timestamp>2025-09-19T10:40:15.789</timestamp>
</response>
```

## Error Responses

### Validation Error (400 Bad Request)
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>error</status>
    <message>Validation failed</message>
    <timestamp>2025-09-19T10:45:30.123</timestamp>
</response>
```

### Not Found Error (404)
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>error</status>
    <message>User not found with id: 999</message>
    <timestamp>2025-09-19T10:50:45.456</timestamp>
</response>
```

### Duplicate Email Error (409 Conflict)
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <status>error</status>
    <message>User with email test@example.com already exists</message>
    <timestamp>2025-09-19T10:55:10.789</timestamp>
</response>
```

## Database Access

### H2 Console
- **URL**: http://localhost:8086/h2-console
- **Driver Class**: `org.h2.Driver`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

### Pre-loaded Sample Data
The application initializes with 5 sample users:
1. John Doe (Active)
2. Jane Smith (Active)
3. Bob Johnson (Active)
4. Alice Brown (Active)
5. Charlie Wilson (Inactive)

## Project Structure
```
src/
├── main/
│   ├── java/com/example/xmlapidemo/
│   │   ├── XmlApiDemoApplication.java          # Main application class
│   │   ├── entity/
│   │   │   └── User.java                       # User entity with JPA annotations
│   │   ├── dto/
│   │   │   ├── UsersResponse.java             # XML wrapper for multiple users
│   │   │   └── ApiResponse.java               # Generic API response wrapper
│   │   ├── repository/
│   │   │   └── UserRepository.java            # JPA repository interface
│   │   ├── service/
│   │   │   └── UserService.java               # Business logic layer
│   │   ├── controller/
│   │   │   └── UserController.java            # REST API endpoints
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java    # Global error handling
│   │   └── config/
│   │       └── DataInitializer.java           # Sample data initialization
│   └── resources/
│       └── application.yml                     # Application configuration
└── pom.xml                                     # Maven dependencies
```

## Data Validation Rules

| Field | Validation Rule |
|-------|----------------|
| Name | Required, 2-100 characters |
| Email | Required, valid email format, unique |
| Phone | Optional, max 15 characters |
| Address | Optional, max 200 characters |
| Active | Optional, defaults to true |

## HTTP Status Codes

| Status Code | Description |
|------------|-------------|
| 200 | OK - Successful GET, PUT, PATCH, DELETE |
| 201 | Created - Successful POST |
| 400 | Bad Request - Validation errors |
| 404 | Not Found - User doesn't exist |
| 409 | Conflict - Duplicate email |
| 500 | Internal Server Error - Unexpected errors |

## Troubleshooting

### Common Issues

1. **H2 Driver Not Found**
   ```
   Solution: Update pom.xml with explicit H2 version (2.2.224)
   ```

2. **Port Already in Use**
   ```bash
   # Change port in application.yml or kill process
   lsof -ti:8086 | xargs kill -9
   ```

3. **XML Marshalling Errors**
   ```
   Solution: Ensure proper JAXB annotations and dependencies
   ```

4. **Validation Errors**
   ```
   Solution: Check required fields (name, email) and constraints
   ```

### Logs Location
Application logs are displayed in the console. To change log levels, modify `application.yml`:
```yaml
logging:
  level:
    com.example: DEBUG  # Your package logs
    org.springframework.web: DEBUG  # Spring web logs
```

## Testing with Postman

1. Create a new collection
2. Set base URL: `http://localhost:8086/api/users`
3. For POST/PUT/PATCH requests:
   - Set `Content-Type: application/xml`
   - Set `Accept: application/xml`
4. Use the XML examples provided above

## Development Notes

- The application uses H2 in-memory database, so data is lost on restart
- All endpoints produce XML responses
- CORS is enabled for all origins
- Validation is enforced on POST and PUT operations
- PATCH operations allow partial updates

## Future Enhancements

- Add authentication/authorization
- Implement pagination for large datasets
- Add more complex search filters
- Include API versioning
- Add comprehensive unit tests
- Implement caching strategy

## Testing on Postman

```javascript
// ✅ Check for 201 Created
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// ✅ Check for correct content type
pm.test("Response is XML", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/xml");
});

// ✅ Convert XML to JSON (Postman built-in helper)
const jsonData = xml2Json(pm.response.text());

// ✅ Validate response content
pm.test("User created with correct data", function () {
    pm.expect(jsonData.user.name).to.eql("Jane Smith");
    pm.expect(jsonData.user.email).to.eql("jane.smith3@example.com");
    pm.expect(jsonData.user.active).to.eql("true");
    pm.expect(jsonData.user.id).to.exist;
});

// ✅ Define expected schema
const userSchema = {
    type: "object",
    required: ["user"],
    properties: {
        user: {
            type: "object",
            required: ["id", "name", "email", "active"],
            properties: {
                id: { type: "string", pattern: "^[0-9]+$" },
                name: { type: "string" },
                email: { type: "string", format: "email" },
                phone: { type: "string" },
                address: { type: "string" },
                active: { type: "string", enum: ["true", "false"] }
            }
        }
    }
};

// ✅ Validate schema using Postman built-in
pm.test("Response matches user schema", function () {
    pm.expect(jsonData).to.have.jsonSchema(userSchema);
});
