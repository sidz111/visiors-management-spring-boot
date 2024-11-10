# Visitors Management System

The **Visitors Management System** is a robust application designed to streamline the management of visitor information within a secure and organized environment. Built using **Spring Boot**, this system leverages **Spring Data JPA** for database interaction, **Thymeleaf** for dynamic HTML rendering, and **Hibernate ORM** for object-relational mapping. This project enables organizations to monitor visitor activities, log security actions, and keep a record of all visitor-related activities in a centralized and efficient manner.

# Views of Project
![Screenshot (282)](https://github.com/user-attachments/assets/08d86504-2d26-48e1-b4b9-33503b8a0554)
![Screenshot (273)](https://github.com/user-attachments/assets/330ea416-511e-4444-883b-b46653b9c244)
![Screenshot (274)](https://github.com/user-attachments/assets/428652a7-7bf5-48b5-939a-94895e180ca9)
![Screenshot (275)](https://github.com/user-attachments/assets/94e2d566-fbe9-4e10-8774-daf7db9840c5)
![Screenshot (276)](https://github.com/user-attachments/assets/913be92b-7b29-45f6-a200-d7b9d14c0a12)
![Screenshot (277)](https://github.com/user-attachments/assets/500f3afa-85af-44de-bffc-e32532d5953d)
![Screenshot (278)](https://github.com/user-attachments/assets/2a2160e3-11df-4479-b0dc-7ca55fd2cb93)
![Screenshot (280)](https://github.com/user-attachments/assets/5cdc1814-7cb9-4373-9d25-209ec492981a)
![Screenshot (279)](https://github.com/user-attachments/assets/3261e174-a36f-46e7-b2d9-6a69a4265234)
![Screenshot (281)](https://github.com/user-attachments/assets/10365de8-4eab-4f28-b56e-ca5a3ecc704e)



## Features

1. **Visitor Records Management**:
   - Store and retrieve visitor details including name, purpose of visit, and visit time.
   - Provides easy-to-use CRUD (Create, Read, Update, Delete) operations for managing visitor records.

2. **Security Logging**:
   - Records security actions associated with each visit.
   - Logs important actions, such as visitor entry and exit, in real-time, providing traceability and enhancing security measures.

3. **Search and Filter**:
   - Search visitor records and security logs by visitor ID.
   - Filter data easily to locate specific visitor information, enabling quick access to historical records.

4. **User-friendly Interface**:
   - Built with **Thymeleaf** templates, providing a seamless experience for administrators to manage visitor data and view logs.
   - Responsive and intuitive web interface optimized for clarity and ease of use.

5. **Data Integrity and Security**:
   - Ensures data consistency and integrity with database-level constraints.
   - Uses Spring Security for securing sensitive endpoints (if required).

## Project Structure

The project is structured using a layered architecture pattern:

- **Entities**: Represent the core data structures within the system, including `VisitRecord` and `SecurityLog`.
- **Repositories**: Leverage **Spring Data JPA** to interface with the database, providing methods for database interactions.
- **Service Layer**: Handles the business logic, encapsulating the core functionality for managing visitor records and security logs.
- **Controllers**: Manage HTTP requests, directing them to the appropriate services and rendering data on the frontend using Thymeleaf templates.

### Entity Classes

- **VisitRecord**: Stores visitor information, including name, purpose, and time of visit.
- **SecurityLog**: Logs security actions and associates each log entry with a `VisitRecord`, enabling detailed tracking of visitor-related activities.

### Key Endpoints

 **Visitor Record Endpoints**:
   - `GET /visitors`: Retrieve a list of all visitors.
   - `POST /visitor`: Create a new visitor entry.
   - `PUT /visitor/{id}`: Update an existing visitor entry.
   - `DELETE /visitor/{id}`: Delete a visitor entry.


## Technology Stack

- **Java**: The core programming language.
- **Spring Boot**: Provides the application framework, supporting RESTful web services and project structure.
- **Spring Data JPA**: Simplifies the implementation of data access layers.
- **Hibernate ORM**: Manages the interaction between Java objects and database tables.
- **Thymeleaf**: Templating engine for server-side rendering of HTML.
- **MySQL Database**: Manages persistent data storage.

## Installation and Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/sidz111/Visitors-Management-System.git
   cd Visitors-Management-System
   ```
