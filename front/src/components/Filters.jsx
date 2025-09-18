// src/components/Filters.jsx
import { useState } from "react";

export default function Filters({ onFilter }) {
    const [branch, setBranch] = useState("");
    const [position, setPosition] = useState("");
    const [level, setLevel] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        onFilter({ branch, position, level });
    };

    return (
        <form onSubmit={handleSubmit} className="filters-form">
            <input
                type="text"
                placeholder="Branch"
                value={branch}
                onChange={(e) => setBranch(e.target.value)}
            />
            <input
                type="text"
                placeholder="Position"
                value={position}
                onChange={(e) => setPosition(e.target.value)}
            />
            <select value={level} onChange={(e) => setLevel(e.target.value)}>
                <option value="">All Levels</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select>
            <button type="submit">Filter</button>
        </form>
    );
}
