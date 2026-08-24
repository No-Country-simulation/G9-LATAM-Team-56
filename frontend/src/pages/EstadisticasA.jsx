import { useState, useEffect } from "react";
import { obtenerEstadisticas } from "/services/api";
import { useNavigate } from "react-router-dom";
import ModalBloqueo from "../components/Bloqueo";
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
  const [bloqueado, setBloqueado] = useState(false);

  const navigate = useNavigate();

  // VERIFICAR SI EL CSV ESTÁ CARGADO
  useEffect(() => {
   const csvCargado = localStorage.getItem("csvCargado") === "true";
     // Si intentan entrar sin tener el CSV cargado mostramos el pop-up
     if (!csvCargado) {
       setBloqueado(true); // Activa el bloque si no hay CSV
     }
   }, [navigate]);

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
    { key: "hoy", label: "DÍA" },
    { key: "semana", label: "SEMANA" },
    { key: "mes", label: "MES" },
    { key: "anio", label: "AÑO" },
  ];

  // FUNCIÓN PARA ASIGNAR UN ICONO SEGÚN LA CATEGORÍA
  const obtenerIconoCategoria = (nombreCategoria) => {
    if (!nombreCategoria) return "mdi:tag-outline";
    const cat = nombreCategoria.toLowerCase();

    if (cat.includes("vivienda")) { return "mdi:mdi:home-outline"; }
    if (cat.includes("servicios")) { return "mdi:lightning-bolt-outline" }
    if (cat.includes("alimentacion")) { return "mdi:cart-outline"; }
    if (cat.includes("transporte")) { return "mdi:car-outline"; }
    if (cat.includes("restaurant")) { return "mdi:silverware-fork-knife"; }
    if (cat.includes("entretenimiento")) { return "mdi:movie-open-outline"; }
    if (cat.includes("vestuario")) { return "mdi:tshirt-crew-outline"; }
    if (cat.includes("electronicos")) { return "mdi:laptop"; }
    if (cat.includes("salud")) { return "mdi:hospital-box-outline"; }
    if (cat.includes("educacion")) { return "mdi:school-outline"; }
    if (cat.includes("otros")) { return "mdi:dots-horizontal-circle-outline"; }

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
        const data = await obtenerEstadisticas({
          usuario: usuarioActual,
          inicio: fechaInicioFiltro,
          fin: fechaFinFiltro
        });
        setTopCategorias(data.top3 || []);
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

      // Definición de rangos de fechas dinámicos para el backend
      if (rangoGrafico === "hoy") {
        inicio = new Date(hoy);
      } else if (rangoGrafico === "semana") {
        // Últimos 7 días incluyendo hoy
        inicio = new Date(hoy);
        inicio.setDate(hoy.getDate() - 6);
      } else if (rangoGrafico === "mes") {
        // Ciclo mensual: exactamente un mes atrás hasta hoy
        inicio = new Date(hoy);
        inicio.setMonth(hoy.getMonth() - 1);
      } else if (rangoGrafico === "anio") {
        // Ciclo anual: 12 meses atrás hasta hoy
        inicio = new Date(hoy);
        inicio.setFullYear(hoy.getFullYear() - 1);
        inicio.setDate(inicio.getDate() + 1);
      }

      const fInicio = inicio.toISOString().split("T")[0];
      const fFin = fin.toISOString().split("T")[0];

      try {
        const data = await obtenerEstadisticas({
          usuario: usuarioActual,
          inicio: fInicio,
          fin: fFin
        });

        const transacciones = data.transaccionesDetalladas || [];
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

  // Lógica de procesamiento
  const procesarDatosGrafico = (transacciones, rango, hoy) => {
    if (!transacciones) transacciones = [];
    const hoyStr = hoy.toISOString().split("T")[0];

    // Directriz HOY: Categorías de transacciones del día actual en Eje X, valor en Eje Y
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

    // Directriz SEMANA: Últimos 7 días terminando en el día actual de la semana
    if (rango === "semana") {
      const diasSemanaNombres = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];
      const resultadoMap = {};
      const fechasArray = [];

      // Generar los últimos 7 días hacia atrás terminando en hoy
      for (let i = 6; i >= 0; i--) {
        const d = new Date(hoy);
        d.setDate(hoy.getDate() - i);
        const fechaKey = d.toISOString().split("T")[0];
        const nombreDia = diasSemanaNombres[d.getDay()];

        // Guardamos para mapear, asegurando etiqueta única o formato amigable
        fechasArray.push({ fechaKey, name: nombreDia });
        resultadoMap[fechaKey] = 0;
      }

      // Sumar transacciones por fecha exacta
      transacciones.forEach(item => {
        if (resultadoMap[item.fecha] !== undefined) {
          resultadoMap[item.fecha] += item.valor;
        }
      });

      return fechasArray.map(item => ({
        name: item.name,
        valor: resultadoMap[item.fechaKey]
      }));
    }

    // Directriz MES: Ciclo mensual de un mes (ej. 21/07 al 20/08), formato día/mes
    if (rango === "mes") {
      const resultadoMap = {};
      const diasArray = [];

      // Calcular desde hace un mes exacto hasta hoy
      const fechaIterador = new Date(hoy);
      fechaIterador.setMonth(hoy.getMonth() - 1);
      // Avanzar un día para iniciar el día siguiente al cierre del ciclo anterior si aplica, o exacto:
      fechaIterador.setDate(fechaIterador.getDate() + 1);

      while (fechaIterador <= hoy) {
        const fechaKey = fechaIterador.toISOString().split("T")[0];
        const diaStr = String(fechaIterador.getDate()).padStart(2, "0");
        const mesStr = String(fechaIterador.getMonth() + 1).padStart(2, "0");
        const formatoEtiqueta = `${diaStr}/${mesStr}`;

        diasArray.push({ fechaKey, name: formatoEtiqueta });
        resultadoMap[fechaKey] = 0;

        fechaIterador.setDate(fechaIterador.getDate() + 1);
      }

      transacciones.forEach(item => {
        if (resultadoMap[item.fecha] !== undefined) {
          resultadoMap[item.fecha] += item.valor;
        }
      });

      return diasArray.map(item => ({
        name: item.name,
        valor: resultadoMap[item.fechaKey]
      }));
    }

    // Directriz AÑO: 12 meses, formato MES - AÑO (ej. SEP - 25 a AGO - 26), limitando el mes actual hasta hoy
    if (rango === "anio") {
      const mesesNombresCortos = ["ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"];
      const mesesArray = [];

      // Generar los últimos 12 meses terminando en el mes actual
      for (let i = 11; i >= 0; i--) {
        const d = new Date(hoy.getFullYear(), hoy.getMonth() - i, 1);
        const mesIndex = d.getMonth();
        const anioCorto = String(d.getFullYear()).slice(-2);
        const nombreMesAnio = `${mesesNombresCortos[mesIndex]} - ${anioCorto}`;

        mesesArray.push({
          year: d.getFullYear(),
          month: mesIndex,
          name: nombreMesAnio,
          valor: 0
        });
      }

      transacciones.forEach(item => {
        const fechaObj = parsearFechaLocal(item.fecha);
        const itemAnio = fechaObj.getFullYear();
        const itemMes = fechaObj.getMonth();

        // Limitación de mes actual de considerar solo gastos hasta el día actual del mes
        if (itemAnio === hoy.getFullYear() && itemMes === hoy.getMonth()) {
          if (fechaObj > hoy) return; // Ignora días futuros dentro del mes actual
        }

        const encontrado = mesesArray.find(m => m.year === itemAnio && m.month === itemMes);
        if (encontrado) {
          encontrado.valor += item.valor;
        }
      });

      return mesesArray.map(m => ({
        name: m.name,
        valor: m.valor
      }));
    }

    return [];
  };

  return (
    <div className="estadisticas-layout" style={{ position: "relative" }}>
      {/* Si está bloqueado, se muestra el pop-up encima de toda la vista */}
      {bloqueado && <ModalBloqueo />}

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
                    {/* Aplicar padding dinámico en el Eje X según el botón seleccionado (Día vs otros) */}
                    <XAxis
                      dataKey="name"
                      stroke="var(--neutro-666)"
                      fontSize={12}
                      // Si es "hoy", espaciamos los extremos (padding) para que las categorías no peguen contra los bordes
                      padding={rangoGrafico === "hoy" ? { left: 50, right: 50 } : { left: 10, right: 10 }}
                      // Si hay un solo elemento en "hoy", forzamos que se ubique centrado en el gráfico
                      scale={rangoGrafico === "hoy" && datosGrafico.length === 1 ? "band" : "auto"}
                    />
                    {/* Agregamos domain={[0, 'auto']} para que el eje Y siempre sea visible */}
                    <YAxis
                      stroke="var(--neutro-666)"
                      fontSize={12}
                      domain={[0, 'auto']}
                    />
                    {/* Si es "hoy" y hay exactamente 1 categoría, permitimos mostrar el punto del gráfico asegurando que conecte o dibuje el dot visible */}
                    <Tooltip />
                    <Line
                      type="monotone"
                      dataKey="valor"
                      stroke="var(--color-secundario-azul)"
                      strokeWidth={2}
                      dot={{ r: datosGrafico.length === 1 ? 5 : 3 }}
                      isAnimationActive={false}
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
                <p style={{ color: "#fff", padding: "10px" }}>Sin Datos registrados para este periodo</p>
              )}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default EstadisticasA;