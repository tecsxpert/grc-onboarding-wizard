import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // ✅ NEW
import API from "../services/api";

function OnboardingPage() {
  const navigate = useNavigate(); // ✅ NEW

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    role: "",
    description: "",
  });

  const [dataList, setDataList] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  // ✅ FETCH DATA
  const fetchData = async () => {
    try {
      setLoading(true);
      const res = await API.get(`/onboarding?page=${page}&size=5`);
      setDataList(res.data.content || []);
    } catch (err) {
      console.error("Fetch error:", err);
      setDataList([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [page]);

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
      await API.post("/onboarding", formData);

      setFormData({
        name: "",
        email: "",
        role: "",
        description: "",
      });

      fetchData();
    } catch (error) {
      console.error("Submit error:", error);
    }
  };

  return (
    <div className="p-6">
      {/* FORM */}
      <div className="bg-white p-6 rounded shadow w-96 mx-auto">
        <h2 className="text-xl font-bold mb-4 text-center">
          Onboarding Form
        </h2>

        <input
          name="name"
          placeholder="Name"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.name}
        />

        <input
          name="email"
          placeholder="Email"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.email}
        />

        <input
          name="role"
          placeholder="Role"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.role}
        />

        <textarea
          name="description"
          placeholder="Description"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.description}
        />

        <button
          className="w-full bg-blue-500 text-white p-2 rounded"
          onClick={handleSubmit}
        >
          Submit
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
              {Array.isArray(dataList) && dataList.length > 0 ? (
                dataList.map((item) => (
                  <tr
                    key={item.id}
                    onClick={() => navigate(`/detail/${item.id}`)} // ✅ CLICK NAVIGATION
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