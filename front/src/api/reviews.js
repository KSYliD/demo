import axios from "axios";

const API_URL = "http://localhost:8080/admin/feedback";

export const getReviews = async ({ branch, position, level }) => {
    const params = {
        branch: branch || null,
        position: position || null,
        criticalityLevel: level || null,
    };
    const response = await axios.get(API_URL, { params });
    return response.data;
};


