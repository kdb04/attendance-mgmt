import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import StudentDashboard from './StudentDashboard';
import TeacherDashboard from './TeacherDashboard';
import './Dashboard.css';
import AdminDashboard from './AdminDashboard';

function Dashboard() {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const role = localStorage.getItem('role');
        const name = localStorage.getItem('name');
        
        if (!userId || !role) {
            navigate('/');
            return;
        }
        
        setUserData({ userId, role, name });
        setLoading(false);
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem('userId');
        localStorage.removeItem('role');
        localStorage.removeItem('name');
        navigate('/');
    };

    if (loading) {
        return <div className="loading">Loading...</div>;
    }

    const renderRoleBasedContent = () => {
        switch (userData?.role) {
            case 'STUDENT':
                return <StudentDashboard srn={userData.userId} />;
            case 'TEACHER':
                return <TeacherDashboard trn={userData.userId} />;
            case 'ADMIN':
                return <AdminDashboard />; 
            default:
                return <div>Unknown role</div>;
        }
    };

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <div className="user-info">
                    <h1>{userData?.role.toLowerCase() === 'admin' ? 'Admin' : userData?.role.charAt(0).toUpperCase() + userData?.role.slice(1).toLowerCase()} Dashboard</h1>
                    <div className="user-details">
                        <p>Welcome, {userData?.name}</p>
                        <p>ID: {userData?.userId}</p>
                    </div>
                </div>
                <button onClick={handleLogout} className="logout-btn">
                    Logout
                </button>
            </header>
            <main className="dashboard-content">
                <div className="content-wrapper">
                    {renderRoleBasedContent()}
                </div>
            </main>
        </div>
    );
}

export default Dashboard;
