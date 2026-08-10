import { useState } from "react";
import "./Login.css";
import logo from "../assets/img/logo-financeai.jpg";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!email.trim() || !password.trim()) {
      alert("Por favor completa todos los campos.");
      return;
    }

    console.log("Login enviado:", { email, password });
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

            <button type="submit" className="btn-primary">
              Entrar
            </button>
          </form>

          <div className="form-links">
            <a href="#">Olvidé mi contraseña</a>
            <span className="divider"></span>
            <a href="#">Crear cuenta</a>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
