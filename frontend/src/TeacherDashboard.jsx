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
    const [showSessionForm, setShowSessionForm] = useState(false)
    const [sessionData, setSessionData] = useState({
        courseCode: '',
        trn: '',
        sessionDate: new Date().toISOString().split('T')[0],
        startTime: '08:00',
        endTime: '09:00'
    })
    const [activeSession, setActiveSession] = useState(null)
    const [attendanceStatus, setAttendanceStatus] = useState({})
    const [submitting, setSubmitting] = useState(false)

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
        setSessionData({
            ...sessionData,
            courseCode: course.courseCode,
            trn: trn
        })
        
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
        setShowSessionForm(false)
        setActiveSession(null)
        setAttendanceStatus({})
    }

    const handleCreateSession = () => {
        setShowSessionForm(true)
    }

    const handleCloseForm = () => {
        setShowSessionForm(false)
    }

    const handleSessionInputChange = (e) => {
        const { name, value } = e.target
        setSessionData({
            ...sessionData,
            [name]: value
        })
    }

    const handleSessionSubmit = async (e) => {
        e.preventDefault()
        setSubmitting(true)

        try {
            const response = await api.post('/sessions', sessionData)
            console.log('Session created:', response.data)
            // Convert to number if it's a string to ensure correct type
            const sessionId = typeof response.data.sessionId === 'string' 
                ? parseInt(response.data.sessionId, 10) 
                : response.data.sessionId
            
            setActiveSession(sessionId)
            setShowSessionForm(false)
            
            // Initialize attendance status for all students as pending
            const initialStatus = {}
            students.forEach(student => {
                initialStatus[student.srn] = 'absent' // Default is absent according to the schema
            })
            setAttendanceStatus(initialStatus)
            
        } catch (err) {
            console.error('Error creating session:', err)
            alert('Failed to create session. Please try again.')
        } finally {
            setSubmitting(false)
        }
    }

    const handleAttendanceChange = async (srn, status) => {
        // Update local state immediately for UI feedback
        setAttendanceStatus({
            ...attendanceStatus,
            [srn]: status
        })

        try {
            await api.put('/sessions/attendance', {
                sessionId: activeSession, // This is now an integer
                srn: srn,
                status: status
            })
            console.log(`Attendance updated for ${srn} to ${status}`)
        } catch (err) {
            console.error('Error updating attendance:', err)
            // Revert on failure
            setAttendanceStatus({
                ...attendanceStatus,
                [srn]: 'absent' // Revert to default state
            })
            alert(`Failed to update attendance for ${srn}. Please try again.`)
        }
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
                    {!activeSession && !showSessionForm && (
                        <button onClick={handleCreateSession} className="create-session-btn">
                            Create New Session
                        </button>
                    )}
                </div>
                
                {showSessionForm && (
                    <div className="session-form-container">
                        <h3>Create New Class Session</h3>
                        <form onSubmit={handleSessionSubmit} className="session-form">
                            <div className="form-group">
                                <label htmlFor="sessionDate">Date</label>
                                <input
                                    type="date"
                                    id="sessionDate"
                                    name="sessionDate"
                                    value={sessionData.sessionDate}
                                    onChange={handleSessionInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="startTime">Start Time</label>
                                <input
                                    type="time"
                                    id="startTime"
                                    name="startTime"
                                    value={sessionData.startTime}
                                    onChange={handleSessionInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="endTime">End Time</label>
                                <input
                                    type="time"
                                    id="endTime"
                                    name="endTime"
                                    value={sessionData.endTime}
                                    onChange={handleSessionInputChange}
                                    required
                                />
                            </div>
                            <div className="form-actions">
                                <button 
                                    type="button" 
                                    onClick={handleCloseForm} 
                                    className="cancel-btn"
                                    disabled={submitting}
                                >
                                    Cancel
                                </button>
                                <button 
                                    type="submit" 
                                    className="submit-btn" 
                                    disabled={submitting}
                                >
                                    {submitting ? 'Creating...' : 'Create Session'}
                                </button>
                            </div>
                        </form>
                    </div>
                )}
                
                <div className="student-list-container">
                    <h3>
                        {activeSession 
                            ? 'Mark Attendance for this Session' 
                            : 'Enrolled Students'}
                    </h3>
                    
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
                                        {activeSession && <th>Attendance</th>}
                                    </tr>
                                </thead>
                                <tbody>
                                    {students.map(student => (
                                        <tr key={student.srn}>
                                            <td>{student.srn}</td>
                                            <td>{student.name}</td>
                                            {activeSession && (
                                                <td className="attendance-cell">
                                                    <button 
                                                        className={`present-btn ${attendanceStatus[student.srn] === 'present' ? 'active' : ''}`}
                                                        onClick={() => handleAttendanceChange(student.srn, 'present')}
                                                    >
                                                        ✓
                                                    </button>
                                                    <button 
                                                        className={`absent-btn ${attendanceStatus[student.srn] === 'absent' ? 'active' : ''}`}
                                                        onClick={() => handleAttendanceChange(student.srn, 'absent')}
                                                    >
                                                        ✗
                                                    </button>
                                                </td>
                                            )}
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