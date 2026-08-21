import { Icon } from '@iconify/react';
import { useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom'; // Importamos hooks de navegación
import logo from '../assets/img/logo-financeai.jpg';
import './Sidebar.css';

function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation(); // Nos permite saber en qué ruta estamos actualmente

  // Estado para controlar la visibilidad del Pop-up de advertencia de CSV
  const [mostrarModalCsv, setMostrarModalCsv] = useState(false);

  // Definimos las rutas (path) para cada ítem del menú
  const navItems = [
    { label: 'Dashboard', icon: 'mdi:view-dashboard-outline', path: '/dashboard' },
    { label: 'Estadisticas Avanzadas', icon: 'mdi:chart-line', path: '/estadisticas' },
    { label: 'Recomendaciones', icon: 'mdi:lightbulb-outline', path: '/recomendaciones' },
    { label: 'Perfil', icon: 'mdi:account-outline', path: '/perfil' },
  ];

  // Función para manejar la navegación al hacer clic en un enlace
  const handleNavigation = (path, e) => {
    e.preventDefault();
    // Verificamos si estamos actualmente en la vista de Perfil
    const estoyEnPerfil = location.pathname === '/perfil';

    // Verificamos si ya se cargó un CSV válido (puedes guardar esta bandera en localStorage al subir el archivo con éxito)
    const csvCargado = localStorage.getItem("csvCargado") === "true";

    // Si estoy en perfil y quiero ir a otra vista sin haber cargado un CSV válido, bloqueo e interrumpo la navegación
    if (estoyEnPerfil && !csvCargado && path !== '/perfil') {
      setMostrarModalCsv(true);
      return;
    }
    navigate(path);
  };

  // Función para cerrar sesión
  const handleLogout = (e) => {
    e.preventDefault();
    // Limpiamos los datos del usuario guardados en el navegador
    localStorage.removeItem("usuarioNombre");
    localStorage.removeItem("usuarioEmail");
    localStorage.removeItem("csvCargado");

    // Redirigimos al Login
    navigate("/");
  };

  return (
    <>
      <aside className="sidebar">
        <div className="sidebar-logo">
          <img src={logo} alt="financeAI" />
        </div>

        <nav className="sidebar-nav">
          {navItems.map((item) => {
            // Verificamos si la ruta actual coincide con la del ítem para marcarlo como activo
            const isActive = location.pathname === item.path;

            return (
              <a
                href={item.path}
                key={item.label}
                className={`sidebar-link ${isActive ? 'active' : ''}`}
                onClick={(e) => handleNavigation(item.path, e)}
              >
                <Icon icon={item.icon} width="20" />
                <span>{item.label}</span>
              </a>
            );
          })}
        </nav>

        <div className="sidebar-footer">
          <a
            href="#logout"
            className="sidebar-link"
            onClick={handleLogout} // Vinculamos la función de cierre de sesión
          >
            <Icon icon="mdi:logout" width="20" />
            <span>Log out</span>
          </a>
        </div>
      </aside>
      {/* Pop-up Pop-up estilizado persistente si intenta navegar sin CSV desde Perfil */}
        {mostrarModalCsv && (
          <div style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0, 0, 0, 0.75)",
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
              <Icon icon="mdi:file-document-alert-outline" width="48" style={{ color: "#38bdf8", marginBottom: "15px" }} />
              <h3 style={{ fontSize: "1.25rem", marginBottom: "10px", fontWeight: "bold" }}>Acción Requerida</h3>
              <p style={{ color: "#94a3b8", fontSize: "0.95rem", lineHeight: "1.5", marginBottom: "25px" }}>
                Para continuar con la navegación y habilitar las funciones del sistema, por favor cargue un documento CSV válido con las transacciones del usuario.
              </p>
              <button
                onClick={() => setMostrarModalCsv(false)}
                style={{
                  backgroundColor: "#0ea5e9",
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
                onMouseOver={(e) => e.target.style.backgroundColor = "#0284c7"}
                onMouseOut={(e) => e.target.style.backgroundColor = "#0ea5e9"}
              >
                Entendido
              </button>
            </div>
          </div>
        )}
    </>
  );
}

export default Sidebar;