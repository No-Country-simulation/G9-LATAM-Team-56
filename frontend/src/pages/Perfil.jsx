import { Icon } from "@iconify/react";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import "./Perfil.css";

function Perfil() {
  const cuentaInfo = [
    { label: "Moneda", value: "USD Dólar", icon: "mdi:currency-usd" },
    { label: "Ingreso Mensual", value: "$2,000", icon: "mdi:cash" },
    { label: "Saldo Total", value: "$1,250", icon: "mdi:wallet-outline" },
    {
      label: "Nivel de Endeudamiento",
      value: "25%",
      icon: "mdi:percent-outline",
    },
  ];

  return (
    <div className="perfil-layout">
      <Sidebar />

      <div className="perfil-main">
        <div className="perfil-dark-bg">
          <header className="perfil-topbar">
            <div className="search-box">
              <Icon icon="mdi:magnify" width="18" />
              <input type="text" placeholder="¿qué deseas buscar?" />
            </div>

            <UserBadge />
          </header>

          <section className="perfil-hero">
            <h1>Perfil</h1>
            <p>Administra tu cuenta</p>
          </section>

          <section className="perfil-content">
            <div className="perfil-card">
              <div className="perfil-card-left">
                <div className="avatar-placeholder">
                  <Icon icon="mdi:account-circle" width="48" />
                </div>
                <div>
                  <p className="user-name">Marcela G.</p>
                  <p className="user-email">marcela.g@finance.dev</p>
                </div>
              </div>
              <div className="perfil-card-right">
                <button className="btn-primary">Editar Perfil</button>
              </div>
            </div>

            <div className="perfil-card">
              <div className="perfil-card-left">
                <ul className="perfil-info-list">
                  {cuentaInfo.map((item) => (
                    <li key={item.label}>
                      <Icon icon={item.icon} width="18" />
                      <span className="info-label">{item.label}</span>
                      <span className="info-value">{item.value}</span>
                    </li>
                  ))}
                </ul>
              </div>
              <div className="perfil-card-right">
                <button className="btn-primary">Importar CSV</button>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default Perfil;
