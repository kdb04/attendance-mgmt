/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: ooad
-- ------------------------------------------------------
-- Server version	11.7.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Current Database: `ooad`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ooad` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `ooad`;

--
-- Table structure for table `Attendance`
--

DROP TABLE IF EXISTS `Attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Attendance` (
  `attendance_id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` int(11) NOT NULL,
  `srn` varchar(13) NOT NULL,
  `status` enum('present','absent','deceased') NOT NULL,
  `timestamp` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`attendance_id`),
  UNIQUE KEY `session_id` (`session_id`,`srn`),
  KEY `srn` (`srn`),
  CONSTRAINT `Attendance_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `ClassSessions` (`session_id`),
  CONSTRAINT `Attendance_ibfk_2` FOREIGN KEY (`srn`) REFERENCES `Students` (`srn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Attendance`
--

LOCK TABLES `Attendance` WRITE;
/*!40000 ALTER TABLE `Attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `Attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ClassSessions`
--

DROP TABLE IF EXISTS `ClassSessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ClassSessions` (
  `session_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_code` varchar(20) NOT NULL,
  `trn` varchar(13) NOT NULL,
  `session_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `course_code` (`course_code`,`session_date`,`start_time`),
  KEY `trn` (`trn`),
  CONSTRAINT `ClassSessions_ibfk_1` FOREIGN KEY (`course_code`) REFERENCES `Courses` (`course_code`),
  CONSTRAINT `ClassSessions_ibfk_2` FOREIGN KEY (`trn`) REFERENCES `Teachers` (`trn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ClassSessions`
--

LOCK TABLES `ClassSessions` WRITE;
/*!40000 ALTER TABLE `ClassSessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ClassSessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Courses`
--

DROP TABLE IF EXISTS `Courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Courses` (
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `credits` int(11) DEFAULT NULL,
  PRIMARY KEY (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Courses`
--

LOCK TABLES `Courses` WRITE;
/*!40000 ALTER TABLE `Courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `Courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StudentEnrollments`
--

DROP TABLE IF EXISTS `StudentEnrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `StudentEnrollments` (
  `srn` varchar(13) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  `enrollment_date` date DEFAULT curdate(),
  PRIMARY KEY (`srn`,`course_code`),
  KEY `course_code` (`course_code`),
  CONSTRAINT `StudentEnrollments_ibfk_1` FOREIGN KEY (`srn`) REFERENCES `Students` (`srn`),
  CONSTRAINT `StudentEnrollments_ibfk_2` FOREIGN KEY (`course_code`) REFERENCES `Courses` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StudentEnrollments`
--

LOCK TABLES `StudentEnrollments` WRITE;
/*!40000 ALTER TABLE `StudentEnrollments` DISABLE KEYS */;
/*!40000 ALTER TABLE `StudentEnrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Students`
--

DROP TABLE IF EXISTS `Students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Students` (
  `srn` varchar(13) NOT NULL,
  `name` varchar(100) NOT NULL,
  `year_of_study` int(11) NOT NULL CHECK (`year_of_study` between 1 and 4),
  PRIMARY KEY (`srn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Students`
--

LOCK TABLES `Students` WRITE;
/*!40000 ALTER TABLE `Students` DISABLE KEYS */;
/*!40000 ALTER TABLE `Students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TeacherAssignments`
--

DROP TABLE IF EXISTS `TeacherAssignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `TeacherAssignments` (
  `trn` varchar(13) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  PRIMARY KEY (`trn`,`course_code`),
  KEY `course_code` (`course_code`),
  CONSTRAINT `TeacherAssignments_ibfk_1` FOREIGN KEY (`trn`) REFERENCES `Teachers` (`trn`),
  CONSTRAINT `TeacherAssignments_ibfk_2` FOREIGN KEY (`course_code`) REFERENCES `Courses` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TeacherAssignments`
--

LOCK TABLES `TeacherAssignments` WRITE;
/*!40000 ALTER TABLE `TeacherAssignments` DISABLE KEYS */;
/*!40000 ALTER TABLE `TeacherAssignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Teachers`
--

DROP TABLE IF EXISTS `Teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Teachers` (
  `trn` varchar(13) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`trn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Teachers`
--

LOCK TABLES `Teachers` WRITE;
/*!40000 ALTER TABLE `Teachers` DISABLE KEYS */;
/*!40000 ALTER TABLE `Teachers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-03-16 18:47:19


-- Insert into Courses
INSERT INTO Courses (course_code, course_name, credits) VALUES
('CS101', 'Introduction to Programming', 5),
('CS102', 'Mathematics for CS', 4),
('CS201', 'Data Structures', 5),
('CS202', 'Database Management', 4),
('CS203', 'Computer Networks', 4),
('CS301', 'Operating Systems', 5),
('CS302', 'Artificial Intelligence', 4),
('CS303', 'Machine Learning', 4),
('CS304', 'Cybersecurity', 4),
('CS305', 'Cloud Computing', 4);

-- Insert into Teachers
INSERT INTO Teachers (trn, name) VALUES
('TRN001', 'Dr. Adams'),
('TRN002', 'Prof. Baker'),
('TRN003', 'Dr. Carter'),
('TRN004', 'Prof. Davis'),
('TRN005', 'Dr. Evans'),
('TRN006', 'Prof. Foster'),
('TRN007', 'Dr. Green'),
('TRN008', 'Prof. Harris'),
('TRN009', 'Dr. Johnson'),
('TRN010', 'Prof. King');

-- Insert into TeacherAssignments
INSERT INTO TeacherAssignments (trn, course_code) VALUES
('TRN001', 'CS101'),
('TRN002', 'CS102'),
('TRN003', 'CS201'),
('TRN004', 'CS202'),
('TRN005', 'CS203'),
('TRN006', 'CS301'),
('TRN007', 'CS302'),
('TRN008', 'CS303'),
('TRN009', 'CS304'),
('TRN010', 'CS305');

-- Insert into Students
INSERT INTO Students (srn, name, year_of_study) VALUES
('PES2UG22CS125', 'Alice Johnson', 3),
('PES2UG22CS302', 'Bob Smith', 3),
('PES2UG22CS478', 'Charlie Davis', 3),
('PES2UG22CS589', 'David Williams', 3),
('PES2UG22CS642', 'Eva Brown', 3),
('PES2UG23CS011', 'Frank Miller', 2),
('PES2UG23CS237', 'Grace Wilson', 2),
('PES2UG23CS389', 'Henry Moore', 2),
('PES2UG24CS053', 'Ivy Taylor', 1),
('PES2UG24CS194', 'Jack Anderson', 1);

-- Insert into StudentEnrollments
INSERT INTO StudentEnrollments (srn, course_code, enrollment_date) VALUES
('PES2UG22CS125', 'CS301', CURRENT_DATE()),
('PES2UG22CS302', 'CS302', CURRENT_DATE()),
('PES2UG22CS478', 'CS303', CURRENT_DATE()),
('PES2UG22CS589', 'CS304', CURRENT_DATE()),
('PES2UG22CS642', 'CS305', CURRENT_DATE()),
('PES2UG23CS011', 'CS201', CURRENT_DATE()),
('PES2UG23CS237', 'CS202', CURRENT_DATE()),
('PES2UG23CS389', 'CS203', CURRENT_DATE()),
('PES2UG24CS053', 'CS101', CURRENT_DATE()),
('PES2UG24CS194', 'CS102', CURRENT_DATE());
