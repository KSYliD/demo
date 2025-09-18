// src/components/ReviewTable.jsx
export default function ReviewTable({ reviews }) {
    if (!reviews || reviews.length === 0) {
        return <p className="no-reviews">No reviews available.</p>;
    }

    return (
        <div className="review-table-container">
            <table className="review-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Branch</th>
                    <th>Position</th>
                    <th>Text</th>
                    <th>Sentiment</th>
                    <th>Criticality</th>
                    <th>Suggested Solution</th>
                    <th>Created At</th>
                    <th>Google Doc</th>
                    <th>Trello</th>
                </tr>
                </thead>
                <tbody>
                {reviews.map((r) => (
                    <tr key={r.id}>
                        <td className={r.id ? "" : "empty"}>{r.id ?? "—"}</td>
                        <td className={r.branchName ? "" : "empty"}>{r.branchName ?? "—"}</td>
                        <td className={r.role ? "" : "empty"}>{r.role ?? "—"}</td>
                        <td className={r.text ? "" : "empty"}>{r.text ?? "—"}</td>
                        <td className={r.sentiment ? "" : "empty"}>{r.sentiment ?? "—"}</td>
                        <td className="criticality">{r.criticality ?? "—"}</td>
                        <td className={r.suggestedSolution ? "" : "empty"}>{r.suggestedSolution ?? "—"}</td>
                        <td className={r.createdAt ? "" : "empty"}>
                            {r.createdAt ? new Date(r.createdAt).toLocaleString("uk-UA") : "—"}
                        </td>
                        <td>
                            {r.googleDocUrl ? (
                                <a href={r.googleDocUrl} target="_blank" rel="noreferrer">Google Doc</a>
                            ) : "—"}
                        </td>
                        <td>
                            {r.trelloCardUrl ? (
                                <a href={r.trelloCardUrl} target="_blank" rel="noreferrer">Trello</a>
                            ) : "—"}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
