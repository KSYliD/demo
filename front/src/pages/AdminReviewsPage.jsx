// src/pages/AdminReviewsPage.jsx
import { useEffect, useState } from "react";
import { getReviews } from "../api/reviews";
import Filters from "../components/Filters";
import ReviewTable from "../components/ReviewTable";

export default function AdminReviewsPage() {
    const [reviews, setReviews] = useState([]);
    const [filters, setFilters] = useState({ branch: "", position: "", level: "" });

    useEffect(() => {
        fetchReviews();
    }, [filters]);

    const fetchReviews = async () => {
        try {
            const data = await getReviews({ ...filters, page: 0, size: 20 });
            console.log("Fetched reviews:", data);
            setReviews(Array.isArray(data) ? data : data.content || []);
        } catch (error) {
            console.error("Failed to fetch reviews:", error);
        }
    };

    return (
        <div className="admin-page">
            <h1>Admin Reviews Panel</h1>
            <Filters onFilter={setFilters} />
            <ReviewTable reviews={reviews} />
        </div>
    );
}
