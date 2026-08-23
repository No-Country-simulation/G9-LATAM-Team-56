import axios from 'axios';

// URLs de backend en Spring Boot
const API_URL = 'http://localhost:8080/api/csv';
const AUTH_URL = 'http://localhost:8080/api/auth';
const PERFIL_URL = 'http://localhost:8080/api/perfil';
const DASHBOARD_URL = 'http://localhost:8080/api/dashboard';
const ESTADISTICAS_URL = 'http://localhost:8080/api/estadisticas';

export const uploadCsvFile = async (file, usuario) => {
  // Creamos un objeto FormData para enviar archivos y texto juntos
  const formData = new FormData();

  formData.append('file', file);
  formData.append('usuario', usuario);

  try {
    const response = await axios.post(`${API_URL}/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  } catch (error) {
    const data = error.response?.data;

    const uploadError = new Error(
      data?.message || 'Error al conectar con el servidor'
    );

    // Clasificamos el error para que el frontend sepa cómo presentarlo
    uploadError.type =
      data?.error === 'CSV_VALIDATION_ERROR'
        ? 'validation'
        : 'server';

    // Conservamos información útil de la respuesta del backend
    uploadError.status = data?.status || error.response?.status || null;
    uploadError.code = data?.error || null;
    uploadError.errors = data?.errors || [];

    throw uploadError;
  }
};

// FUNCIÓN PARA TRAER LOS DATOS FINANCIEROS DESDE LA BD
export const obtenerDatosPerfil = async (email) => {
  try {
    const response = await axios.get(`${PERFIL_URL}/datos`, {
      params: { email } // Enviamos el correo como parámetro para buscar al usuario
    });
    return response.data; // Retorna el JSON con moneda, ingresoMensual, saldoTotal, etc.
  } catch (error) {
    throw new Error(error.response?.data?.error || 'Error al obtener los datos del perfil');
  }
};

export const obtenerDashboard = async (usuarioNombre) => {
  try {
    const response = await axios.get(
      `${DASHBOARD_URL}/${encodeURIComponent(usuarioNombre)}`
    );

    return response.data;
  } catch (error) {
    throw new Error(
      error.response?.data?.message ||
      error.response?.data?.error ||
      'Error al obtener los datos del dashboard'
    );
  }
};

// FUNCIONES CENTRALIZADAS PARA LOGIN Y ESTADÍSTICAS
export const loginUsuario = async (credentials) => {
  try {
    const response = await axios.post(`${AUTH_URL}/login`, credentials);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Error en el inicio de sesión');
  }
};

export const obtenerEstadisticas = async (params) => {
  try {
    const response = await axios.get(ESTADISTICAS_URL, { params });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Error al obtener estadísticas');
  }
};