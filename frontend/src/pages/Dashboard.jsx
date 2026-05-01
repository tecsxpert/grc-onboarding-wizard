import { useEffect, useState } from "react";
import API from "../services/api";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

function Dashboard() {
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    deleted: 0,
  });

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const res = await API.get("/onboarding/stats");
      setStats(res.data);
    } catch (err) {
      console.error("Error fetching stats:", err);
    }
  };

  // ✅ Chart Data
  const chartData = [
    { name: "Total", value: stats.total },
    { name: "Active", value: stats.active },
    { name: "Deleted", value: stats.deleted },
  ];

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6 text-center">
        Dashboard
      </h1>

      {/* KPI CARDS */}
      <div className="grid grid-cols-3 gap-6">
        <div className="bg-blue-200 p-6 rounded shadow text-center">
          <h2 className="text-lg font-semibold">Total</h2>
          <p className="text-2xl">{stats.total}</p>
        </div>

        <div className="bg-green-200 p-6 rounded shadow text-center">
          <h2 className="text-lg font-semibold">Active</h2>
          <p className="text-2xl">{stats.active}</p>
        </div>

        <div className="bg-red-200 p-6 rounded shadow text-center">
          <h2 className="text-lg font-semibold">Deleted</h2>
          <p className="text-2xl">{stats.deleted}</p>
        </div>
      </div>

      {/* 🔥 CHART */}
      <div className="mt-10 flex justify-center">
        <BarChart width={500} height={300} data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="value" />
        </BarChart>
      </div>
    </div>
  );
}

export default Dashboard;