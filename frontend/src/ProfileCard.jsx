"use client"

import { useState } from "react"
import "./Dashboard.css"

function ProfileCard() {
  // Sample data - in a real app, this would come from an API
  const [profile] = useState({
    name: "John Doe",
    studentId: "STU2025001",
    email: "john.doe@example.com",
    phone: "+1 (555) 123-4567",
    program: "Computer Science",
    year: "3rd Year",
    avatar: "https://via.placeholder.com/100",
  })

  return (
    <div className="profile-card">
      <div className="card-header">
        <h2>Profile Information</h2>
      </div>
      <div className="profile-content">
        <img src={profile.avatar || "/placeholder.svg"} alt={profile.name} className="profile-avatar" />
        <h3 className="profile-name">{profile.name}</h3>
        <p className="profile-id">ID: {profile.studentId}</p>

        <div className="profile-details">
          <div className="profile-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <rect x="2" y="4" width="20" height="16" rx="2"></rect>
              <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"></path>
            </svg>
            <span>{profile.email}</span>
          </div>
          <div className="profile-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
            </svg>
            <span>{profile.phone}</span>
          </div>
          <div className="profile-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1-2.5-2.5Z"></path>
              <path d="M8 7h6"></path>
              <path d="M8 11h8"></path>
              <path d="M8 15h5"></path>
            </svg>
            <span>{profile.program}</span>
          </div>
          <div className="profile-item">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M22 10v6M2 10l10-5 10 5-10 5z"></path>
              <path d="M6 12v5c3 3 9 3 12 0v-5"></path>
            </svg>
            <span>{profile.year}</span>
          </div>
        </div>
      </div>
      <div className="card-footer">
        <button className="edit-profile-button">Edit Profile</button>
      </div>
    </div>
  )
}

export default ProfileCard

