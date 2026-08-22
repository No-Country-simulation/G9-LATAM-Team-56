import { Icon } from "@iconify/react";
import { useNavigate } from "react-router-dom";

function NotFound() {
  const navigate = useNavigate();

  return (
    <div style={{
      position: "fixed",
      top: 0,
      left: 0,
      width: "100vw",
      height: "100vh",
      backgroundColor: "rgba(0, 0, 0, 0.8)",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      zIndex: 9999,
      backdropFilter: "blur(4px)"
    }}>
      <div style={{
        backgroundColor: "#1e293b",
        padding: "30px",
        borderRadius: "12px",
        maxWidth: "450px",
        width: "90%",
        textAlign: "center",
        boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.5)",
        border: "1px solid #334155",
        color: "#f8fafc",
        fontFamily: "inherit"
      }}>
        <Icon icon="mdi:alert-circle-outline" width="48" style={{ color: "#ef4444", marginBottom: "15px" }} />
        <h3 style={{ fontSize: "1.25rem", marginBottom: "10px", fontWeight: "bold" }}>Página No Encontrada</h3>
        <p style={{ color: "#94a3b8", fontSize: "0.95rem", lineHeight: "1.5", marginBottom: "25px" }}>
          La ruta a la que intentas acceder no existe en la plataforma. Serás redirigido a tu panel principal.
        </p>
        <button
          onClick={() => navigate("/dashboard")}
          style={{
            backgroundColor: "#2563eb",
            color: "#ffffff",
            border: "none",
            padding: "10px 20px",
            borderRadius: "6px",
            fontSize: "0.95rem",
            fontWeight: "600",
            cursor: "pointer",
            width: "100%",
            transition: "background-color 0.2s"
          }}
          onMouseOver={(e) => e.target.style.backgroundColor = "#1d4ed8"}
          onMouseOut={(e) => e.target.style.backgroundColor = "#2563eb"}
        >
          Volver al Dashboard
        </button>
      </div>
    </div>
  );
}

export default NotFound;