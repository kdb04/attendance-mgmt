import { useState, useEffect } from 'react'
import api from './services/api'
import './StudentDashboard.css' // We can reuse the same CSS
import './TeacherDashboard.css' // Add this new CSS file for teacher-specific styles

function TeacherDashboard({ trn }) {
    const [courses, setCourses] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)
    const [selectedCourse, setSelectedCourse] = useState(null)
    const [students, setStudents] = useState([])
    const [loadingStudents, setLoadingStudents] = useState(false)

    useEffect(() => {
        const fetchAssignedCourses = async () => {
            try {
                console.log('Fetching courses for TRN:', trn)
                const response = await api.get(`/teachers/${trn}/courses`)
                console.log('API Response:', response.data)
                setCourses(response.data)
                setLoading(false)
            } catch (err) {
                console.error('Error fetching courses:', err)
                setError(err.response?.data?.message || 'Failed to fetch assigned courses')
                setLoading(false)
            }
        }

        if (trn) {
            fetchAssignedCourses()
        }
    }, [trn])

    const handleCourseSelect = async (course) => {
        setSelectedCourse(course)
        setLoadingStudents(true)
        
        try {
            const response = await api.get(`/courses/${course.courseCode}/students`)
            setStudents(response.data)
            setLoadingStudents(false)
        } catch (err) {
            console.error('Error fetching students:', err)
            setStudents([])
            setLoadingStudents(false)
        }
    }

    const handleBackToCourses = () => {
        setSelectedCourse(null)
        setStudents([])
    }

    if (loading) {
        return (
            <div className="teacher-content">
                <div className="loading">Loading courses...</div>
            </div>
        )
    }

    if (error) {
        return (
            <div className="teacher-content">
                <div className="error-message">
                    {error}
                    <button onClick={() => window.location.reload()} className="retry-button">
                        Retry
                    </button>
                </div>
            </div>
        )
    }

    if (selectedCourse) {
        return (
            <div className="teacher-content">
                <div className="course-header">
                    <button onClick={handleBackToCourses} className="back-button">
                        ← Back to Courses
                    </button>
                    <h2>{selectedCourse.courseName} ({selectedCourse.courseCode})</h2>
                </div>
                
                <div className="student-list-container">
                    <h3>Enrolled Students</h3>
                    
                    {loadingStudents ? (
                        <div className="loading">Loading students...</div>
                    ) : students.length === 0 ? (
                        <div className="no-students">No students enrolled in this course</div>
                    ) : (
                        <div className="table-wrapper">
                            <table className="student-table">
                                <thead>
                                    <tr>
                                        <th>SRN</th>
                                        <th>Name</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {students.map(student => (
                                        <tr key={student.srn}>
                                            <td>{student.srn}</td>
                                            <td>{student.name}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        )
    }

    return (
        <div className="teacher-content">
            <h2>Your Assigned Courses</h2>
            {courses?.length === 0 ? (
                <div className="no-courses">
                    <p>You are not assigned to any courses</p>
                </div>
            ) : (
                <div className="courses-grid">
                    {courses?.map(course => (
                        <div 
                            key={course.courseCode} 
                            className="course-card clickable"
                            onClick={() => handleCourseSelect(course)}
                        >
                            <h3>{course.courseName}</h3>
                            <div className="course-details">
                                <p><strong>Course Code:</strong> {course.courseCode}</p>
                                <p><strong>Credits:</strong> {course.credits}</p>
                            </div>
                            <div className="view-students-prompt">
                                Click to view enrolled students
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}

export default TeacherDashboard