import { useState, useEffect } from 'react'
import api from './services/api'
import './StudentDashboard.css' // We can reuse the same CSS

function TeacherDashboard({ trn }) {
    const [courses, setCourses] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

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
                        <div key={course.courseCode} className="course-card">
                            <h3>{course.courseName}</h3>
                            <div className="course-details">
                                <p><strong>Course Code:</strong> {course.courseCode}</p>
                                <p><strong>Credits:</strong> {course.credits}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}

export default TeacherDashboard