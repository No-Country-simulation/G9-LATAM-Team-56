// --- MAPEO DE ÍCONOS POR CATEGORÍA (Búsqueda O(1)) ---
const CATEGORY_ICON_MAP = {
  vivienda: "mdi:home-outline",
  servicios: "mdi:lightning-bolt-outline",
  restaurante: "mdi:silverware-fork-knife",
  transporte: "mdi:car-outline",
  vestuario: "mdi:tshirt-crew-outline",
  entretenimiento: "mdi:movie-open-outline",
  electronicos: "mdi:laptop",
  electrónicos: "mdi:laptop",
  salud: "mdi:hospital-box-outline",
  alimentacion: "mdi:cart-outline",
  alimentación: "mdi:cart-outline",
  educacion: "mdi:school-outline",
  educación: "mdi:school-outline",
  ahorro: "mdi:piggy-bank-outline",
  otros: "mdi:dots-horizontal-circle-outline",
};

/**
 * Traduce la categoría enviada por el Backend al ícono de Iconify correspondiente.
 */
export const obtenerIconoCategoria = (categoria = "") => {
  if (!categoria) return "mdi:image-outline";
  const catLimpia = categoria.trim().toLowerCase();

  if (CATEGORY_ICON_MAP[catLimpia]) {
    return CATEGORY_ICON_MAP[catLimpia];
  }

  const claveEncontrada = Object.keys(CATEGORY_ICON_MAP).find((key) =>
    catLimpia.includes(key)
  );

  return claveEncontrada
    ? CATEGORY_ICON_MAP[claveEncontrada]
    : "mdi:image-outline";
};

/**
 * Determina el estado de salud financiera según la escala del Gauge.
 */
export const obtenerEstadoSalud = (score = 0) => {
  if (score >= 50) {
    return { texto: "Buena", claseCss: "status-good" };
  }
  if (score >= 20) {
    return { texto: "Media", claseCss: "status-warning" };
  }
  return { texto: "Baja", claseCss: "status-bad" };
};

// Paleta de colores para gráficos Recharts (Definitivo)
export const COLORS_GASTOS = [
  "#0088FE", // Azul principal
  "#00C49F", // Verde menta
  "#FFBB28", // Amarillo mostaza
  "#FF8042", // Naranja vibrante
  "#A4DE6C", // Verde lima
  "#8884D8", // Morado lavanda
  "#82CA9D", // Verde suave
  "#FF6584", // Rosa coral
  "#4BC0C0", // Turquesa
  "#9966FF", // Violeta
  "#C9CBCF"  // Gris neutro (ideal para la categoría "Otros")
];


// DATOS MOCK TEMPORALES (Sustituir con llamadas API)

export const GAUGE_MOCK = {
  scoreValue: 45,
  gaugeData: [
    { name: "Baja Salud", value: 20, color: "#990000" },
    { name: "Salud Media", value: 30, color: "#D97706" },
    { name: "Buena Salud", value: 50, color: "#2D6A4F" },
  ]
};

export const KPIS_MOCK = {
  perfil: "En Observación",
  probabilidad: 82,
  endeudamiento: 25
};

export const GASTOS_MOCK = [
  { id: 1, categoria: "Vivienda", monto: 450.00 },
  { id: 2, categoria: "Servicios", monto: 85.50 },
  { id: 3, categoria: "Alimentación", monto: 110.40 },
  { id: 4, categoria: "Transporte", monto: 30.00 },
  { id: 5, categoria: "Restaurante", monto: 42.00 },
  { id: 6, categoria: "Entretenimiento", monto: 25.00 },
  { id: 7, categoria: "Vestuario", monto: 65.00 },
  { id: 8, categoria: "Electrónicos", monto: 299.99 },
  { id: 9, categoria: "Salud", monto: 35.00 },
  { id: 10, categoria: "Educación", monto: 120.00 },
  { id: 11, categoria: "Otros", monto: 18.25 },
];

export const TRANSACCIONES_MOCK = [
  { id: 101, categoria: "Vivienda", fecha: "2026-08-10", monto: 450.00 },
  { id: 102, categoria: "Servicios", fecha: "2026-08-09", monto: 85.50 },
  { id: 103, categoria: "Restaurante", fecha: "2026-08-08", monto: 42.00 },
  { id: 104, categoria: "Transporte", fecha: "2026-08-07", monto: 30.00 },
  { id: 105, categoria: "Vestuario", fecha: "2026-08-06", monto: 65.00 },
  { id: 106, categoria: "Entretenimiento", fecha: "2026-08-05", monto: 25.00 },
  { id: 107, categoria: "Electrónicos", fecha: "2026-08-04", monto: 299.99 },
  { id: 108, categoria: "Salud", fecha: "2026-08-03", monto: 35.00 },
  { id: 109, categoria: "Alimentación", fecha: "2026-08-02", monto: 110.40 },
  { id: 110, categoria: "Educación", fecha: "2026-08-01", monto: 120.00 },
  { id: 111, categoria: "Otros", fecha: "2026-07-31", monto: 18.25 },
];

export const RECOMENDACIONES_MOCK = [
  {
    id: 1,
    categoria: "Entretenimiento",
    texto: "Monitorear los gastos recurrentes de entretenimiento"
  },
  {
    id: 2,
    categoria: "Ahorro",
    texto: "Aumentar la reserva financiera mensual"
  }
];

/**
 * Retorna los 3 arcos del velocímetro con sus nombres y colores correspondientes:
 * - En riesgo: Rojo (#EF4444)
 * - En observación: Amarillo (#F59E0B)
 * - Saludable: Verde (#10B981)
 */
export const obtenerGaugeData = () => {
  return [
    { name: "En Riesgo", value: 33.33, color: "#EF4444" },      // Arco 0 - 33.33% (Rojo)
    { name: "En Observación", value: 33.33, color: "#F59E0B" }, // Arco 33.33% - 66.66% (Amarillo)
    { name: "Saludable", value: 33.34, color: "#10B981" },      // Arco 66.66% - 100% (Verde)
  ];
};