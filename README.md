# Attendance Management System

A comprehensive system for tracking and managing attendance in educational institutions, including features for students, teachers, and administrators.

## Overview

This project is a full-stack attendance management application built with:
- **Backend**: Java Spring Boot API
- **Frontend**: React.js with modern UI components
- **Database**: PostgreSQL
- **Deployment**: Dockerized backend on Render, frontend on Vercel

The system is designed with clean architecture principles, implementing design patterns like Factory, Facade, Strategy, Repository and Singleton methods to ensure maintainability and scalability.

## Live Demo
**Backend Server(Render)**
- Health Check: ```bash https://attendance-backend-zpio.onrender.com/api/test```
(Hosted on Render Free-Tier, can take upto 30s-1min to start-up)

**Frontend (Vercel)**
- Live UI: ```bash https://attendance-mgmt-mu.vercel.app```

**Test Credentials**
- **Student**: SRN - PES2UG22CS001, Password - test
- **Teacher**: TRN - TRN042, Password - test

## Features

### Authentication & Security
- JWT-based authentication
- Role-based access control(Admin/Student/Teacher)
- Secure route protection using a custom JWT filter
- Passwords created on first login, stored as hash and compared for subsequent logins
- Stateless backend

### Administrator Features
- Add, update, and manage student records
- Add, update, and manage teacher records
- Create and manage course information
- Allocate courses to teachers
- Enroll students to courses

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
- JWT for Authentication & RBAC
- SQL query optimization for attendance statistics

### Frontend
- React.js
- React Hooks for state management
- CSS with responsive design
- API integration using Axios

### Database
- PostgreSQL with efficient schema design
- Triggers & constraints for automation & integrity
- Optimized SQL queries for attendance analytics

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

- Motamarri Sai Sathvik: http://github.com/User-LazySloth
- Narayan Sreekumar: https://github.com/vortex73
- Nikhil Srivatsa : https://github.com/kdb04
- Nitheesh Pugazhanthi : https://github.com/noth2006
