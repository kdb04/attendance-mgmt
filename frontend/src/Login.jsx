import { useState } from "react"
import "./Login.css"
import { Link } from "react-router-dom"

function Login() {
    const [formData, setFormData] = useState({
        username: "",
        password: ""
    })

    const [error, setError] = useState("")

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData(prevState => ({
        ...prevState,
        [name]: value
        }))
        // Clear error when user starts typing
        if (error) setError("")
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        if (!formData.username || !formData.password) {
            setError('Please fill in all fields')
            return
        }
        setError('')
        console.log('Login attempt:', formData)
    }

    return (
        <div className="login-container">
        <div className="login-box">
        <h1 className="login-title">Attendance Management System</h1>
        <form className="login-form" onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="username">Username</label>
                <input type="text" id="username" name="username" value={formData.username} onChange={handleChange} placeholder="Enter your username" autoComplete="username" />
            </div>
            <div className="form-group">
                <label htmlFor="password">Password</label>
                <input type="password" id="password" name="password" value={formData.password} onChange={handleChange} placeholder="Enter your password" autoComplete="current-password" />
            </div>
            {error && <div className="error-message">{error}</div>}
            <button type="submit" className="login-button">Login</button>
            <div className="signup-link">
                Don't have an account?<Link to="/signup">Sign-Up</Link>
            </div>
        </form>
      </div>
    </div>
  )
}

export default Login
