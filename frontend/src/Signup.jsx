import { useState } from "react"
import "./Signup.css"
import { Link } from "react-router-dom"

function Signup() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    email: "",
  })

  const [error, setError] = useState("")

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }))
    // Clear error when user starts typing
    if (error) setError("")
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    // Validate all fields are filled
    if (!formData.username || !formData.password || !formData.confirmPassword || !formData.email) {
      setError("Please fill in all fields")
      return
    }

    // Validate password match
    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match")
      return
    }

    // Validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(formData.email)) {
      setError("Please enter a valid email address")
      return
    }

    setError("")
    console.log("Signup attempt:", formData)
    // Here you would typically call an API to register the user
  }

  return (
    <div className="signup-container">
      <div className="signup-box">
        <h1 className="signup-title">Attendance Management System</h1>
        <h2 className="signup-subtitle">Create Account</h2>
        <form className="signup-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Choose a username"
              autoComplete="username"
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Enter your email"
              autoComplete="email"
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
              placeholder="Create a password"
              autoComplete="new-password"
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Confirm your password"
              autoComplete="new-password"
            />
          </div>
          {error && <div className="error-message">{error}</div>}
          <button type="submit" className="signup-button">
            Sign Up
          </button>
          <div className="login-link">
            Already have an account? <Link to="/">Login</Link>
          </div>
        </form>
      </div>
    </div>
  )
}

export default Signup

