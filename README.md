[Geofencing-Based Attendance System — README.md](https://github.com/user-attachments/files/31250693/Geofencing-Based.Attendance.System.README.md)
# 📍 Geofencing-Based Attendance System

A **Spring Boot-based attendance management system** that uses **GPS geofencing and JWT authentication** to allow students to mark attendance only when they are physically present within the professor's predefined classroom/location boundary.

The system provides separate functionality for **Professors and Students**, with secure role-based authentication and location-based attendance validation.

---

## 🚀 Features

### 👨‍🏫 Professor

- Secure professor login using JWT authentication
- Create and manage courses
- Add students to courses
- Define a geographical attendance boundary
- View course attendance
- View attendance summaries for specific dates
- Monitor student attendance records

### 👨‍🎓 Student

- Secure student authentication using JWT
- View enrolled courses
- Mark attendance using current GPS coordinates
- Attendance is accepted only when the student is inside the allowed geofence
- Prevents attendance from unauthorized locations

### 🔐 Authentication

- JWT-based authentication
- Role-based authorization
- Separate access for `PROFESSOR` and `STUDENT`
- Protected application endpoints

---

## 🗺️ How Geofencing Works

The system uses the student's **latitude and longitude** to determine whether they are physically present within the permitted attendance location.

When a student attempts to mark attendance:

```text
Student
   │
   ▼
Login
   │
   ▼
JWT Token
   │
   ▼
Select Course
   │
   ▼
Send GPS Coordinates
   │
   ▼
Geofence Validation
   │
   ├── Inside Boundary ──► Attendance Marked
   │
   └── Outside Boundary ─► Attendance Rejected
```

The backend calculates the distance between the student's current location and the configured course location.

---

## 🏗️ System Architecture

```text
                   ┌──────────────────┐
                   │     Frontend     │
                   │   React / Vite   │
                   └────────┬─────────┘
                            │
                         REST API
                            │
                            ▼
                 ┌─────────────────────┐
                 │    Spring Boot      │
                 │      Backend        │
                 └──────────┬──────────┘
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
   ┌─────────────┐   ┌─────────────┐   ┌──────────────┐
   │ Controllers │   │  Services   │   │ JWT Security │
   └─────────────┘   └──────┬──────┘   └──────────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │   Repositories  │
                   └────────┬────────┘
                            │
                            ▼
                     ┌─────────────┐
                     │    MySQL    │
                     └─────────────┘
```

---

## 🛠️ Tech Stack

### Backend

- **Java**
- **Spring Boot**
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Maven

### Database

- **MySQL**
- Hibernate / JPA

### Frontend

- **React**
- **Vite**
- JavaScript
- HTML
- CSS

### Development Tools

- IntelliJ IDEA
- Postman
- Git
- GitHub

---

## 🔑 Authentication Flow

The application uses **JWT (JSON Web Token)** authentication.

```text
User Login
    │
    ▼
Validate Email & Password
    │
    ▼
Generate JWT
    │
    ▼
Return Token
    │
    ▼
Client Stores Token
    │
    ▼
Token Sent With API Requests
    │
    ▼
JWT Validation
    │
    ▼
Access Protected Resource
```

The JWT contains information such as:

- User ID
- Email
- Role

Supported roles:

```text
PROFESSOR
STUDENT
```

---

## 📍 Attendance Process

The attendance process follows these steps:

1. Student logs into the application.
2. Student selects an enrolled course.
3. The application obtains the student's current GPS coordinates.
4. The coordinates are sent to the Spring Boot backend.
5. The backend validates the student's location against the course geofence.
6. If the student is within the permitted radius, attendance is marked.
7. If the student is outside the permitted radius, attendance is rejected.

This ensures that students cannot simply mark attendance from an unauthorized location.

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/your-repository.git
cd attendance-system
```

### 2. Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE attendance_db;
```

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Configure JWT

Add your JWT secret in the application configuration or environment variables.

```properties
jwt.secret=YOUR_SECRET_KEY
```

> For production, secrets should be stored using environment variables rather than committed to GitHub.

### 4. Run the Backend

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application directly from IntelliJ IDEA.

### 5. Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will normally be available at:

```text
http://localhost:5173
```

---

## 🎯 Use Cases

This system can be used in:

- Colleges and universities
- Classrooms
- Training institutes
- Corporate offices
- Workshops
- Events requiring location-based attendance

---

## 🔮 Future Improvements

- QR-code-based attendance
- Face recognition
- Attendance notifications
- Attendance analytics and charts
- Export attendance to Excel/PDF
- Email notifications
- Admin dashboard
- Multiple geofences
- Real-time attendance monitoring
- Mobile application
- Improved GPS spoofing detection
- Attendance history and reports

---

## ⭐ If you found this project useful

Give the repository a ⭐ on GitHub!
