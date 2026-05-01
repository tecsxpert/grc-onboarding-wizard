import { useEffect, useState } from "react";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";

export default function ToolList() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  // ✅ Correct way to get token
  const token = localStorage.getItem("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3Nzc2MTQzMjksImV4cCI6MTc3NzYxNzkyOX0.8_v8w3i59BiOeCTU2o8b_bwPugoBXkwMQQcJFXQ3fGs");

  useEffect(() => {
  const token = localStorage.getItem("token");

  fetch("http://localhost:8081/api/auth/users", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((res) => {
      console.log("DATA:", res);
      setData(res.content || res || []);
      setLoading(false);
    })
    .catch(() => {
      setError(true);
      setLoading(false);
    });
},  [token]);

  if (loading) return <Skeleton />;
  if (error) return <p>Failed to load data</p>;
  if (data.length === 0) return <EmptyState />;

  return (
    <div>
      <h2>User List</h2>
      {data.map((user) => (
        <div key={user.id}>
          <h4>{user.name}</h4>
          <p>{user.email}</p>
        </div>
      ))}
    </div>
  );
}