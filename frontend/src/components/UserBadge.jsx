import React, { useState, useEffect } from "react";
import avatarUser from "../assets/img/avatar-marcela.svg";
import "./UserBadge.css";

function UserBadge() {

  const [nombreUsuario, setNombreUsuario] = useState("--------");

  useEffect(() => {
    // Leemos el nombre guardado en el localStorage al iniciar sesión
    const usuarioLogueado = localStorage.getItem("usuarioNombre");
    if (usuarioLogueado) {
      setNombreUsuario(usuarioLogueado);
    }
  }, []);

  return (
    <div className="user-badge">
      <div className="user-badge-avatar">
        <img src={avatarUser} alt={nombreUsuario} />
      </div>
      <span className="user-badge-name">{nombreUsuario}</span>
    </div>
  );
}
export default UserBadge;
