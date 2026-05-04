export default function Button({ children, onClick }) {
  return (
    <button
      onClick={onClick}
      className="h-11 px-4 bg-primary text-white rounded-xl shadow hover:bg-blue-900 transition"
    >
      {children}
    </button>
  );
}