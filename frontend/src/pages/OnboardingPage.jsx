import { useState } from "react";
import API from "../services/api"; // axios instance

function OnboardingPage() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    role: "",
    description: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async () => {
    try {
      const response = await API.post("/onboarding", formData);
      console.log("Success:", response.data);

      // optional: clear form after submit
      setFormData({
        name: "",
        email: "",
        role: "",
        description: "",
      });

    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded-xl shadow-md w-96">
        <h2 className="text-2xl font-bold mb-4 text-center">
          Onboarding Form
        </h2>

        <input
          type="text"
          name="name"
          placeholder="Name"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.name}
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={formData.email}
        />

        <input
          type="text"
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
    </div>
  );
}

export default OnboardingPage;