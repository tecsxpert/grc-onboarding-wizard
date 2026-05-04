export default function Skeleton() {
  return (
    <div className="card">
      <div style={{ display: "flex", gap: "12px" }}>
        <div className="avatar"></div>
        <div>
          <div className="skeleton" style={{ width: "120px" }}></div>
          <div className="skeleton" style={{ width: "180px" }}></div>
        </div>
      </div>
    </div>
  );
}