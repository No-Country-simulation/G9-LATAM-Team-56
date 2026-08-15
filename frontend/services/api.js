import axios from 'axios';

// URLs de backend en Spring Boot
const API_URL = 'http://localhost:8080/api/csv';
const AUTH_URL = 'http://localhost:8080/api/auth';
const PERFIL_URL = 'http://localhost:8080/api/perfil';

export const uploadCsvFile = async (file, usuario) => {
  // Creamos un objeto FormData para enviar archivos y texto juntos
  const formData = new FormData();
  formData.append('file', file);
  formData.append('usuario', usuario); // <- Aquí enviamos el parámetro que pide tu @RequestParam("usuario")

  try {
    const response = await axios.post(`${API_URL}/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Error al conectar con el servidor');
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