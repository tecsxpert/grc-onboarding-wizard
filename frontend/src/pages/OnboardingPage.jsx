import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import API from "../services/api";

function OnboardingPage() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    role: "",
    description: "",
  });

  const [dataList, setDataList] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // 🔥 NEW FILTER STATES
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [status, setStatus] = useState("all");

  // ✅ FETCH LIST
  const fetchData = async () => {
    try {
      setLoading(true);

      let url = `/onboarding?page=${page}&size=5`;

      // 🔥 DATE FILTER
      if (startDate && endDate) {
        url += `&startDate=${startDate}&endDate=${endDate}`;
      }

      const res = await API.get(url);
      let data = res.data.content || [];

      // 🔥 STATUS FILTER (frontend)
      if (status === "active") {
        data = data.filter((item) => !item.deleted);
      } else if (status === "deleted") {
        data = data.filter((item) => item.deleted);
      }

      setDataList(data);

    } catch (err) {
      console.error("Fetch error:", err);
      setDataList([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [page, startDate, endDate, status]);

  // ✅ LOAD DATA FOR EDIT
  useEffect(() => {
    if (id) {
      API.get(`/onboarding/${id}`)
        .then((res) => {
          setFormData(res.data);
          setEditingId(id);
        })
        .catch(() => {
          console.error("Error loading edit data");
        });
    }
  }, [id]);

  // ✅ INPUT CHANGE
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  // ✅ SUBMIT
  const handleSubmit = async () => {
    try {
      if (editingId) {
        await API.put(`/onboarding/${editingId}`, formData);
        alert("Updated successfully");
      } else {
        await API.post("/onboarding", formData);
        alert("Created successfully");
      }

      setFormData({
        name: "",
        email: "",
        role: "",
        description: "",
      });

      setEditingId(null);
      fetchData();
      navigate("/home");

    } catch (error) {
      console.error("Submit error:", error);
    }
  };

  return (
    <div className="p-6">

      {/* 🔥 FILTER SECTION */}
      <div className="flex gap-3 mb-4">

        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="border p-2"
        />

        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          className="border p-2"
        />

        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="border p-2"
        >
          <option value="all">All</option>
          <option value="active">Active</option>
          <option value="deleted">Deleted</option>
        </select>

      </div>

      {/* FORM */}
      <div className="bg-white p-6 rounded shadow w-96 mx-auto">
        <h2 className="text-xl font-bold mb-4 text-center">
          {editingId ? "Edit User" : "Onboarding Form"}
        </h2>

        <input
          name="name"
          placeholder="Name"
          className="w-full p-2 mb-3 border rounded"
          value={formData.name}
          onChange={handleChange}
        />

        <input
          name="email"
          placeholder="Email"
          className="w-full p-2 mb-3 border rounded"
          value={formData.email}
          onChange={handleChange}
        />

        <input
          name="role"
          placeholder="Role"
          className="w-full p-2 mb-3 border rounded"
          value={formData.role}
          onChange={handleChange}
        />

        <textarea
          name="description"
          placeholder="Description"
          className="w-full p-2 mb-3 border rounded"
          value={formData.description}
          onChange={handleChange}
        />

        <button
          className="w-full bg-blue-500 text-white p-2 rounded"
          onClick={handleSubmit}
        >
          {editingId ? "Update" : "Submit"}
        </button>
      </div>

      {/* TABLE */}
      <div className="mt-8">
        <h2 className="text-xl font-bold mb-4">Onboarding List</h2>

        {loading ? (
          <p>Loading...</p>
        ) : (
          <table className="border w-full">
            <thead>
              <tr>
                <th className="border p-2">Name</th>
                <th className="border p-2">Email</th>
                <th className="border p-2">Role</th>
              </tr>
            </thead>

            <tbody>
              {dataList.length > 0 ? (
                dataList.map((item) => (
                  <tr
                    key={item.id}
                    onClick={() => navigate(`/detail/${item.id}`)}
                    className="cursor-pointer hover:bg-gray-100"
                  >
                    <td className="border p-2">{item.name}</td>
                    <td className="border p-2">{item.email}</td>
                    <td className="border p-2">{item.role}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3" className="text-center p-4">
                    No Data Found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}

        {/* PAGINATION */}
        <div className="mt-4 flex gap-4">
          <button
            className="bg-gray-300 px-3 py-1"
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
          >
            Prev
          </button>

          <button
            className="bg-gray-300 px-3 py-1"
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
}

export default OnboardingPage;