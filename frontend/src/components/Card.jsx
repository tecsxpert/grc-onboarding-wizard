export default function Card({ user }) {
  return (
    <div className="card">
      <div style={{ display: "flex", gap: "12px", alignItems: "center" }}>
        <div className="avatar">{user.name[0]}</div>
        <div>
          <div style={{ fontWeight: "bold" }}>{user.name}</div>
          <div style={{ color: "gray" }}>{user.email}</div>
        </div>
      </div>

      <button>View</button>
    </div>
  );
}