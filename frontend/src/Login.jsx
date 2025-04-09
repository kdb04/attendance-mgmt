import { useState } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import "./Login.css"

function Login() {
    const [formData, setFormData] = useState({
        username: "",
        password: ""
    })
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }))
        if (error) setError("")
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        if (!formData.username || !formData.password) {
            setError('Please fill in all fields')
            return
        }

        setLoading(true)
        try {
            console.log('Sending request to:', 'http://localhost:8080/api/auth/login')
            const response = await axios.post('http://localhost:8080/api/auth/login', formData, {
                headers: {
                    'Content-Type': 'application/json'
                },
                withCredentials: true
            })
            
            // Store auth data
            localStorage.setItem('userId', response.data.userId)
            localStorage.setItem('role', response.data.role)
            localStorage.setItem('name', response.data.name)

            // Navigate to dashboard
            navigate('/dashboard')
        } catch (err) {
            console.error('Error details:', err)
            setError(err.response?.data?.message || 'Failed to login')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="login-container">
            <div className="login-box">
                <h1 className="login-title">Attendance Management System</h1>
                <form className="login-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            placeholder="Enter SRN/TRN/admin"
                            disabled={loading}
                            autoComplete="username"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="Enter your password"
                            disabled={loading}
                            autoComplete="current-password"
                        />
                    </div>
                    {error && <div className="error-message">{error}</div>}
                    <button type="submit" className="login-button" disabled={loading}>
                        {loading ? 'Logging in...' : 'Login'}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default Login
