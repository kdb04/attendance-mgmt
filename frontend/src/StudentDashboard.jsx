import { useState, useEffect } from 'react'
import api from './services/api'
import './StudentDashboard.css'

function StudentDashboard({ srn }) {
    const [courses, setCourses] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        const fetchEnrolledCourses = async () => {
            try {
                console.log('Fetching courses for SRN:', srn)
                const response = await api.get(`/students/${srn}/courses`)
                console.log('API Response:', response.data)
                setCourses(response.data)
                setLoading(false)
            } catch (err) {
                console.error('Error fetching courses:', err)
                setError(err.response?.data?.message || 'Failed to fetch enrolled courses')
                setLoading(false)
            }
        }

        if (srn) {
            fetchEnrolledCourses()
        }
    }, [srn])

    if (loading) {
        return (
            <div className="student-content">
                <div className="loading">Loading courses...</div>
            </div>
        )
    }

    if (error) {
        return (
            <div className="student-content">
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
        <div className="student-content">
            <h2>Your Enrolled Courses</h2>
            {courses?.length === 0 ? (
                <div className="no-courses">
                    <p>You are not enrolled in any courses</p>
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

export default StudentDashboard
