import { Icon } from "@iconify/react";
import { useNavigate } from "react-router-dom"; // Importar el hook de navegación
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import "./Dashboard.css";

import { PieChartWithNeedle } from "../components/PieChartWithNeedle";
import { GraficoGastos } from "../components/GraficoGastos";
import {
  COLORS_GASTOS,
  obtenerIconoCategoria,
  obtenerEstadoSalud
} from "./dashboardData.mock";

// Datos de prueba (sustituir por llamados Axios/Fetch a la API de Spring Boot)
import {
  GAUGE_MOCK,
  KPIS_MOCK,
  GASTOS_MOCK,
  TRANSACCIONES_MOCK,
  RECOMENDACIONES_MOCK,
} from "./dashboardData.mock";


// Helper para dar formato a la fecha
const formatearFecha = (fechaStr) => {
  if (!fechaStr || typeof fechaStr !== "string") return "";
  const partes = fechaStr.split("-");
  if (partes.length === 3) {
    const [year, month, day] = partes;
    return `${day}/${month}/${year}`;
  }
  return fechaStr;
};

//-------------------------------------------------------------------------------------------------

function Dashboard() {
  const estadoSalud = obtenerEstadoSalud(GAUGE_MOCK.scoreValue);
  const navigate = useNavigate(); // Inicializar la función de navegación

  // Función que maneja el evento al hacer clic en el botón
    const handleVerDetalle = () => {
        navigate("/recomendaciones");
      };

  return (
    <div className="dashboard-layout">
      <Sidebar />

      <main className="dashboard-main">
        <div className="dashboard-dark-bg">
          <header className="dashboard-topbar">
            <div className="search-box">
              <input
                type="search"
                placeholder="¿Qué deseas buscar?"
                aria-label="Buscar"
              />
              <Icon icon="mdi:magnify" width="20" className="search-icon" aria-hidden="true" />
            </div>
            <UserBadge />
          </header>

          <section className="dashboard-hero">
            <h1>Dashboard</h1>
            <h2>Tu Resumen Financiero</h2>
          </section>

          <section className="dashboard-grid">
            {/* COLUMNA 1: Perfil Financiero */}
            <article className="card-container profile-card">
              <div className="profile-top-section">
                <header className="card-header-plain">
                  <h3>PERFIL FINANCIERO</h3>
                </header>

                <PieChartWithNeedle
                  scoreValue={GAUGE_MOCK.scoreValue}
                  gaugeData={GAUGE_MOCK.gaugeData}
                />
              </div>

              <div className="profile-bottom-section">
                <div className="score-section">
                  <h3>Score: {GAUGE_MOCK.scoreValue}/100</h3>
                  <p>
                    Tu salud es:{" "}
                    <span className={estadoSalud.claseCss}>
                      {estadoSalud.texto}
                    </span>
                  </p>
                </div>

                <div className="kpi-buttons-grid">
                  <div className="kpi-blue-button">
                    <div className="kpi-icon-badge">
                      <Icon icon="mdi:account-reactivate" width="24" aria-hidden="true" />
                    </div>
                    <span>Perfil:</span>
                    <strong>{KPIS_MOCK.perfil}</strong>
                  </div>

                  <div className="kpi-blue-button">
                    <div className="kpi-icon-badge">
                      <Icon icon="ph:chart-pie-fill" width="24" aria-hidden="true" />
                    </div>
                    <span>Probabilidad:</span>
                    <strong>{KPIS_MOCK.probabilidad}%</strong>
                  </div>

                  <div className="kpi-blue-button">
                    <div className="kpi-icon-badge">
                      <Icon icon="mdi:sack-percent" width="24" aria-hidden="true" />
                    </div>
                    <span>Nivel de endeudamiento:</span>
                    <strong>{KPIS_MOCK.endeudamiento}%</strong>
                  </div>
                </div>
              </div>
            </article>

            {/* COLUMNA 2: Resumen de Gastos y Transacciones */}
            <div className="dashboard-center-column">
              <article className="card-container">
                <header className="card-header-light">
                  <h3>RESUMEN DE GASTOS</h3>
                </header>

                <GraficoGastos datos={GASTOS_MOCK} colores={COLORS_GASTOS} />
              </article>

              <article className="card-container">
                <header className="card-header-light">
                  <h3>TRANSACCIONES RECIENTES</h3>
                </header>

                <div className="transactions-list" role="table" aria-label="Transacciones recientes">
                  <div className="tx-header transaction-item" role="row">
                    <span role="columnheader">Fecha</span>
                    <span className="tx-header-cat" role="columnheader">Categoría</span>
                    <span className="tx-header-val" role="columnheader">Valor</span>
                  </div>

                  {TRANSACCIONES_MOCK.map((item) => {
                    const nombreCat = item.categoria || item.descripcion || item.nombre || "";
                    const valorMonto = typeof item.monto !== "undefined" ? item.monto : item.valor;

                    return (
                      <div key={item.id} className="tx-grid transaction-item" role="row">
                       <span className="tx-date" role="cell">
                                                 {formatearFecha(item.fecha)}
                                               </span>
                        <div className="tx-info" role="cell">
                          <Icon
                            icon={obtenerIconoCategoria(nombreCat)}
                            width="20"
                            className="tx-icon"
                            aria-hidden="true"
                          />
                          <span>{nombreCat}</span>
                        </div>
                        <span className="tx-amount" role="cell">
                          ${Number(valorMonto || 0).toFixed(2)}
                        </span>
                      </div>
                    );
                  })}
                </div>
              </article>
            </div>

            {/* COLUMNA 3: Recomendaciones */}
            <article className="card-container recommendations-card">
              <h3>RECOMENDACIONES</h3>

              {RECOMENDACIONES_MOCK.slice(0, 2).map((rec) => (
                <div key={rec.id} className="recommendation-card-item">
                  <div className="rec-card-top">
                    <div className="rec-icon-badge">
                      <Icon
                        icon={obtenerIconoCategoria(rec.categoria)}
                        width="24"
                        height="24"
                        aria-hidden="true"
                      />
                    </div>
                    <p className="rec-text">{rec.texto}</p>
                  </div>

                  <div className="rec-card-bottom">
                    {/* Añadido el evento onClick llamando a la función de redirección */}
                    <button type="button" className="btn-green-full" onClick={handleVerDetalle}>
                      Ver detalles
                    </button>
                  </div>
                </div>
              ))}
            </article>
          </section>
        </div>
      </main>
    </div>
  );
}

export default Dashboard;