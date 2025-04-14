import { useState, useEffect } from 'react'
import api from './services/api'
import './StudentDashboard.css'

function StudentDashboard({ srn }) {
    const [courses, setCourses] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)
    const [selectedCourse, setSelectedCourse] = useState(null)
    const [attendanceStats, setAttendanceStats] = useState(null)
    const [loadingStats, setLoadingStats] = useState(false)

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

    const handleCourseClick = async (course) => {
        setSelectedCourse(course)
        setLoadingStats(true)
        try {
            const response = await api.get(`/students/${srn}/courses/${course.courseCode}/attendance`)
            setAttendanceStats(response.data)
        } catch (err) {
            console.error('Error fetching attendance stats:', err)
        } finally {
            setLoadingStats(false)
        }
    }

    const handleCloseStats = () => {
        setSelectedCourse(null)
        setAttendanceStats(null)
    }

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
                        <div 
                            key={course.courseCode} 
                            className="course-card clickable"
                            onClick={() => handleCourseClick(course)}
                        >
                            <h3>{course.courseName}</h3>
                            <div className="course-details">
                                <p><strong>Course Code:</strong> {course.courseCode}</p>
                                <p><strong>Credits:</strong> {course.credits}</p>
                            </div>
                            <div className="view-stats-prompt">
                                Click to view attendance statistics
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {selectedCourse && (
                <div className="stats-modal">
                    <div className="stats-content">
                        <h3>{selectedCourse.courseName} - Attendance Statistics</h3>
                        {loadingStats ? (
                            <div className="loading">Loading statistics...</div>
                        ) : attendanceStats ? (
                            <table className="stats-table">
                                <tbody>
                                    <tr>
                                        <td>Total Classes Planned</td>
                                        <td>{attendanceStats.total_classes_for_course}</td>
                                    </tr>
                                    <tr>
                                        <td>Classes Held So Far</td>
                                        <td>{attendanceStats.classes_held_so_far}</td>
                                    </tr>
                                    <tr>
                                        <td>Classes Attended</td>
                                        <td>{attendanceStats.current_attendance}</td>
                                    </tr>
                                    <tr>
                                        <td>Current Attendance</td>
                                        <td>{attendanceStats.current_attendance_percentage}%</td>
                                    </tr>
                                    <tr>
                                        <td>Minimum Sessions Required</td>
                                        <td>{attendanceStats.min_sessions_required}</td>
                                    </tr>
                                    <tr>
                                        <td>Additional Sessions Needed</td>
                                        <td>{attendanceStats.additional_sessions_needed}</td>
                                    </tr>
                                </tbody>
                            </table>
                        ) : (
                            <p>Failed to load statistics</p>
                        )}
                        <button onClick={handleCloseStats} className="close-btn">
                            Close
                        </button>
                    </div>
                </div>
            )}
        </div>
    )
}

export default StudentDashboard
