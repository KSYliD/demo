import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AdminReviewsPage from "./pages/AdminReviewsPage";

function App() {
    return (
        <Router>
            <div className="min-h-screen bg-gray-50">
                <Routes>
                    {/* Головна сторінка адмін-панелі */}
                    <Route path="/" element={<AdminReviewsPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
