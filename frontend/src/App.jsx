import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Perfil from './pages/Perfil';
import EstadisticasA from './pages/EstadisticasA';

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
      </Routes>
    </BrowserRouter>
  );
}