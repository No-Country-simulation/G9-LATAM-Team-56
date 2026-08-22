import React, { useState, useRef, useEffect } from "react";
import { Icon } from "@iconify/react";
import Sidebar from "../components/Sidebar";
import UserBadge from "../components/UserBadge";
import { uploadCsvFile, obtenerDatosPerfil } from "/services/api";
import "./Perfil.css";

function Perfil() {
  // Estados para manejar el proceso de carga del CSV
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);

  // Estados para almacenar el nombre y correo del usuario logueado
  const [nombreUsuario, setNombreUsuario] = useState("Usuario");
  const [correoUsuario, setCorreoUsuario] = useState("correo@finance.dev");


  // Estado para la información de la cuenta inicializado con valores por defecto
  const [cuentaInfo, setCuentaInfo] = useState([
      { label: "Moneda", value: "Cargando...", icon: "mdi:currency-usd" },
      { label: "Ingreso Mensual", value: "Cargando...", icon: "mdi:cash" },
      { label: "Gastos Total del Mes", value: "Cargando...", icon: "mdi:wallet-outline" },
      { label: "Saldo del Mes [Ahorro]", value: "Cargando...", icon: "mdi:piggy-bank-outline" },
      { label: "Nivel de Endeudamiento", value: "Cargando...", icon: "mdi:percent-outline" },
  ]);

  // Referencia para activar el input de archivos de forma invisible mediante el botón
  const fileInputRef = useRef(null);

  // useEffect para cargar los datos del usuario y sus finanzas desde la BD al abrir la página
  useEffect(() => {
    const nombreGuardado = localStorage.getItem("usuarioNombre");
    const correoGuardado = localStorage.getItem("usuarioEmail");

    if (nombreGuardado) setNombreUsuario(nombreGuardado);
    if (correoGuardado) {
        setCorreoUsuario(correoGuardado);

    // Llamamos al backend para traer los valores financieros reales de este correo
       obtenerDatosPerfil(correoGuardado)
         .then((data) => {
             // Actualizamos el arreglo con los datos que vinieron de la BD de Spring Boot
             setCuentaInfo([
                 { label: "Moneda", value: data.moneda, icon: "mdi:currency-usd" },
                 { label: "Ingreso Mensual", value: data.ingresoMensual, icon: "mdi:cash" },
                 { label: "Gastos Total del Mes", value: data.gastoTotal, icon: "mdi:wallet-outline" },
                 { label: "Saldo del Mes [Ahorro]", value: data.saldoTotal, icon: "mdi:piggy-bank-outline" },
                 { label: "Nivel de Endeudamiento", value: data.nivelEndeudamiento, icon: "mdi:percent-outline" },
             ]);
             // Evaluamos si el usuario ya cuenta con un ingreso registrado en la BD al iniciar sesión
             const tieneDatos = data.ingresoMensual &&
                                data.ingresoMensual !== "$0.0" &&
                                data.ingresoMensual !== "$0" &&
                                data.ingresoMensual !== "Cargando...";

             if (tieneDatos) {
                 localStorage.setItem("csvCargado", "true");
             } else {
                 localStorage.setItem("csvCargado", "false"); // Forzamos a falso si no hay datos reales
             }
         })
         .catch((err) => {
             console.error("No se pudieron cargar los datos financieros:", err);
             // Valores de respaldo en caso de que ocurra un error de conexión
             setCuentaInfo([
                 { label: "Moneda", value: "USD Dólar", icon: "mdi:currency-usd" },
                 { label: "Ingreso Mensual", value: "$0.0", icon: "mdi:cash" },
                 { label: "Gastos Total del Mes", value: "$0.0", icon: "mdi:wallet-outline" },
                 { label: "Saldo del Mes [Ahorro]", value: "$0.00", icon: "mdi:piggy-bank-outline" },
                 { label: "Nivel de Endeudamiento", value: "0%", icon: "mdi:percent-outline" },
             ]);
             localStorage.setItem("csvCargado", "false"); // Si falla, marcamos que no hay datos
         });
       }
  }, []);

  // Función que se ejecuta cuando el usuario hace clic en "Importar CSV"
  const handleImportClick = () => {
    fileInputRef.current.click();
  };

  // Función que procesa el archivo seleccionado y lo envía al backend
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validar que el archivo sea estrictamente .csv
    if (!file.name.endsWith('.csv')) {
      setMessage('Error: Por favor, selecciona un archivo con formato .csv');
      setIsError(true);
      // Ocultar el mensaje de error automáticamente después de 3 segundos
      setTimeout(() => {
          setMessage("");
      }, 3000);
      return;
    }

    setLoading(true);
    setMessage('Subiendo y procesando CSV...');
    setIsError(false);

    try {
      // Llamada a la API enviando tanto el archivo como el nombre del usuario
      const responseMessage = await uploadCsvFile(file, nombreUsuario);

      setMessage('¡CSV importado con éxito!');
      localStorage.setItem("csvCargado", "true");
      setIsError(false);

      // Recargar los datos del perfil automáticamente tras subir el CSV
      const datosNuevos = await obtenerDatosPerfil(correoUsuario);
      setCuentaInfo([
          { label: "Moneda", value: datosNuevos.moneda, icon: "mdi:currency-usd" },
          { label: "Ingreso Mensual", value: datosNuevos.ingresoMensual, icon: "mdi:cash" },
          { label: "Gastos Total del Mes", value: datosNuevos.gastoTotal, icon: "mdi:wallet-outline" },
          { label: "Saldo del Mes [Ahorro]", value: datosNuevos.saldoTotal, icon: "mdi:piggy-bank-outline" },
          { label: "Nivel de Endeudamiento", value: datosNuevos.nivelEndeudamiento, icon: "mdi:percent-outline" },
      ]);

    } catch (error) {
      setMessage(error.message || 'Ocurrió un error al subir el archivo.');
      setIsError(true);
    } finally {
      setLoading(false);
      e.target.value = null; // Limpiar el input para permitir subir el mismo archivo otra vez
      // Ocultar el mensaje de éxito o error (del try/catch) después de 3 segundos
      setTimeout(() => {
          setMessage("");
      }, 3000);
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
                  {/* Mostramos dinámicamente el nombre y correo obtenidos del login */}
                  <p className="user-name">{nombreUsuario}</p>
                  <p className="user-email">{correoUsuario}</p>
                </div>
              </div>
              <div className="perfil-card-right">
                   {/*<button className="btn-primary">Editar Perfil</button>*/}
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