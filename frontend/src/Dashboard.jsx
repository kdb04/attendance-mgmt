import { useState } from "react"
import "./Dashboard.css"
import DashboardHeader from "./DashboardHeader"
import DashboardSidebar from "./DashboardSidebar"
import AttendanceOverview from "./AttendanceOverview"
import AttendanceHistory from "./AttendanceHistory"
import ProfileCard from "./ProfileCard"

function Dashboard() {
  return (
    <div className="dashboard-container">
      <DashboardSidebar />
      <div className="dashboard-content">
        <DashboardHeader />
        <main className="dashboard-main">
          <h1 className="dashboard-title">Student Dashboard</h1>
          
          <div className="overview-grid">
            <AttendanceOverview />
          </div>
          
          <div className="dashboard-grid">
            <div className="dashboard-grid-main">
              <AttendanceHistory />
            </div>
            <div className="dashboard-grid-side">
              <ProfileCard />
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

export default Dashboard
