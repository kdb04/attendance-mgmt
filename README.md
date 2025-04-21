# Attendance Management System

A comprehensive system for tracking and managing attendance in educational institutions, including features for students, teachers, and administrators.

## Overview

This project is a full-stack attendance management application built with:
- **Backend**: Java Spring Boot API
- **Frontend**: React.js with modern UI components
- **Database**: MySQL

The system is designed with clean architecture principles, implementing design patterns like Factory Method, Repository, and Facade to ensure maintainability and scalability.

## Features

### Administrator Features
- Add, update, and manage student records
- Add, update, and manage teacher records
- Create and manage course information
- Allocate courses to teachers

### Teacher Features
- View assigned courses
- Create and manage class sessions
- Record and update attendance for each session
- View historical attendance records

### Student Features
- View enrolled courses
- Check attendance statistics and current status
- Track attendance requirements and shortfalls

## Tech Stack

### Backend
- Java Spring Boot
- JDBC for database connectivity
- RESTful API architecture
- SQL query optimization for attendance statistics

### Frontend
- React.js
- React Hooks for state management
- CSS with responsive design
- API integration using Axios

### Database
- MySQL with efficient schema design
- Triggers for automated attendance tracking
- Foreign key constraints for data integrity

## Getting Started

### Prerequisites
- Java 17 or newer
- Maven
- Node.js and npm
- MySQL Server

### Running the Backend
```bash
cd backend/AttendanceManagementSystem
mvn spring-boot:run
```
This will start the Spring Boot server at `http://localhost:8080`

### Running the Frontend
```bash
cd frontend
npm install
npm run dev
```
This will start the development server at `http://localhost:5173`

### Database Configuration
Database connection parameters can be configured in:
```
backend/AttendanceManagementSystem/src/main/resources/application.properties
backend/AttendanceManagementSystem/src/main/java/com/AttendanceManagementSystem/util/DBConnection.java
```

## User Authentication

- **Admin**: Username: `admin`, Password: `admin123#`
- **Students**: Use SRN format `PES2UG22CS123`, Password: `any` (follows pattern validation)
- **Teachers**: Use TRN format `TRN123`, Password: `any` (follows pattern validation)

## Project Structure

### Backend Components
- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic
- **Repositories**: Handle database operations
- **Models**: Define data structure
- **Factories**: Create user objects based on role
- **Facades**: Simplify complex subsystems like authentication

### Frontend Components
- **Dashboard views** for different user roles
- **Forms** for data input and management
- **Tables** for data display
- **Stats components** for attendance visualization

## License 

MIT — Use it, Build on it and Make it Great Again.

## Contributors

Motamarri Sai Sathvik: http://github.com/User-LazySloth
Narayan Sreekumar: https://github.com/vortex73
Nikhil Srivatsa : https://github.com/kdb04/attendance-mgmt
Nitheesh Pugazhanthi : https://github.com/noth2006