import { useState } from "react";
import { Icon } from "@iconify/react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import flechaUp from "../assets/img/flecha-up.svg";
import flechaDown from "../assets/img/flecha-down.svg";
import "./EstadisticasA.css";

const dataPorRango = {
  hoy: [
    { name: "8am", valor: 20 },
    { name: "12pm", valor: 45 },
    { name: "4pm", valor: 30 },
    { name: "8pm", valor: 60 },
  ],
  semana: [
    { name: "Lun", valor: 120 },
    { name: "Mar", valor: 90 },
    { name: "Mie", valor: 150 },
    { name: "Jue", valor: 80 },
    { name: "Vie", valor: 200 },
    { name: "Sab", valor: 170 },
    { name: "Dom", valor: 60 },
  ],
  mes: [
    { name: "Sem 1", valor: 400 },
    { name: "Sem 2", valor: 300 },
    { name: "Sem 3", valor: 500 },
    { name: "Sem 4", valor: 480 },
  ],
  anio: [
    { name: "Ene", valor: 1200 },
    { name: "Feb", valor: 1500 },
    { name: "Mar", valor: 1100 },
    { name: "Abr", valor: 1800 },
    { name: "May", valor: 1600 },
    { name: "Jun", valor: 2000 },
  ],
};

const resumenCards = [
  { label: "Ingresos", valor: "$2,000", variacion: "13%", tendencia: "up" },
  { label: "Gastos", valor: "$1,680", variacion: "8%", tendencia: "down" },
  { label: "Ahorro", valor: "$750", variacion: "20%", tendencia: "up" },
];

function EstadisticasA() {
  const [rango, setRango] = useState("hoy");
  const [periodo, setPeriodo] = useState("mes");
  const [fechaInicio, setFechaInicio] = useState("2026-07-01");
  const [fechaFin, setFechaFin] = useState("2026-08-01");
  const [mostrarFechas, setMostrarFechas] = useState(false);

  const rangos = [
    { key: "hoy", label: "HOY" },
    { key: "semana", label: "SEMANA" },
    { key: "mes", label: "MES" },
    { key: "anio", label: "AÑO" },
  ];

  return (
    <div className="estadisticas-layout">
      <Sidebar />

      <div className="estadisticas-main">
        <div className="estadisticas-dark-bg">
          <header className="estadisticas-topbar">
            <div className="search-box">
              <Icon icon="mdi:magnify" width="18" />
              <input type="text" placeholder="¿qué deseas buscar?" />
            </div>
            <UserBadge />
          </header>

          <section className="estadisticas-hero">
            <div className="estadisticas-hero-title">
              <h1>Estadísticas</h1>
              <p>Análisis de tus finanzas</p>
            </div>

            <div className="estadisticas-filtro">
              <div className="filtro-label">
                <Icon icon="mdi:filter-variant" width="18" />
                <span>Filtrar por</span>
              </div>

              <select
                className="filtro-pill filtro-select"
                value={periodo}
                onChange={(e) => setPeriodo(e.target.value)}
              >
                <option value="semana">Esta semana</option>
                <option value="mes">Este mes</option>
                <option value="trimestre">Este trimestre</option>
                <option value="anio">Este año</option>
              </select>

              <div className="filtro-fechas-wrapper">
                <button
                  className="filtro-pill"
                  onClick={() => setMostrarFechas(!mostrarFechas)}
                >
                  {fechaInicio.slice(5).split("-").reverse().join("/")} -{" "}
                  {fechaFin.slice(5).split("-").reverse().join("/")}
                  <Icon icon="mdi:calendar-month-outline" width="16" />
                </button>

                {mostrarFechas && (
                  <div className="filtro-fechas-dropdown">
                    <label>
                      Desde
                      <input
                        type="date"
                        value={fechaInicio}
                        onChange={(e) => setFechaInicio(e.target.value)}
                      />
                    </label>
                    <label>
                      Hasta
                      <input
                        type="date"
                        value={fechaFin}
                        onChange={(e) => setFechaFin(e.target.value)}
                      />
                    </label>
                    <button
                      className="filtro-fechas-aplicar"
                      onClick={() => setMostrarFechas(false)}
                    >
                      Aplicar
                    </button>
                  </div>
                )}
              </div>
            </div>
          </section>

          <section className="estadisticas-content">
            <div className="grafico-card">
              <h2>EVOLUCIÓN GASTOS</h2>

              <div className="grafico-tabs">
                {rangos.map((r) => (
                  <button
                    key={r.key}
                    className={`grafico-tab ${rango === r.key ? "active" : ""}`}
                    onClick={() => setRango(r.key)}
                  >
                    {r.label}
                  </button>
                ))}
              </div>

              <div className="grafico-chart">
                <ResponsiveContainer width="100%" height={260}>
                  <LineChart data={dataPorRango[rango]}>
                    <CartesianGrid
                      strokeDasharray="3 3"
                      stroke="var(--neutro-ccc)"
                    />
                    <XAxis
                      dataKey="name"
                      stroke="var(--neutro-666)"
                      fontSize={12}
                    />
                    <YAxis stroke="var(--neutro-666)" fontSize={12} />
                    <Tooltip />
                    <Line
                      type="monotone"
                      dataKey="valor"
                      stroke="var(--color-secundario-azul)"
                      strokeWidth={2}
                      dot={{ r: 3 }}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </div>

            <div className="resumen-cards">
              {resumenCards.map((card) => (
                <div className="resumen-card" key={card.label}>
                  <div
                    className={`resumen-card-icon ${card.tendencia === "down" ? "negativo" : "positivo"}`}
                  >
                    <img
                      src={card.tendencia === "down" ? flechaDown : flechaUp}
                      alt={
                        card.tendencia === "down"
                          ? "tendencia negativa"
                          : "tendencia positiva"
                      }
                      className="tendencia-icon"
                    />
                  </div>
                  <div className="resumen-card-info">
                    <p className="resumen-card-label">{card.label}</p>
                    <p className="resumen-card-valor">{card.valor}</p>
                    <p className="resumen-card-sub">vs. mes anterior</p>
                    <div
                      className={`resumen-card-variacion ${card.tendencia === "down" ? "negativo" : "positivo"}`}
                    >
                      <Icon
                        icon={
                          card.tendencia === "down"
                            ? "mdi:trending-down"
                            : "mdi:trending-up"
                        }
                        width="14"
                      />
                      <span>{card.variacion}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default EstadisticasA;
