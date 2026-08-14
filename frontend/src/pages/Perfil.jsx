import React, { useState, useRef } from "react";
import { Icon } from "@iconify/react";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import { uploadCsvFile } from "/services/api"; // Importamos el servicio que conecta con el backend
import "./Perfil.css";

function Perfil() {
  // Estados para manejar el proceso de carga del CSV
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);

  // Referencia para activar el input de archivos de forma invisible mediante el botón
  const fileInputRef = useRef(null);

  const cuentaInfo = [
    { label: "Moneda", value: "USD Dólar", icon: "mdi:currency-usd" },
    { label: "Ingreso Mensual", value: "$2,000", icon: "mdi:cash" },
    { label: "Saldo Total", value: "$1,250", icon: "mdi:wallet-outline" },
    {
      label: "Nivel de Endeudamiento",
      value: "25%",
      icon: "mdi:percent-outline",
    },
  ];

  // Función que se ejecuta cuando el usuario hace clic en "Importar CSV"
  const handleImportClick = () => {
    fileInputRef.current.click(); // Simula el clic en el input de archivos oculto
  };

  // Función que procesa el archivo seleccionado y lo envía al backend
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validar que el archivo sea estrictamente .csv
    if (!file.name.endsWith('.csv')) {
      setMessage('Error: Por favor, selecciona un archivo con formato .csv');
      setIsError(true);
      return;
    }

    setLoading(true);
    setMessage('Subiendo y procesando CSV...');
    setIsError(false);

    try {
      // Llamada a la API programada en Spring Boot
      const responseMessage = await uploadCsvFile(file);

      setMessage(responseMessage); // Mensaje de éxito proveniente de la API
      setIsError(false);
    } catch (error) {
      setMessage(error.message || 'Ocurrió un error al subir el archivo.');
      setIsError(true);
    } finally {
      setLoading(false);
      e.target.value = null; // Limpiar el input para permitir subir el mismo archivo otra vez si es necesario
    }
  };

  return (
    <div className="perfil-layout">
      <Sidebar />

      <div className="perfil-main">
        <div className="perfil-dark-bg">
          <header className="perfil-topbar">
            <div className="search-box">
              <Icon icon="mdi:magnify" width="18" />
              <input type="text" placeholder="¿qué deseas buscar?" />
            </div>

            <UserBadge />
          </header>

          <section className="perfil-hero">
            <h1>Perfil</h1>
            <p>Administra tu cuenta</p>
          </section>

          <section className="perfil-content">
            <div className="perfil-card">
              <div className="perfil-card-left">
                <div className="avatar-placeholder">
                  <Icon icon="mdi:account-circle" width="48" />
                </div>
                <div>
                  <p className="user-name">Marcela G.</p>
                  <p className="user-email">marcela.g@finance.dev</p>
                </div>
              </div>
              <div className="perfil-card-right">
                <button className="btn-primary">Editar Perfil</button>
              </div>
            </div>

            <div className="perfil-card">
              <div className="perfil-card-left">
                <ul className="perfil-info-list">
                  {cuentaInfo.map((item) => (
                    <li key={item.label}>
                      <Icon icon={item.icon} width="18" />
                      <span className="info-label">{item.label}</span>
                      <span className="info-value">{item.value}</span>
                    </li>
                  ))}
                </ul>
              </div>
              <div className="perfil-card-right" style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>

                {/* Input de tipo file oculto que se activa mediante la referencia */}
                <input
                  type="file"
                  ref={fileInputRef}
                  onChange={handleFileChange}
                  accept=".csv"
                  style={{ display: 'none' }}
                />

                {/* Botón adaptado con la funcionalidad de importación */}
                <button
                  className="btn-primary"
                  onClick={handleImportClick}
                  disabled={loading}
                  style={{ cursor: loading ? 'not-allowed' : 'pointer', opacity: loading ? 0.7 : 1 }}
                >
                  {loading ? "Procesando..." : "Importar CSV"}
                </button>

                {/* Mensaje dinámico de estado (éxito o error) */}
                {message && (
                  <span style={{
                    fontSize: '12px',
                    color: isError ? '#ff6b6b' : '#51cf66',
                    maxWidth: '200px',
                    textAlign: 'right',
                    fontWeight: '500'
                  }}>
                    {message}
                  </span>
                )}

              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default Perfil;
