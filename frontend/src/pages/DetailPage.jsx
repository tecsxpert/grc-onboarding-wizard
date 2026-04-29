import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../services/api";

function DetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState({});

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const res = await API.get(`/onboarding?page=0&size=100`);
    const item = res.data.content.find((x) => x.id == id);
    setData(item);
  };

  const handleDelete = async () => {
    await API.delete(`/onboarding/${id}`);
    navigate("/home");
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Detail Page</h1>

      <div className="border p-4 rounded shadow w-96">

        <p><b>Name:</b> {data.name}</p>
        <p><b>Email:</b> {data.email}</p>
        <p><b>Role:</b> {data.role}</p>

        {/* 🔥 SCORE BADGE (simple) */}
        <div className="mt-3">
          <span className="bg-blue-500 text-white px-3 py-1 rounded">
            Score: {data.id}
          </span>
        </div>

        {/* BUTTONS */}
        <div className="mt-4 flex gap-3">
          <button
            className="bg-yellow-400 px-3 py-1"
            onClick={() => navigate("/home")}
          >
            Edit
          </button>

          <button
            className="bg-red-500 text-white px-3 py-1"
            onClick={handleDelete}
          >
            Delete
          </button>
        </div>

      </div>
    </div>
  );
}

export default DetailPage;