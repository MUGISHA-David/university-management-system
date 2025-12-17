-- ============================================================
-- University Management System Database Schema
-- Database: university_management_system_db
-- DBMS: PostgreSQL
-- ============================================================

-- Drop tables if they exist (for fresh setup)
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS instructors CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Table 1: USERS (For Login - Contains 2 Technical Validations)
-- ============================================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8),  -- Technical: Min 8 chars
    email VARCHAR(100) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),  -- Technical: Email format
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN' CHECK (role IN ('ADMIN', 'STAFF', 'VIEWER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table 2: STUDENTS (5+ Attributes)
-- ============================================================
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('Male', 'Female', 'Other')),
    email VARCHAR(100) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),  -- Technical: Email format
    phone VARCHAR(15) NOT NULL CHECK (phone ~ '^[0-9]{10,15}$'),  -- Technical: Numeric, 10-15 digits
    address TEXT,
    enrollment_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'GRADUATED', 'SUSPENDED'))
);

-- ============================================================
-- Table 3: COURSES (5+ Attributes)
-- ============================================================
CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INTEGER NOT NULL CHECK (credits >= 1 AND credits <= 6),  -- Business: Credits 1-6
    department VARCHAR(100) NOT NULL,
    description TEXT,
    max_capacity INTEGER DEFAULT 50 CHECK (max_capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table 4: INSTRUCTORS (5+ Attributes)
-- ============================================================
CREATE TABLE instructors (
    instructor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    phone VARCHAR(15) NOT NULL CHECK (phone ~ '^[0-9]{10,15}$'),
    department VARCHAR(100) NOT NULL,
    hire_date DATE DEFAULT CURRENT_DATE
);

-- ============================================================
-- Table 5: ENROLLMENTS (Many-to-Many Relationship)
-- ============================================================
CREATE TABLE enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    enrollment_date DATE DEFAULT CURRENT_DATE,
    grade VARCHAR(5),
    status VARCHAR(20) DEFAULT 'ENROLLED' CHECK (status IN ('ENROLLED', 'COMPLETED', 'DROPPED')),
    UNIQUE(student_id, course_id)  -- Business: No duplicate enrollments
);

-- ============================================================
-- Insert default admin user
-- ============================================================
INSERT INTO users (username, password, email, role) 
VALUES ('admin', 'admin123', 'admin@university.edu', 'ADMIN');

-- ============================================================
-- Sample Data for Testing
-- ============================================================
INSERT INTO instructors (first_name, last_name, qualification, email, phone, department) VALUES
('John', 'Smith', 'PhD Computer Science', 'john.smith@university.edu', '0781234567', 'Computer Science'),
('Jane', 'Doe', 'MSc Mathematics', 'jane.doe@university.edu', '0789876543', 'Mathematics');

INSERT INTO courses (course_code, course_name, credits, department, description, max_capacity) VALUES
('CS101', 'Introduction to Programming', 3, 'Computer Science', 'Basic programming concepts using Java', 100),
('MATH201', 'Calculus I', 4, 'Mathematics', 'Differential and Integral Calculus', 80),
('CS201', 'Data Structures', 4, 'Computer Science', 'Arrays, Lists, Trees, Graphs', 60);

INSERT INTO students (first_name, last_name, date_of_birth, gender, email, phone, address) VALUES
('Alice', 'Johnson', '2002-05-15', 'Female', 'alice.johnson@student.edu', '0781111111', 'Kigali, Rwanda'),
('Bob', 'Williams', '2001-08-22', 'Male', 'bob.williams@student.edu', '0782222222', 'Huye, Rwanda');
