import { useState, useEffect } from "react";
import axios from "axios";
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
import "./EstadisticasA.css";

function EstadisticasA() {
  // Usuario autenticado real
  const usuarioActual = localStorage.getItem("usuarioNombre") || sessionStorage.getItem("usuarioNombre") || "Invitado";

  // ESTADOS EXCLUSIVOS PARA LAS TARJETAS (Filtro Superior)
  const [periodoFiltro, setPeriodoFiltro] = useState("semana");
  const [fechaInicioFiltro, setFechaInicioFiltro] = useState("");
  const [fechaFinFiltro, setFechaFinFiltro] = useState("");
  const [topCategorias, setTopCategorias] = useState([]);

  // ESTADOS EXCLUSIVOS PARA EL GRÁFICO (Botones: HOY por defecto)
  const [rangoGrafico, setRangoGrafico] = useState("hoy");
  const [datosGrafico, setDatosGrafico] = useState([]);

  const rangosBotones = [
    { key: "hoy", label: "HOY" },
    { key: "semana", label: "SEMANA" },
    { key: "mes", label: "MES" },
    { key: "anio", label: "AÑO" },
  ];

  // FUNCIÓN PARA ASIGNAR UN ICONO SEGÚN LA CATEGORÍA
  const obtenerIconoCategoria = (nombreCategoria) => {
    if (!nombreCategoria) return "mdi:tag-outline";
    const cat = nombreCategoria.toLowerCase();

    if (cat.includes("vivienda")) {return "mdi:mdi:home-outline";}
    if (cat.includes("servicios")) {return "mdi:lightning-bolt-outline"}
    if (cat.includes("alimentacion")) {return "mdi:cart-outline";}
    if (cat.includes("transporte")) {return "mdi:car-outline";}
    if (cat.includes("restaurant")) {return "mdi:silverware-fork-knife";}
    if (cat.includes("entretenimiento")) {return "mdi:movie-open-outline";}
    if (cat.includes("vestuario")) {return "mdi:tshirt-crew-outline";}
    if (cat.includes("electronicos")) {return "mdi:laptop";}
    if (cat.includes("salud")) {return "mdi:hospital-box-outline";}
    if (cat.includes("educacion")) {return "mdi:school-outline";}
    if (cat.includes("otros")) {return "mdi:dots-horizontal-circle-outline";}

    return "mdi:tag-outline"; // Icono por defecto
  };

  // =============================
  //  TARJETAS (Filtro Superior)
  // =============================
  useEffect(() => {
    const ahora = new Date();
    let inicio = new Date();
    let fin = new Date();

    if (periodoFiltro === "semana") {
      const diaSemana = ahora.getDay();
      const diffToMonday = ahora.getDate() - diaSemana + (diaSemana === 0 ? -6 : 1);
      inicio = new Date(ahora.setDate(diffToMonday));
      fin = new Date();
    } else if (periodoFiltro === "mes") {
      inicio = new Date(ahora.getFullYear(), ahora.getMonth(), 1);
      fin = new Date(ahora.getFullYear(), ahora.getMonth() + 1, 0);
    } else if (periodoFiltro === "trimestre") {
      inicio = new Date(ahora.getFullYear(), ahora.getMonth() - 3, 1);
      fin = new Date();
    } else if (periodoFiltro === "anio") {
      inicio = new Date(ahora.getFullYear(), 0, 1);
      fin = new Date(ahora.getFullYear(), 11, 31);
    }

    setFechaInicioFiltro(inicio.toISOString().split("T")[0]);
    setFechaFinFiltro(fin.toISOString().split("T")[0]);
  }, [periodoFiltro]);

  useEffect(() => {
    if (!fechaInicioFiltro || !fechaFinFiltro || usuarioActual === "Invitado") return;

    const cargarTarjetas = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/estadisticas`, {
          params: { usuario: usuarioActual, inicio: fechaInicioFiltro, fin: fechaFinFiltro }
        });
        setTopCategorias(res.data.top3 || []);
      } catch (error) {
        console.error("Error al cargar tarjetas:", error);
      }
    };
    cargarTarjetas();
  }, [fechaInicioFiltro, fechaFinFiltro, usuarioActual]);

  // ==================================
  //  GRÁFICO (Botones Independientes)
  // ==================================
  useEffect(() => {
    if (usuarioActual === "Invitado") return;

    const cargarGraficoIndependiente = async () => {
      const hoy = new Date();
      let inicio = new Date();
      let fin = new Date(hoy);

      if (rangoGrafico === "hoy") {
        inicio = hoy;
        fin = hoy;
      } else if (rangoGrafico === "semana") {
        const diaSemana = hoy.getDay();
        const diffToMonday = hoy.getDate() - diaSemana + (diaSemana === 0 ? -6 : 1);
        inicio = new Date(new Date().setDate(diffToMonday));
      } else if (rangoGrafico === "mes") {
        inicio = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
      } else if (rangoGrafico === "anio") {
        inicio = new Date(hoy.getFullYear(), 0, 1);
      }

      const fInicio = inicio.toISOString().split("T")[0];
      const fFin = fin.toISOString().split("T")[0];

      try {
        const res = await axios.get(`http://localhost:8080/api/estadisticas`, {
          params: { usuario: usuarioActual, inicio: fInicio, fin: fFin }
        });

        const transacciones = res.data.transaccionesDetalladas || [];
        const datosProcesados = procesarDatosGrafico(transacciones, rangoGrafico, hoy);
        setDatosGrafico(datosProcesados);
      } catch (error) {
        console.error("Error al cargar gráfico:", error);
      }
    };

    cargarGraficoIndependiente();
  }, [rangoGrafico, usuarioActual]);

  const parsearFechaLocal = (fechaStr) => {
    const [anio, mes, dia] = fechaStr.split("-").map(Number);
    return new Date(anio, mes - 1, dia);
  };

  const procesarDatosGrafico = (transacciones, rango, hoy) => {
    if (!transacciones || transacciones.length === 0) return [];
    const hoyStr = hoy.toISOString().split("T")[0];

    if (rango === "hoy") {
      const agrupado = {};
      transacciones.forEach(item => {
        if (item.fecha === hoyStr) {
          const cat = item.categoria || "General";
          agrupado[cat] = (agrupado[cat] || 0) + item.valor;
        }
      });
      return Object.keys(agrupado).map(cat => ({ name: cat, valor: agrupado[cat] }));
    }

    if (rango === "semana") {
      const diasOrdenados = ["Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"];
      const agrupado = { "Lun": 0, "Mar": 0, "Mié": 0, "Jue": 0, "Vie": 0, "Sáb": 0, "Dom": 0 };
      transacciones.forEach(item => {
        const fechaObj = parsearFechaLocal(item.fecha);
        let jsDay = fechaObj.getDay();
        const indices = [6, 0, 1, 2, 3, 4, 5];
        const nombreDia = diasOrdenados[indices[jsDay]];
        agrupado[nombreDia] += item.valor;
      });
      return diasOrdenados.map(dia => ({ name: dia, valor: agrupado[dia] }));
    }

    if (rango === "mes") {
      const agrupado = { "Sem 1": 0, "Sem 2": 0, "Sem 3": 0, "Sem 4": 0 };
      transacciones.forEach(item => {
        const fechaObj = parsearFechaLocal(item.fecha);
        const diaMes = fechaObj.getDate();
        let sem = "Sem 4";
        if (diaMes <= 7) sem = "Sem 1";
        else if (diaMes <= 14) sem = "Sem 2";
        else if (diaMes <= 21) sem = "Sem 3";
        agrupado[sem] += item.valor;
      });
      return Object.keys(agrupado).map(sem => ({ name: sem, valor: agrupado[sem] }));
    }

    if (rango === "anio") {
      const mesesNombres = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
      const agrupado = {};
      mesesNombres.forEach(m => agrupado[m] = 0);
      transacciones.forEach(item => {
        const fechaObj = parsearFechaLocal(item.fecha);
        const mesIndex = fechaObj.getMonth();
        const mesNombre = mesesNombres[mesIndex];
        if (mesNombre) agrupado[mesNombre] += item.valor;
      });
      return mesesNombres.map(mes => ({ name: mes, valor: agrupado[mes] }));
    }

    return [];
  };

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

              {/* Selector exclusivo para las Tarjetas Top 3 */}
              <select
                className="filtro-pill filtro-select"
                value={periodoFiltro}
                onChange={(e) => setPeriodoFiltro(e.target.value)}
              >
                <option value="semana">Esta semana</option>
                <option value="mes">Este mes</option>
                <option value="trimestre">Este trimestre</option>
                <option value="anio">Este año</option>
              </select>

              {/* Rango de Fechas visual de las tarjetas */}
              <div className="filtro-fechas-wrapper">
                <div className="filtro-pill" style={{ cursor: "default" }}>
                  {fechaInicioFiltro ? fechaInicioFiltro.split("-").reverse().join("/") : ""} -{" "}
                  {fechaFinFiltro ? fechaFinFiltro.split("-").reverse().join("/") : ""}
                  <Icon icon="mdi:calendar-month-outline" width="16" />
                </div>
              </div>
            </div>
          </section>

          <section className="estadisticas-content">
            <div className="grafico-card">
              <h2>EVOLUCIÓN GASTOS</h2>

              {/* Botones exclusivos para el Gráfico */}
              <div className="grafico-tabs">
                {rangosBotones.map((r) => (
                  <button
                    key={r.key}
                    className={`grafico-tab ${rangoGrafico === r.key ? "active" : ""}`}
                    onClick={() => setRangoGrafico(r.key)}
                  >
                    {r.label}
                  </button>
                ))}
              </div>

              <div className="grafico-chart">
                <ResponsiveContainer width="100%" height={260}>
                  {/* Si no hay datos, enviamos un array con valor 0 para mantener la estructura */}
                  <LineChart data={datosGrafico.length > 0 ? datosGrafico : [{ name: 'Sin datos', valor: 0 }]}>
                    <CartesianGrid strokeDasharray="3 3" stroke="var(--neutro-ccc)" />
                    <XAxis dataKey="name" stroke="var(--neutro-666)" fontSize={12} />

                    {/* Agregamos domain={[0, 'auto']} para que el eje Y siempre sea visible */}
                    <YAxis
                      stroke="var(--neutro-666)"
                      fontSize={12}
                      domain={[0, 'auto']}
                    />

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

            {/* TARJETAS TOP 3 CATEGORÍAS CON ICONOS */}
            <div className="resumen-cards">
              {topCategorias.length > 0 ? (
                topCategorias.map((card, index) => (
                  <div className="resumen-card" key={index}>
                    <div className="resumen-card-icon">
                      <Icon icon={obtenerIconoCategoria(card.label)} width="24" />
                    </div>
                    <div className="resumen-card-info">
                      <p className="resumen-card-label">{card.label}</p>
                      <p className="resumen-card-valor">{card.valor}</p>
                    </div>
                  </div>
                ))
              ) : (
                <p style={{ color: "#fff", padding: "10px" }}>No hay datos para este período</p>
              )}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default EstadisticasA;