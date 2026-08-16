import { Icon } from '@iconify/react';
import logo from '../assets/img/logo-financeai.jpg';
import './Sidebar.css';

function Sidebar() {
  const navItems = [
    { label: 'Dashboard', icon: 'mdi:view-dashboard-outline'  },
    { label: 'Estadisticas Avanzadas', icon: 'mdi:chart-line' },
    { label: 'Recomendaciones', icon: 'mdi:lightbulb-outline'},
    { label: 'Perfil', icon: 'mdi:account-outline'},
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <img src={logo} alt="financeAI" />
      </div>

      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <a
            href="#"
            key={item.label}
            className={`sidebar-link ${item.active ? 'active' : ''}`}
          >
            <Icon icon={item.icon} width="20" />
            <span>{item.label}</span>
          </a>
        ))}
      </nav>

      <div className="sidebar-footer">
        <a href="#" className="sidebar-link">
          <Icon icon="mdi:logout" width="20" />
          <span>Log out</span>
        </a>
      </div>
    </aside>
  );
}

export default Sidebar;
