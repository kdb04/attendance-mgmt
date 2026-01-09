import { useState, useEffect } from 'react';
import api from './services/api';
import './AdminDashboard.css';

function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('students');
    const [message, setMessage] = useState(null);
    const [loading, setLoading] = useState(false);

    const [studentData, setStudentData] = useState({
        srn: '',
        name: '',
        yearOfStudy: 1
    });

    const [teacherData, setTeacherData] = useState({
        trn: '',
        name: ''
    });

    const [courseData, setCourseData] = useState({
        courseCode: '',
        courseName: '',
        credits: 3
    });

    const [assignmentData, setAssignmentData] = useState({
        trn: '',
        courseCode: ''
    });

    const [enrollmentData, setEnrollmentData] = useState({
        srn: '',
        courseCode: ''
    });

    const [teachers, setTeachers] = useState([]);
    const [courses, setCourses] = useState([]);
    const [students, setStudents] = useState([]);

    const [fetchingData, setFetchingData] = useState(false);

    // Fetch teachers and courses when allocation/enrollement tab is selected
    useEffect(() => {
        if (activeTab === 'allocations' || activeTab === 'enrollments') {
            fetchTeachersAndCourses();
        }
    }, [activeTab]);

    const fetchTeachersAndCourses = async () => {
        setFetchingData(true);
        try {
            const [teachersResponse, coursesResponse, studentResponse] = await Promise.all([
                api.get('/teachers'),
                api.get('/courses'),
                api.get('/students')
            ]);
            setTeachers(teachersResponse.data);
            setCourses(coursesResponse.data);
            setStudents(studentResponse.data);
        } catch (err) {
            console.error('Failed to fetch data:', err);
            setMessage({ type: 'error', text: 'Failed to load teachers or courses' });
        } finally {
            setFetchingData(false);
        }
    };

    const handleStudentSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/admin/students', studentData);
            setMessage({ type: 'success', text: 'Student added successfully' });
            setStudentData({ srn: '', name: '', yearOfStudy: 1 });
        } catch (err) {
            setMessage({ type: 'error', text: err.response?.data || 'Failed to add student' });
        } finally {
            setLoading(false);
        }
    };

    const handleTeacherSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/admin/teachers', teacherData);
            setMessage({ type: 'success', text: 'Teacher added successfully' });
            setTeacherData({ trn: '', name: '' });
        } catch (err) {
            setMessage({ type: 'error', text: err.response?.data || 'Failed to add teacher' });
        } finally {
            setLoading(false);
        }
    };

    const handleCourseSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/admin/courses', courseData);
            setMessage({ type: 'success', text: 'Course added successfully' });
            setCourseData({ courseCode: '', courseName: '', credits: 3 });
        } catch (err) {
            setMessage({ type: 'error', text: err.response?.data || 'Failed to add course' });
        } finally {
            setLoading(false);
        }
    };

    const handleAssignmentSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/admin/teacher-assignments', assignmentData);
            setMessage({ type: 'success', text: 'Course assigned to teacher successfully' });
            setAssignmentData({ trn: '', courseCode: '' });
        } catch (err) {
            setMessage({ type: 'error', text: err.response?.data || 'Failed to assign course to teacher' });
        } finally {
            setLoading(false);
        }
    };

    const handleEnrollmentSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await api.post('/admin/student-enrollments', enrollmentData);
            setMessage({ type: 'success', text: 'Student enrolled successfully' });
            setEnrollmentData({ srn: '', courseCode: '' });
        } catch (err) {
            setMessage({ type: 'error', text: err.response?.data?.message || 'Failed to enroll student' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="admin-dashboard">
            <div className="admin-tabs">
                <button 
                    className={activeTab === 'students' ? 'active' : ''} 
                    onClick={() => setActiveTab('students')}
                >
                    Add Student
                </button>
                <button 
                    className={activeTab === 'teachers' ? 'active' : ''} 
                    onClick={() => setActiveTab('teachers')}
                >
                    Add Teacher
                </button>
                <button 
                    className={activeTab === 'courses' ? 'active' : ''} 
                    onClick={() => setActiveTab('courses')}
                >
                    Add Course
                </button>
                <button 
                    className={activeTab === 'allocations' ? 'active' : ''} 
                    onClick={() => setActiveTab('allocations')}
                >
                    Allocate Courses
                </button>
                <button 
                    className={activeTab === 'enrollments' ? 'active' : ''} 
                    onClick={() => setActiveTab('enrollments')}
                >
                    Enroll Student
                </button>
            </div>

            <div className="form-container">
                {message && (
                    <div className={`message ${message.type} slide-in`}>
                        {message.text}
                        <button onClick={() => setMessage(null)}>&times;</button>
                    </div>
                )}

                {activeTab === 'students' && (
                    <form onSubmit={handleStudentSubmit} className="admin-form">
                        <h3>Add New Student</h3>
                        <div className="form-group">
                            <label>SRN:</label>
                            <input 
                                type="text" 
                                value={studentData.srn}
                                onChange={e => setStudentData({...studentData, srn: e.target.value})}
                                pattern="PES2UG\d{2}CS\d{3}"
                                placeholder="e.g., PES2UG22CS123"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Name:</label>
                            <input 
                                type="text" 
                                value={studentData.name}
                                onChange={e => setStudentData({...studentData, name: e.target.value})}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Year of Study:</label>
                            <select 
                                value={studentData.yearOfStudy}
                                onChange={e => setStudentData({...studentData, yearOfStudy: parseInt(e.target.value)})}
                            >
                                <option value={1}>1st Year</option>
                                <option value={2}>2nd Year</option>
                                <option value={3}>3rd Year</option>
                                <option value={4}>4th Year</option>
                            </select>
                        </div>
                        <button type="submit" disabled={loading}>
                            {loading ? 'Adding...' : 'Add Student'}
                        </button>
                    </form>
                )}

                {activeTab === 'teachers' && (
                    <form onSubmit={handleTeacherSubmit} className="admin-form">
                        <h3>Add New Teacher</h3>
                        <div className="form-group">
                            <label>TRN:</label>
                            <input 
                                type="text" 
                                value={teacherData.trn}
                                onChange={e => setTeacherData({...teacherData, trn: e.target.value})}
                                pattern="TRN\d{3}"
                                placeholder="e.g., TRN123"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Name:</label>
                            <input 
                                type="text" 
                                value={teacherData.name}
                                onChange={e => setTeacherData({...teacherData, name: e.target.value})}
                                required
                            />
                        </div>
                        <button type="submit" disabled={loading}>
                            {loading ? 'Adding...' : 'Add Teacher'}
                        </button>
                    </form>
                )}

                {activeTab === 'courses' && (
                    <form onSubmit={handleCourseSubmit} className="admin-form">
                        <h3>Add New Course</h3>
                        <div className="form-group">
                            <label>Course Code:</label>
                            <input 
                                type="text" 
                                value={courseData.courseCode}
                                onChange={e => setCourseData({...courseData, courseCode: e.target.value})}
                                pattern="[A-Z]{2}\d{3}"
                                placeholder="e.g., CS101"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Course Name:</label>
                            <input 
                                type="text" 
                                value={courseData.courseName}
                                onChange={e => setCourseData({...courseData, courseName: e.target.value})}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Credits:</label>
                            <select
                                value={courseData.credits}
                                onChange={e => setCourseData({...courseData, credits: parseInt(e.target.value)})}
                                required
                            >
                                <option value={2}>2 (20 classes)</option>
                                <option value={3}>3 (40 classes)</option>
                                <option value={4}>4 (60 classes)</option>
                                <option value={5}>5 (80 classes)</option>
                                <option value={6}>6 (100 classes)</option>
                            </select>
                            <div className="form-text">
                                Total classes will be automatically determined based on credits.
                            </div>
                        </div>
                        <button type="submit" disabled={loading}>
                            {loading ? 'Adding...' : 'Add Course'}
                        </button>
                    </form>
                )}

                {activeTab === 'allocations' && (
                    <form onSubmit={handleAssignmentSubmit} className="admin-form">
                        <h3>Allocate Course to Teacher</h3>
                        
                        {fetchingData ? (
                            <div className="loading">Loading data...</div>
                        ) : (
                            <>
                                <div className="form-group">
                                    <label>Select Teacher:</label>
                                    <select
                                        value={assignmentData.trn}
                                        onChange={e => setAssignmentData({...assignmentData, trn: e.target.value})}
                                        required
                                    >
                                        <option value="">Select a teacher</option>
                                        {teachers.map(teacher => (
                                            <option key={teacher.trn} value={teacher.trn}>
                                                {teacher.name} ({teacher.trn})
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label>Select Course:</label>
                                    <select
                                        value={assignmentData.courseCode}
                                        onChange={e => setAssignmentData({...assignmentData, courseCode: e.target.value})}
                                        required
                                    >
                                        <option value="">Select a course</option>
                                        {courses.map(course => (
                                            <option key={course.courseCode} value={course.courseCode}>
                                                {course.courseName} ({course.courseCode})
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <button type="submit" disabled={loading}>
                                    {loading ? 'Assigning...' : 'Assign Course'}
                                </button>
                            </>
                        )}
                    </form>
                )}

                {activeTab === 'enrollments' && (
                    <form onSubmit={handleEnrollmentSubmit} className="admin-form">
                        <h3>Enroll Student to Course</h3>

                        {fetchingData ? (
                            <div className="loading">Loading data...</div>
                        ) : (
                            <>
                                <div className="form-group">
                                    <label>Select Student:</label>
                                    <select
                                        value={enrollmentData.srn}
                                        onChange={e => setEnrollmentData({
                                            ...enrollmentData,
                                            srn: e.target.value
                                        })}
                                        required
                                    >
                                        <option value="">Select a student</option>
                                        {students.map(student => (
                                            <option key={student.srn} value={student.srn}>
                                                {student.name} ({student.srn})
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label>Select Course:</label>
                                    <select
                                        value={enrollmentData.courseCode}
                                        onChange={e => setEnrollmentData({
                                            ...enrollmentData,
                                            courseCode: e.target.value
                                        })}
                                        required
                                    >
                                        <option value="">Select a course</option>
                                        {courses.map(course => (
                                            <option key={course.courseCode} value={course.courseCode}>
                                                {course.courseName} ({course.courseCode})
                                            </option>
                                        ))}
                                    </select>
                                </div>
                
                                <button type="submit" disabled={loading}>
                                    {loading ? 'Enrolling...' : 'Enroll Student'}
                                </button>
                            </>
                        )}
                    </form>
                )}

            </div>
        </div>
    );
}

export default AdminDashboard;
