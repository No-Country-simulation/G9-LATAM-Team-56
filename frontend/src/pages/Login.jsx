import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { loginUsuario } from '/services/api'
import axios from "axios";
import "./Login.css";
import logo from "../assets/img/logo-financeai.jpg";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // Efecto para hacer que la alerta de error desaparezca a los 3 segundos
  useEffect(() => {
    if (errorMessage) {
      const timer = setTimeout(() => {
        setErrorMessage("");
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [errorMessage]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    if (!email.trim() || !password.trim()) {
      setErrorMessage("Por favor completa todos los campos.");
      return;
    }

    setLoading(true);

    try {
      // Petición al backend en Spring Boot
      const response = await loginUsuario({
        email,
        password
      });

      // Guardamos datos en localStorage y redirigimos
      localStorage.setItem("usuarioNombre", response.nombre);
      localStorage.setItem("usuarioEmail", response.email);

      navigate("/dashboard");

    } catch (error) {
      console.error("Error en el login:", error);
      const mensajeError = error.response?.data?.error || "Error al conectar con el servidor.";
      setErrorMessage(mensajeError);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="left-panel">
        <a
          href="https://github.com/No-Country-simulation/G9-LATAM-Team-56"
          target="_blank"
          rel="noopener noreferrer"
          className="team-tag"
        >
          G9-LATAM-Team-56
        </a>
        <h1>Asistente Inteligente de Salud Financiera</h1>
        <p>
          Transforma datos financieros brutos en conocimiento claro, visual y
          accionable.
        </p>
      </div>

      <div className="right-panel">
        <div className="form-wrapper">
          <img src={logo} alt="financeAI" className="logo" />

          <h2>Iniciar Sesión</h2>

          <form onSubmit={handleSubmit}>
            <label htmlFor="email">Correo electrónico</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="ejemplo.correo@dominio.com"
              required
            />

            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />

            <button
              type="submit"
              className="btn-primary"
              disabled={loading}
              style={{ opacity: loading ? 0.7 : 1, cursor: loading ? 'not-allowed' : 'pointer' }}
            >
              {loading ? "Validando..." : "Entrar"}
            </button>
          </form>

          {/* Alerta visual de error que se oculta sola a los 3 segundos */}
          {errorMessage && (
            <p style={{ color: "#ff6b6b", fontSize: "13px", marginTop: "10px", textAlign: "center", fontWeight: "500" }}>
              {errorMessage}
            </p>
          )}
          {/*<div className="form-links">
            <a href="#">Olvidé mi contraseña</a>
            <span className="divider"></span>
            <a href="#">Crear cuenta</a>
          </div>*/}
        </div>
      </div>
    </div>
  );
}

export default Login;