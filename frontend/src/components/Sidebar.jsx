import { Icon } from '@iconify/react';
import { useNavigate, useLocation } from 'react-router-dom'; // 👈 Importamos hooks de navegación
import logo from '../assets/img/logo-financeai.jpg';
import './Sidebar.css';

function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation(); // 👈 Nos permite saber en qué ruta estamos actualmente

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
    navigate(path);
  };

  // Función para cerrar sesión
  const handleLogout = (e) => {
    e.preventDefault();
    // Limpiamos los datos del usuario guardados en el navegador
    localStorage.removeItem("usuarioNombre");
    localStorage.removeItem("usuarioEmail");

    // Redirigimos al Login
    navigate("/");
  };

  return (
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
          onClick={handleLogout} // 👈 Vinculamos la función de cierre de sesión
        >
          <Icon icon="mdi:logout" width="20" />
          <span>Log out</span>
        </a>
      </div>
    </aside>
  );
}

export default Sidebar;