import { useState } from 'react';
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
            </div>

            {message && (
                <div className={`message ${message.type}`}>
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
        </div>
    );
}

export default AdminDashboard;