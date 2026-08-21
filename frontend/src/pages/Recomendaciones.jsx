import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Icon } from "@iconify/react";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import { obtenerDashboard } from "/services/api";
import "./Recomendaciones.css";

// Normaliza texto para comparar sin depender de mayúsculas/tildes exactas
const normalizar = (texto) =>
  (texto || "")
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "");

// Determina el estilo (color + ícono) según el perfil financiero del usuario
const getStatusConfig = (perfilFinanciero) => {
  const perfil = normalizar(perfilFinanciero);

  switch (perfil) {
    case "riesgo":
      return { bgClass: "status-bad-bg", iconLeft: "mdi:emoticon-dead" };
    case "en observacion":
      return { bgClass: "status-warning-bg", iconLeft: "ph:warning-fill" };
    case "saludable":
      return { bgClass: "status-good-bg", iconLeft: "solar:shield-check-bold" };
    default:
      console.warn(`Perfil financiero no reconocido: "${perfilFinanciero}"`);
      return { bgClass: "status-good-bg", iconLeft: "solar:shield-check-bold" };
  }
};

// Determina el ícono según la categoría de la recomendación
const getCategoriaIcon = (categoria) => {
  const cat = normalizar(categoria);

  const iconos = {
    transporte: "mdi:car",
    electronicos: "mdi:laptop",
    alimentacion: "mdi:food",
    vivienda: "mdi:home",
    servicios: "mdi:flash",
    restaurante: "mdi:silverware-fork-knife",
    entretenimiento: "mdi:movie-open",
    vestuario: "mdi:tshirt-crew",
    salud: "mdi:medical-bag",
    educacion: "mdi:school",
    credito: "mdi:cash",
  };

  return iconos[cat] || "mdi:lightbulb-outline"; // ícono por defecto si no coincide
};

export default function Recomendaciones() {
  const navigate = useNavigate();

  const [recomendaciones, setRecomendaciones] = useState([]);
  const [perfilFinanciero, setPerfilFinanciero] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const nombreUsuario = localStorage.getItem("usuarioNombre");

    if (!nombreUsuario) {
      setError("No se encontró el usuario. Inicia sesión de nuevo.");
      setLoading(false);
      return;
    }

    obtenerDashboard(nombreUsuario)
      .then((data) => {
        setPerfilFinanciero(data.perfil_financiero);
        setRecomendaciones(data.recomendaciones || []);
      })
      .catch((err) => {
        console.error("No se pudieron cargar las recomendaciones:", err);
        setError(
          "No se pudieron cargar las recomendaciones. Importa un CSV desde tu Perfil si aún no lo has hecho.",
        );
      })
      .finally(() => setLoading(false));
  }, []);

  const handleVerDetalle = () => {
    navigate("/estadisticas");
  };

  const { bgClass, iconLeft } = getStatusConfig(perfilFinanciero);

  return (
    <div className="recomendaciones-layout">
      <Sidebar />

      <div className="recomendaciones-main">
        <div className="recomendaciones-dark-bg">
          <header className="recomendaciones-topbar">
            <div className="search-box">
              <input type="text" placeholder="¿qué deseas buscar?" />
              <Icon icon="mdi:magnify" width="20" className="search-icon" />
            </div>

            <UserBadge />
          </header>

          <section className="recomendaciones-hero">
            <h1>Recomendaciones</h1>
            <h2>Sugerencias para mejorar tu salud financiera</h2>
          </section>

          <section className="recomendaciones-content">
            {loading && (
              <p className="rec-status-msg">Cargando recomendaciones...</p>
            )}

            {!loading && error && (
              <p className="rec-status-msg rec-error">{error}</p>
            )}

            {!loading && !error && recomendaciones.length === 0 && (
              <p className="rec-status-msg">
                Aún no tienes recomendaciones. Importa un CSV desde tu Perfil.
              </p>
            )}

            {!loading && !error && recomendaciones.length > 0 && (
              <div className="recomendaciones-grid">
                {recomendaciones.slice(0, 3).map((item, index) => (
                  <div
                    key={`${item.categoria}-${index}`}
                    className="rec-full-card"
                  >
                    <div className={`rec-card-left ${bgClass}`}>
                      <div className="rec-hero-icon-badge">
                        <Icon
                          icon={getCategoriaIcon(item.categoria)}
                          width="28"
                        />
                      </div>
                      <div className="rec-text-info">
                        <h3>{item.categoria}</h3>
                        <p>{item.recomendacion}</p>
                      </div>
                    </div>

                    <div className="rec-card-right">
                      <Icon
                        icon="flat-color-icons:combo-chart"
                        width="44"
                        className="rec-action-icon"
                      />
                      <button
                        type="button"
                        className="btn-blue-action"
                        onClick={handleVerDetalle}
                      >
                        Ver detalle
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>
        </div>
      </div>
    </div>
  );
}
