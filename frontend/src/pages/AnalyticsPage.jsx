import { useEffect, useState } from "react";
import API from "../services/api";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid,
  PieChart, Pie, Cell
} from "recharts";

function AnalyticsPage() {

  const [data, setData] = useState([]);
  const [stats, setStats] = useState({});
  const [period, setPeriod] = useState("all");

  useEffect(() => {
    fetchData();
    fetchStats();
  }, [period]);

  const fetchData = async () => {

    let url = "/onboarding?page=0&size=100";

    if (period !== "all") {
      const today = new Date();
      const past = new Date();

      past.setDate(today.getDate() - parseInt(period));

      const start = past.toISOString().split("T")[0];
      const end = today.toISOString().split("T")[0];

      url += `&startDate=${start}&endDate=${end}`;
    }

    const res = await API.get(url);

    const roleMap = {};

    res.data.content.forEach(item => {
      roleMap[item.role] = (roleMap[item.role] || 0) + 1;
    });

    const chartData = Object.keys(roleMap).map(role => ({
      role,
      count: roleMap[role]
    }));

    setData(chartData);
  };

  const fetchStats = async () => {
    const res = await API.get("/onboarding/stats");
    setStats(res.data);
  };

  const COLORS = ["#4f46e5", "#22c55e", "#f97316", "#ef4444"];

  return (
    <div className="p-6">

      <h1 className="text-2xl font-bold mb-4">Analytics</h1>

      {/* 🔥 STATS CARDS */}
      <div className="grid grid-cols-3 gap-4 mb-6">

        <div className="bg-blue-500 text-white p-4 rounded shadow">
          <h2>Total</h2>
          <p className="text-xl">{stats.total}</p>
        </div>

        <div className="bg-green-500 text-white p-4 rounded shadow">
          <h2>Active</h2>
          <p className="text-xl">{stats.active}</p>
        </div>

        <div className="bg-red-500 text-white p-4 rounded shadow">
          <h2>Deleted</h2>
          <p className="text-xl">{stats.deleted}</p>
        </div>

      </div>

      {/* PERIOD */}
      <select
        className="border p-2 mb-4 rounded"
        value={period}
        onChange={(e) => setPeriod(e.target.value)}
      >
        <option value="all">All</option>
        <option value="7">Last 7 Days</option>
        <option value="30">Last 30 Days</option>
      </select>

      {/* 🔥 CHARTS GRID */}
      <div className="grid grid-cols-2 gap-6">

        {/* BAR CHART */}
        <div className="bg-white p-4 rounded shadow">
          <h2 className="mb-3 font-semibold">Role Distribution</h2>

          <div style={{ width: "100%", height: 300 }}>
            <ResponsiveContainer>
              <BarChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="role" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="count" fill="#4f46e5" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* 🔥 PIE CHART */}
        <div className="bg-white p-4 rounded shadow">
          <h2 className="mb-3 font-semibold">Role Breakdown</h2>

          <div style={{ width: "100%", height: 300 }}>
            <ResponsiveContainer>
              <PieChart>
                <Pie
                  data={data}
                  dataKey="count"
                  nameKey="role"
                  outerRadius={100}
                  label
                >
                  {data.map((entry, index) => (
                    <Cell key={index} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

      </div>

    </div>
  );
}

export default AnalyticsPage;