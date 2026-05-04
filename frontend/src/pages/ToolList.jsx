import { useEffect, useState } from "react";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";
import Card from "../components/Card";

export default function ToolList() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(false);

  const fetchUsers = () => {
    const token = localStorage.getItem("token");

    if (!token) {
      setError(true);
      setData([]);
      return;
    }

    fetch("http://localhost:8081/api/auth/users", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((res) => {
        setData(res.content || res || []);
        setError(false);
      })
      .catch(() => {
        setError(true);
        setData([]);
      });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  if (data === null) {
    return (
      <div className="container">
        {[...Array(4)].map((_, i) => (
          <Skeleton key={i} />
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="container" style={{ textAlign: "center" }}>
        <p>Error loading users</p>
        <button onClick={fetchUsers}>Retry</button>
      </div>
    );
  }

  if (data.length === 0) return <EmptyState />;

  return (
    <div className="container">
      <h2>User List ({data.length})</h2>
      {data.map((user) => (
        <Card key={user.id} user={user} />
      ))}
    </div>
  );
}