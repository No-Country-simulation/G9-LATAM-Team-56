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
  obtenerEstadoSalud,
  obtenerGaugeData
} from "./dashboardData.mock";

import { useEffect, useState } from "react";
import { obtenerDashboard } from "../../services/api";

// Datos de prueba (sustituir por llamados Axios/Fetch a la API de Spring Boot)
import {
  GAUGE_MOCK
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
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;

    const cargarDashboard = async () => {
      try {
        const usuario = localStorage.getItem("usuarioNombre");

        if (!usuario) {
          throw new Error("No se encontró el usuario");
        }

        const data = await obtenerDashboard(usuario);

        // Solo actualizar el estado si el componente sigue montado
        if (isMounted) {
          setDashboardData(data);
        }
      } catch (err) {
        console.error("Error al cargar dashboard:", err);
        if (isMounted) {
          setError(err.message);
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    cargarDashboard();

    // Función de limpieza
    return () => {
      isMounted = false;
    };
  }, []);

  if (loading) {
    return <div>Cargando dashboard...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!dashboardData) {
    return <div>No hay datos disponibles.</div>;
  }
  console.log("DATOS DEL DASHBOARD:", dashboardData);

  const obtenerEstadoSalud = (perfil = "") => {
    const perfilLimpio = perfil.trim().toLowerCase();

    if (perfilLimpio === "saludable") {
      return {
        texto: "Saludable",
        claseCss: "status-good"
      };
    }

    if (
      perfilLimpio === "en observación" ||
      perfilLimpio === "en observacion"
    ) {
      return {
        texto: "En Observación",
        claseCss: "status-warning"
      };
    }

    if (
      perfilLimpio === "en riesgo" ||
      perfilLimpio === "riesgo"
    ) {
      return {
        texto: "En Riesgo",
        claseCss: "status-bad"
      };
    }

    return {
      texto: "Sin determinar",
      claseCss: ""
    };
  };

  const estadoSalud = obtenerEstadoSalud(
    dashboardData.perfil_financiero
  );

  const gaugeData = obtenerGaugeData(
    dashboardData.perfil_financiero
  );

  const handleVerDetalle = () => {
    navigate("/recomendaciones");
  };

  const gastosGrafico =
    Object.entries(
      dashboardData.resumen_gastos
    ).map(([categoria, monto]) => ({
      categoria,
      monto
    }));



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
                  scoreValue={dashboardData.probabilidad * 100}
                  gaugeData={gaugeData}
                />
              </div>

              <div className="profile-bottom-section">
                <div className="score-section">
                  <h3>SALUD FINANCIERA</h3>

                  <p>
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
                    <span>Perfil</span>
                    <strong>
                      {dashboardData.perfil_financiero}
                    </strong>
                  </div>

                  <div className="kpi-blue-button">
                    <div className="kpi-icon-badge">
                      <Icon icon="ph:chart-pie-fill" width="24" aria-hidden="true" />
                    </div>
                    <span>Probabilidad</span>
                    <strong>
                      {(dashboardData.probabilidad * 100).toFixed(1)}%
                    </strong>
                  </div>

                  <div className="kpi-blue-button">
                    <div className="kpi-icon-badge">
                      <Icon icon="mdi:sack-percent" width="22" aria-hidden="true" />
                    </div>
                    <span>Endeudamiento</span>
                    <strong>
                      {dashboardData.nivel_endeudamiento}%
                    </strong>
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

                <GraficoGastos
                  datos={gastosGrafico}
                  colores={COLORS_GASTOS}
                />
              </article>

              <article className="card-container">
                <header className="card-header-light">
                  <h3>TRANSACCIONES RECIENTES</h3>
                </header>

                <div
                  className="transactions-list"
                  role="table"
                  aria-label="Transacciones recientes"
                >
                  <div className="tx-header" role="row">
                    <span className="tx-header-fech" role="columnheader">Fecha</span>
                    <span className="tx-header-cat" role="columnheader">Categoría</span>
                    <span className="tx-header-val" role="columnheader">Valor</span>
                  </div>

                  {dashboardData.transacciones.map((item, index) => (
                    <div
                      key={`${item.fecha}-${item.descripcion}-${index}`}
                      className="transaction-item"
                      role="row"
                    >
                      <span className="tx-date" role="cell">
                        {formatearFecha(item.fecha)}
                      </span>

                      <div className="tx-info" role="cell">
                        <Icon
                          icon={obtenerIconoCategoria(item.categoria)}
                          width="20"
                          height="20"
                          className="tx-icon"
                          aria-hidden="true"
                        />

                        <div className="tx-description">
                          <span>{item.categoria}</span>
                          <small>{item.descripcion}</small>
                        </div>
                      </div>

                      <span className="tx-amount" role="cell">
                        ${Number(item.valor || 0).toFixed(2)}
                      </span>
                    </div>
                  ))}
                </div>
              </article>
            </div>

            {/* COLUMNA 3: Recomendaciones */}
            <article className="card-container recommendations-card">
              <h3>RECOMENDACIONES</h3>
              {dashboardData.recomendaciones
                .slice(0, 2)
                .map((rec, index) => (

                  <div
                    key={index}
                    className="recommendation-card-item"
                  >

                    <div className="rec-card-top">

                      <div className="rec-icon-badge">
                        <Icon
                          icon={obtenerIconoCategoria(rec.categoria)}
                          width="24"
                        />
                      </div>

                      <p className="rec-text">
                        {rec.recomendacion}
                      </p>

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