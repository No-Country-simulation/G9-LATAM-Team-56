// frontend/src/services/api.js

// URL base de tu backend de Spring Boot
const API_BASE_URL = 'http://localhost:8080/api/csv';

/**
 * Envía un archivo CSV al backend para su procesamiento y guardado en MySQL.
 * @param {File} file - El archivo CSV seleccionado por el usuario.
 * @returns {Promise<string>} - Mensaje de éxito del servidor.
 */

export const uploadCsvFile = async (file) => {
  const formData = new FormData();
  // 'file' debe coincidir exactamente con el @RequestParam("file") del backend
  formData.append('file', file);

  try {
    const response = await fetch(`${API_BASE_URL}/upload`, {
      method: 'POST',
      body: formData,
    });

    const data = await response.text();

    if (!response.ok) {
      throw new Error(data || 'Error al subir el archivo CSV.');
    }

    return data;
  } catch (error) {
    console.error('Error en el servicio de API:', error);
    throw error;
  }
};