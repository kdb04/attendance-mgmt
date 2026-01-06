import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import loginStyles from "./login.module.css"
import api from './services/api';

// Text Scramble class
class TextScramble {
  constructor(el) {
    this.el = el;
    this.chars = '!<>-_\\/[]{}—=+*^?#________';
    this.update = this.update.bind(this);
  }
  
  setText(newText) {
    const oldText = this.el.innerText;
    const length = Math.max(oldText.length, newText.length);
    const promise = new Promise((resolve) => this.resolve = resolve);
    this.queue = [];
    for (let i = 0; i < length; i++) {
      const from = oldText[i] || '';
      const to = newText[i] || '';
      const start = Math.floor(Math.random() * 40);
      const end = start + Math.floor(Math.random() * 40);
      this.queue.push({ from, to, start, end });
    }
    cancelAnimationFrame(this.frameRequest);
    this.frame = 0;
    this.update();
    return promise;
  }
  
  update() {
    let output = '';
    let complete = 0;
    for (let i = 0, n = this.queue.length; i < n; i++) {
      let { from, to, start, end, char } = this.queue[i];
      if (this.frame >= end) {
        complete++;
        output += to;
      } else if (this.frame >= start) {
        if (!char || Math.random() < 0.28) {
          char = this.randomChar();
          this.queue[i].char = char;
        }
        output += `<span class="dud">${char}</span>`;
      } else {
        output += from;
      }
    }
    this.el.innerHTML = output;
    if (complete === this.queue.length) {
      this.resolve();
    } else {
      this.frameRequest = requestAnimationFrame(this.update);
      this.frame++;
    }
  }
  
  randomChar() {
    return this.chars[Math.floor(Math.random() * this.chars.length)];
  }
}

function Login() {
    const [formData, setFormData] = useState({
        username: "",
        password: ""
    });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const titleRef = useRef(null);
    
    useEffect(() => {
        if (titleRef.current) {
            const phrases = [
				'Attendance Management System',
                'Track Attendance',
                'Effortlessly',
            ];
            
            const fx = new TextScramble(titleRef.current);
            let counter = 0;
            
            const next = () => {
                fx.setText(phrases[counter]).then(() => {
                    setTimeout(next, 2000);
                });
                counter = (counter + 1) % phrases.length;
            };
            
            next();
        }
        
        // Cleanup function to cancel animation frame on unmount
        return () => {
            cancelAnimationFrame(titleRef.current?.frameRequest);
        };
    }, []);
    
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
        if (error) setError("");
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formData.username || !formData.password) {
            setError('Please fill in all fields');
            return;
        }
        setLoading(true);
        try {
            const response = await api.post('/auth/login', formData);
            
            // Store auth data
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('userId', response.data.userId);
            localStorage.setItem('role', response.data.role);
            localStorage.setItem('name', response.data.name);
            // Navigate to dashboard
            navigate('/dashboard');
        } catch (err) {
            console.error('Error details:', err);
            setError(err.response?.data?.message || 'Failed to login');
        } finally {
            setLoading(false);
        }
    };
    
    return (
        <div className={loginStyles.splitContainer}>
            <div className={loginStyles.titleSection}>
                <h1 className={loginStyles.appTitle}>
                    <span ref={titleRef} className={loginStyles.scrambleText}>Attendance Management System</span>
                </h1>
            </div>
            <div className={loginStyles.loginSection}>
                <div className={loginStyles.loginBox}>
                    <h2 className={loginStyles.loginTitle}>Login</h2>
                    <form className={loginStyles.loginForm} onSubmit={handleSubmit}>
                        <div className={loginStyles.formGroup}>
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
                        <div className={loginStyles.formGroup}>
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
                        {error && <div className={loginStyles.errorMessage}>{error}</div>}
                        <button type="submit" className={loginStyles.loginButton} disabled={loading}>
                            {loading ? 'Logging in...' : 'Login'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;
