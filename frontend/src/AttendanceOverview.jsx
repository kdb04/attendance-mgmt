import { useState } from "react"
import "./Dashboard.css"

function AttendanceOverview() {
  // Sample data - in a real app, this would come from an API
  const attendanceData = {
    present: 42,
    absent: 3,
    late: 5,
    totalDays: 50,
    presentPercentage: 84,
  }

  const cards = [
    {
      title: "Present",
      value: attendanceData.present,
      percentage: attendanceData.presentPercentage,
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="icon-green">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
          <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
      ),
      className: "card-green",
    },
    {
      title: "Absent",
      value: attendanceData.absent,
      percentage: Math.round((attendanceData.absent / attendanceData.totalDays) * 100),
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="icon-red">
          <circle cx="12" cy="12" r="10"></circle>
          <line x1="15" y1="9" x2="9" y2="15"></line>
          <line x1="9" y1="9" x2="15" y2="15"></line>
        </svg>
      ),
      className: "card-red",
    },
    {
      title: "Late",
      value: attendanceData.late,
      percentage: Math.round((attendanceData.late / attendanceData.totalDays) * 100),
      icon: (
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="icon-amber">
          <circle cx="12" cy="12" r="10"></circle>
          <polyline points="12 6 12 12 16 14"></polyline>
        </svg>
      ),
      className: "card-amber",
    },
  ]

  return (
    <>
      {cards.map((card) => (
        <div
          key={card.title}
          className={`overview-card ${card.className}`}
        >
          <div className="card-content">
            <div>
              <p className="card-title">{card.title}</p>
              <div className="card-value">
                <h3>{card.value}</h3>
                <p>days</p>
              </div>
              <p className="card-percentage">
                {card.percentage}% of total classes
              </p>
            </div>
            <div className="card-icon">
              {card.icon}
            </div>
          </div>
        </div>
      ))}
    </>
  )
}

export default AttendanceOverview
