import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import StudentDashboard from './StudentDashboard'
import TeacherDashboard from './TeacherDashboard'
import './Dashboard.css'

function Dashboard() {
    const [userData, setUserData] = useState(null)
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate()

    useEffect(() => {
        const userId = localStorage.getItem('userId')
        const role = localStorage.getItem('role')
        const name = localStorage.getItem('name')

        if (!userId || !role) {
            navigate('/')
            return
        }

        setUserData({ userId, role, name })
        setLoading(false)
    }, [navigate])

    const handleLogout = () => {
        localStorage.removeItem('userId')
        localStorage.removeItem('role')
        localStorage.removeItem('name')
        navigate('/')
    }

    if (loading) {
        return <div className="loading">Loading...</div>
    }

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <div className="user-info">
                    <h1>{userData?.role.toLowerCase()} Dashboard</h1>
                    <p>Welcome, {userData?.name}</p>
                    <p>ID: {userData?.userId}</p>
                </div>
                <button onClick={handleLogout} className="logout-btn">
                    Logout
                </button>
            </header>

            <main className="dashboard-content">
                {userData?.role === 'STUDENT' && (
                    <StudentDashboard srn={userData.userId} />
                )}

                {userData?.role === 'TEACHER' && (
                    <TeacherDashboard trn={userData.userId} />
                )}

                {userData?.role === 'ADMIN' && (
                    <div className="admin-content">
                        <h2>System Overview</h2>
                        <p>Admin controls will appear here</p>
                    </div>
                )}
            </main>
        </div>
    )
}

export default Dashboard
