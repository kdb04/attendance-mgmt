"use client"

import { useState } from "react"
import "./Dashboard.css"

function AttendanceHistory() {
  // Sample data - in a real app, this would come from an API
  const [attendanceHistory] = useState([
    {
      id: 1,
      date: "2025-03-18",
      subject: "Mathematics",
      status: "present",
      time: "09:00 AM",
    },
    {
      id: 2,
      date: "2025-03-17",
      subject: "Physics",
      status: "present",
      time: "10:30 AM",
    },
    {
      id: 3,
      date: "2025-03-16",
      subject: "Chemistry",
      status: "late",
      time: "01:15 PM",
      lateMinutes: 10,
    },
    {
      id: 4,
      date: "2025-03-15",
      subject: "English",
      status: "absent",
      time: "11:00 AM",
    },
    {
      id: 5,
      date: "2025-03-14",
      subject: "Computer Science",
      status: "present",
      time: "02:00 PM",
    },
  ])

  const statusIcons = {
    present: (
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
        className="icon-green"
      >
        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
        <polyline points="22 4 12 14.01 9 11.01"></polyline>
      </svg>
    ),
    absent: (
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
        className="icon-red"
      >
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="15" y1="9" x2="9" y2="15"></line>
        <line x1="9" y1="9" x2="15" y2="15"></line>
      </svg>
    ),
    late: (
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
        className="icon-amber"
      >
        <circle cx="12" cy="12" r="10"></circle>
        <polyline points="12 6 12 12 16 14"></polyline>
      </svg>
    ),
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    })
  }

  return (
    <div className="history-card">
      <div className="card-header">
        <h2>Recent Attendance</h2>
      </div>
      <div className="table-container">
        <table className="attendance-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Subject</th>
              <th>Time</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {attendanceHistory.map((record) => (
              <tr key={record.id}>
                <td>{formatDate(record.date)}</td>
                <td>{record.subject}</td>
                <td>{record.time}</td>
                <td>
                  <div className="status-cell">
                    {statusIcons[record.status]}
                    <span className={`status-text status-${record.status}`}>
                      {record.status}
                      {record.status === "late" && record.lateMinutes && ` (${record.lateMinutes} mins)`}
                    </span>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="card-footer">
        <button className="view-all-button">View All Records</button>
      </div>
    </div>
  )
}

export default AttendanceHistory

