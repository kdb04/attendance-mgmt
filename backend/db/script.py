import random
import mysql.connector
from faker import Faker

fake = Faker()
Faker.seed(0)
random.seed(0)

NUM_TEACHERS = 50
NUM_STUDENTS = 200
NUM_COURSES = 30

# Database connection
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "Nikhil1234#",
    "database": "ooad"
}

conn = mysql.connector.connect(**db_config)
cursor = conn.cursor()

#Teachers table
def generate_teachers():
    query = "INSERT INTO Teachers (trn, name) VALUES (%s, %s)"
    data = []

    trns = set()
    while len(trns) < NUM_TEACHERS:
        trn_num = random.randint(1, 999)
        trn = f"TRN{trn_num:03d}"
        if trn not in trns:
            trns.add(trn)

    #Teacher data
    for trn in trns:
        name = fake.name()
        data.append((trn, name))

    cursor.executemany(query, data)
    conn.commit()
    print(f"Inserted {len(data)} teachers.")
    return list(trns)

#Students table
def generate_students():
    query = "INSERT INTO Students (srn, name, year_of_study) VALUES (%s, %s, %s)"
    data = []

    srns = set()
    while len(srns) < NUM_STUDENTS:
        srn_num = random.randint(1, 999)
        srn = f"PES2UG22CS{srn_num:03d}"
        if srn not in srns:
            srns.add(srn)

    for srn in srns:
        name = fake.name()
        year_of_study = random.randint(1, 4)  # 4-year program
        data.append((srn, name, year_of_study))

    cursor.executemany(query, data)
    conn.commit()
    print(f"Inserted {len(data)} students.")
    return list(srns)

#Courses table
def generate_courses():
    query = "INSERT INTO Courses (course_code, course_name, credits) VALUES (%s, %s, %s)"
    data = []

    subjects = ["CS", "MA", "PH", "EC", "HU", "IS"]

    course_codes = set()
    while len(course_codes) < NUM_COURSES:
        subject = random.choice(subjects)
        code_num = random.randint(100, 999)
        course_code = f"{subject}{code_num}"
        if course_code not in course_codes and len(course_code) <= 30:
            course_codes.add(course_code)

    course_prefixes = [
        "Introduction to", "Advanced", "Principles of", "Fundamentals of",
        "Topics in", "Special Topics in", "Seminar on", "Workshop in"
    ]

    course_subjects = [
        "Computer Science", "Data Structures", "Algorithms", "Machine Learning",
        "Artificial Intelligence", "Database Systems", "Networks", "Operating Systems",
        "Software Engineering", "Web Development", "Mobile Computing", "Cybersecurity",
        "Mathematics", "Statistics", "Physics", "Electronics", "Humanities", "Information Systems"
    ]

    for course_code in course_codes:
        prefix = random.choice(course_prefixes)
        subject = random.choice(course_subjects)
        course_name = f"{prefix} {subject}"

        if len(course_name) > 100:
            course_name = course_name[:100]

        credits = random.randint(2, 6)
        data.append((course_code, course_name, credits))

    cursor.executemany(query, data)
    conn.commit()
    print(f"Inserted {len(data)} courses.")
    return list(course_codes)

if __name__ == "__main__":
    try:
        generate_teachers()
        generate_courses()
        generate_students()

        print("Data insertion for Teachers, Students, and Courses completed successfully!")
    except Exception as e:
        print(f"Error: {e}")
    finally:
        cursor.close()
        conn.close()
