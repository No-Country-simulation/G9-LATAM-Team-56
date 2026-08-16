import React from "react";
import { Icon } from "@iconify/react";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import "./Recomendaciones.css";

// 1. Datos de prueba preparados con "prioridad"
const recomendacionesData = [
  {
    id: 1,
    title: "Aumenta tu ahorro mensual",
    description: "Intenta ahorrar al menos el 20% de tus ingresos",
    prioridad: "BAJA", // Verde (Saludable)
  },
  {
    id: 2,
    title: "Evolución del gasto",
    description: "Tu nivel de endeudamiento subió 5% este mes",
    prioridad: "MEDIA", // Naranja (Advertencia)
  },
  {
    id: 3,
    title: "Reduce tus gastos",
    description: "Tu gasto en restaurante representa el 20% de tus gastos totales",
    prioridad: "ALTA", // Rojo (Riesgo)
  },
];

// 2. Función helper para convertir prioridad en clase CSS e icono de la izquierda
const getStatusConfig = (prioridad) => {
  switch (prioridad) {
    case "ALTA":
      return {
        bgClass: "status-bad-bg",
        iconLeft: "mdi:emoticon-dead",
      };
    case "MEDIA":
      return {
        bgClass: "status-warning-bg",
        iconLeft: "ph:warning-fill",
      };
    case "BAJA":
    default:
      return {
        bgClass: "status-good-bg",
        iconLeft: "solar:shield-check-bold",
      };
  }
};

//------------------------------------------------------------------------------------------------------------------
export default function Recomendaciones() {
  return (
    <div className="recomendaciones-layout">
      <Sidebar />

      <div className="recomendaciones-main">
        <div className="recomendaciones-dark-bg">
          {/* TOPBAR */}
          <header className="recomendaciones-topbar">
            <div className="search-box">
              <input type="text" placeholder="¿qué deseas buscar?" />
              <Icon icon="mdi:magnify" width="20" className="search-icon" />
            </div>

            <UserBadge />
          </header>

          {/* HERO */}
          <section className="recomendaciones-hero">
            <h1>Recomendaciones</h1>
            <h2>Sugerencias para mejorar tu salud financiera</h2>
          </section>

          {/* GRID */}
          <section className="recomendaciones-content">
            <div className="recomendaciones-grid">
              {recomendacionesData.slice(0, 3).map((item) => {
                const { bgClass, iconLeft } = getStatusConfig(item.prioridad);

                return (
                  <div key={item.id} className="rec-full-card">
                    {/* SECCIÓN IZQUIERDA */}
                    <div className={`rec-card-left ${bgClass}`}>
                      <div className="rec-hero-icon-badge">
                        <Icon icon={iconLeft} width="28" />
                      </div>
                      <div className="rec-text-info">
                        <h3>{item.title}</h3>
                        <p>{item.description}</p>
                      </div>
                    </div>

                    {/* SECCIÓN DERECHA */}
                    <div className="rec-card-right">
                      <Icon icon="flat-color-icons:combo-chart" width="44" className="rec-action-icon" />
                      <button type="button" className="btn-blue-action">
                        Ver detalle
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}