import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Perfil from './pages/Perfil';
import EstadisticasA from './pages/EstadisticasA';
import Recomendaciones from './pages/Recomendaciones';
import Dashboard from './pages/Dashboard';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Ruta principal (path="/" asigna Login como pantalla de inicio) */}
        <Route path="/" element={<Login />} />
        
        {/* Ruta para el Perfil */}
        <Route path="/perfil" element={<Perfil />} />

        {/* Ruta para Estadísticas */}
        <Route path="/estadisticas" element={<EstadisticasA />} />

        {/* Ruta para Recomendaciones */}
                <Route path="/recomendaciones" element={<Recomendaciones />} />

        {/* Ruta para Dashboard */}
                <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}