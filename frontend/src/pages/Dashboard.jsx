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
  obtenerGaugeData
} from "./dashboardData.mock";

import { useEffect, useState } from "react";
import { obtenerDashboard } from "../../services/api";

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
          // En caso de error de red, cargamos un objeto seguro por defecto
          setDashboardData({
            perfil_financiero: "Sin determinar",
            probabilidad: 0,
            nivel_endeudamiento: 0,
            resumen_gastos: {},
            transacciones: [],
            recomendaciones: [],
          });
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
      texto: "Desconocido",
      claseCss: ""
    };
  };
  /**
 * Convierte el texto del perfil financiero a un valor numérico entre 0 y 100
 * para ubicar la aguja en el centro del rango correspondiente:
 * - En riesgo: centro en 16.6% (rango 0 a 33.3)
 * - En observación: centro en 50% (rango 33.3 a 66.6)
 * - Saludable: centro en 83.3% (rango 66.6 a 100)
 */
  const calcularScorePerfil = (perfil = "") => {
    const perfilLimpio = perfil.trim().toLowerCase();

    if (perfilLimpio === "en riesgo" || perfilLimpio === "riesgo") {
      return 16.6; // Apunta al medio de la zona roja
    }
    if (perfilLimpio === "en observación" || perfilLimpio === "en observacion") {
      return 50;   // Apunta al medio de la zona amarilla
    }
    if (perfilLimpio === "saludable") {
      return 83.3; // Apunta al medio de la zona verde
    }

    return null; // Muestra sin aguja si el valor es desconocido
  };

  // Uso seguro de datos con operador opcional (?.)
  const estadoSalud = obtenerEstadoSalud(dashboardData?.perfil_financiero);
  const gaugeData = obtenerGaugeData(dashboardData?.perfil_financiero);

  const handleVerDetalle = () => {
    navigate("/recomendaciones");
  };

  const gastosGrafico = dashboardData?.resumen_gastos
    ? Object.entries(dashboardData.resumen_gastos).map(([categoria, monto]) => ({
      categoria,
      monto
    }))
    : [];

  // Flag para saber si el perfil no está determinado y ocultar la aguja del gráfico
  const esSinDeterminar = !dashboardData?.perfil_financiero || dashboardData.perfil_financiero === "Desconocido" || dashboardData.probabilidad === 0;

  return (
    <div className="dashboard-layout" style={{ position: "relative" }}>

      {/* Pop-up persistente de bloqueo si no hay perfil y ya terminó de cargar */}
      {!loading && esSinDeterminar && (
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
            boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.5), 0 10px 10px -5px rgba(0, 0, 0, 0.04)",
            border: "1px solid #334155",
            fontFamily: "inherit",
            color: "#f8fafc"
          }}>
            <Icon icon="mdi:alert-circle-outline" width="48" style={{ color: "#f59e0b", marginBottom: "15px" }} />
            <h3 style={{ fontSize: "1.25rem", marginBottom: "10px", fontWeight: "bold" }}>Perfil Financiero No Detectado</h3>
            <p style={{ color: "#94a3b8", fontSize: "0.95rem", lineHeight: "1.5", marginBottom: "25px" }}>
              Dado que no existen registros actuales de su perfil financiero, es necesario configurar su cuenta para continuar utilizando la plataforma.
            </p>
            <button
              onClick={() => navigate("/perfil")}
              style={{
                backgroundColor: "#2563eb",
                color: "#ffffff",
                border: "none",
                padding: "10px 20px",
                borderRadius: "6px",
                fontSize: "0.95rem",
                fontWeight: "600",
                cursor: "pointer",
                transition: "background-color 0.2s",
                width: "100%"
              }}
              onMouseOver={(e) => e.target.style.backgroundColor = "#1d4ed8"}
              onMouseOut={(e) => e.target.style.backgroundColor = "#2563eb"}
            >
              Ir a la ventana Perfil
            </button>
          </div>
        </div>
      )}

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

          {/* Si está cargando, mostramos un contenedor de carga estilizado dentro del mismo layout sin romper el DOM */}
          {loading ? (
            <div style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              height: "400px",
              color: "#94a3b8",
              fontSize: "1.1rem"
            }}>
              Cargando información del dashboard...
            </div>
          ) : (
            <section className="dashboard-grid">
              {/* COLUMNA 1: Perfil Financiero */}
              <article className="card-container profile-card">
                <div className="profile-top-section">
                  <header className="card-header-plain">
                    <h3>PERFIL FINANCIERO</h3>
                  </header>

                  <PieChartWithNeedle
                    scoreValue={
                      esSinDeterminar
                        ? null
                        : calcularScorePerfil(dashboardData?.perfil_financiero)
                    }
                    gaugeData={gaugeData}
                  />
                </div>

                <div className="profile-bottom-section">
                  <div className="score-section">
                    <h3>SALUD FINANCIERA</h3>

                    <p>
                      <span className={estadoSalud.claseCss} style={esSinDeterminar ? { color: "#9ca3af" } : {}}>
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
                  {gastosGrafico.length === 0 ? (
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        height: "120px",
                        color: "#9ca3af",
                        textAlign: "center"
                      }}
                    >
                      Sin Datos registrados para este periodo
                    </div>
                  ) : (
                    <GraficoGastos
                      datos={gastosGrafico}
                      colores={COLORS_GASTOS}
                    />
                  )}
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

                    {(dashboardData?.transacciones || []).length === 0 ? (
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "center",
                          alignItems: "center",
                          height: "120px",
                          color: "#9ca3af",
                          textAlign: "center"
                        }}
                      >
                        Sin Datos registrados para este periodo.
                      </div>
                    ) : (
                      (dashboardData?.transacciones || []).map((item, index) => (
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
                      ))
                    )}
                  </div>
                </article>
              </div>

              {/* COLUMNA 3: Recomendaciones */}
              <article className="card-container recommendations-card">
                <h3>RECOMENDACIONES</h3>
                {(dashboardData?.recomendaciones || []).length === 0 ? (
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      alignItems: "center",
                      height: "150px",
                      color: "#9ca3af",
                      textAlign: "center"
                    }}
                  >
                    Sin Datos registrados para este periodo.
                  </div>
                ) : (
                  (dashboardData?.recomendaciones || [])
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
                          <button type="button" className="btn-green-full" onClick={handleVerDetalle}>
                            Ver detalles
                          </button>
                        </div>
                      </div>
                    ))
                )}
              </article>
            </section>
          )}
        </div>
      </main>
    </div>
  );
}

export default Dashboard;