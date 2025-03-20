import { useState, useEffect } from "react"
import QRCode from "react-qr-code"
import "./Dashboard.css"
import DashboardHeader from "./DashboardHeader"
import DashboardSidebar from "./DashboardSidebar"
import "./QRCodePage.css"

function QRCodePage() {
  const [studentId, setStudentId] = useState("STU12345")
  const [timestamp, setTimestamp] = useState("")

  // Update timestamp every minute to keep QR code fresh
  useEffect(() => {
    const updateTimestamp = () => {
      const now = new Date()
      setTimestamp(now.toISOString())
    }

    updateTimestamp()
    const interval = setInterval(updateTimestamp, 60000)

    return () => clearInterval(interval)
  }, [])

  // QR code value combines student ID and timestamp for security
  const qrValue = `${studentId}|${timestamp}`

  return (
    <div className="dashboard-container">
      <DashboardSidebar />
      <div className="dashboard-content">
        <DashboardHeader />
        <main className="dashboard-main">
          <h1 className="dashboard-title">Student QR Code</h1>

          <div className="qr-code-container">
            <div className="qr-code-card">
              <div className="qr-code-header">
                <h2>Your Attendance QR Code</h2>
                <p>Scan this code to mark your attendance</p>
              </div>

              <div className="qr-code-content">
                <div className="qr-code-wrapper">
                  <QRCode value={qrValue} size={220} level="H" />
                </div>
                <p className="qr-code-id">Student ID: {studentId}</p>
                <p className="qr-code-refresh">Code refreshes every minute for security</p>
              </div>

              <div className="qr-code-instructions">
                <h3>How to use:</h3>
                <ol>
                  <li>Show this QR code to your instructor or scan at the attendance station</li>
                  <li>Ensure your code is scanned within the valid time window</li>
                  <li>Verify your attendance status on the dashboard</li>
                </ol>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

export default QRCodePage

