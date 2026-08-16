import React, { useMemo } from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import "./GraficoGastos.css";

const formatCurrency = (val) => {
  const numericVal = typeof val === "string" ? parseFloat(val.replace(/[^0-9.-]+/g, "")) : val;
  if (isNaN(numericVal)) return "$0.00";
  return `$${numericVal.toFixed(2)}`;
};

const renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent }) => {
  if (!percent || percent < 0.03) return null;
  const RADIAN = Math.PI / 180;
  const radius = innerRadius + (outerRadius - innerRadius) * 0.8;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  return (
    <text
      x={x}
      y={y}
      fill="#ffffff"
      textAnchor="middle"
      dominantBaseline="central"
      fontSize="11"
    >
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

export const GraficoGastos = ({ datos = [], colores = [] }) => {
  const datosFinales = useMemo(() => {
    if (!Array.isArray(datos) || datos.length === 0) return [];

    const getNombre = (item) => item.categoria || item.descripcion || item.nombre || item.name || "";
    const getValor = (item) => {
      if (typeof item.monto !== "undefined") return Number(item.monto);
      if (typeof item.valor !== "undefined") return Number(item.valor);
      if (typeof item.value !== "undefined") return Number(item.value);
      return 0;
    };

    const normales = datos.filter(
      (item) => getNombre(item).trim().toLowerCase() !== "otros"
    );

    const otros = datos.filter(
      (item) => getNombre(item).trim().toLowerCase() === "otros"
    );

    normales.sort((a, b) => {
      const nameA = getNombre(a).trim();
      const nameB = getNombre(b).trim();
      return nameA.localeCompare(nameB, "es", { sensitivity: "base" });
    });

    return [...normales, ...otros].map((item) => ({
      ...item,
      displayName: getNombre(item) || "Sin Categoría",
      numericValue: getValor(item),
    }));
  }, [datos]);

  if (datosFinales.length === 0) {
    return (
      <div className="chart-placeholder empty-chart">
        <p>No hay gastos registrados para este período.</p>
      </div>
    );
  }

  return (
    <div className="chart-placeholder pie-placeholder">
      <ResponsiveContainer width="60%" height={200}>
        <PieChart>
          <Pie
            data={datosFinales}
            dataKey="numericValue"
            nameKey="displayName"
            cx="50%"
            cy="50%"
            outerRadius={95}
            stroke="#ffffff"
            strokeWidth={0.5}
            labelLine={false}
            label={renderCustomizedLabel}
          >
            {datosFinales.map((entry, index) => (
              <Cell
                key={entry.id || `cell-${entry.displayName}-${index}`}
                fill={colores[index % colores.length] || "#005F87"}
              />
            ))}
          </Pie>
          <Tooltip
            formatter={(value, name) => {
              const total = datosFinales.reduce((sum, item) => sum + item.numericValue, 0);
              const percent = total > 0 ? ((value / total) * 100).toFixed(0) : 0;

              return [`${formatCurrency(value)} (${percent}%)`, name];
            }}
          />
        </PieChart>
      </ResponsiveContainer>

      <div className="pie-legend-container">
        {datosFinales.map((entry, index) => {
          const color = colores[index % colores.length] || "#005F87";
          return (
            <div key={entry.id || `legend-${entry.displayName}-${index}`} className="legend-item">
              <span
                className="legend-color-dot"
                style={{ backgroundColor: color }}
                aria-hidden="true"
              />
              <span className="legend-text" title={entry.displayName}>
                {entry.displayName}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};