import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Login from "./Login"
import Dashboard from "./Dashboard"
import QRCodePage from "./QRCodePage"

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/dashboard/*" element={<Dashboard />} />
                <Route path="/dashboard/qr-code" element={<QRCodePage />} />
            </Routes>
        </Router>
    )
}

export default App
