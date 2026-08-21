import React, { useMemo } from "react";
import { PieChart, Pie, Cell, Tooltip } from "recharts";
import "./PieChartWithNeedle.css";

const RADIAN = Math.PI / 180;

const renderNeedle = (value = 0, data = [], cx, cy, iR, oR, color) => {
  const total = data.reduce((acc, curr) => acc + (curr.value || 0), 0) || 100;
  const clampedValue = Math.max(0, Math.min(value, total));
  const ang = 180 * (1 - clampedValue / total);

  const length = (iR + oR) / 2;

  const sin = Math.sin(-RADIAN * ang);
  const cos = Math.cos(-RADIAN * ang);
  const r = 5;
  const x0 = cx;
  const y0 = cy;
  const xba = x0 + r * sin;
  const yba = y0 - r * cos;
  const xbb = x0 - r * sin;
  const ybb = y0 + r * cos;
  const xp = x0 + length * cos;
  const yp = y0 + length * sin;

  return (
    <g key="needle-layer">
      <circle cx={x0} cy={y0} r={r} fill={color} stroke="none" />
      <path d={`M${xba} ${yba} L${xbb} ${ybb} L${xp} ${yp} Z`} fill={color} />
    </g>
  );
};

const renderGaugeLabels = (cx = 160, cy = 145) => (
  <g key="gauge-labels" className="gauge-labels">
    <text x={cx - 128} y={cy - 12} textAnchor="middle">
      <tspan x={cx - 128} dy="0">Mala</tspan>
      <tspan x={cx - 128} dy="14">Salud</tspan>
    </text>
    <text x={cx} y={cy - 125} textAnchor="middle">
      <tspan x={cx} dy="0">Salud</tspan>
      <tspan x={cx} dy="14">Media</tspan>
    </text>
    <text x={cx + 128} y={cy - 12} textAnchor="middle">
      <tspan x={cx + 128} dy="0">Buena</tspan>
      <tspan x={cx + 128} dy="14">Salud</tspan>
    </text>
  </g>
);

export const PieChartWithNeedle = ({ scoreValue = 0, gaugeData = [] }) => {
  const cx = 160;
  const cy = 145;
  const iR = 65;
  const oR = 100;

  const needleSvg = useMemo(
    () => renderNeedle(scoreValue, gaugeData, cx, cy, iR, oR, "#0B2239"),
    [scoreValue, gaugeData, cx, cy, iR, oR]
  );

  return (
    <div className="chart-placeholder gauge-placeholder">
      <PieChart width={320} height={180} style={{ overflow: "visible", backgroundColor: "transparent" }}>
        <Pie
          dataKey="value"
          startAngle={180}
          endAngle={0}
          data={gaugeData}
          cx={cx - 6}
          cy={cy - 3}
          innerRadius={iR}
          outerRadius={oR}
          stroke="#none"
          paddingAngle={0.5}
        >
          {gaugeData.map((entry, index) => (
            <Cell
              key={entry.name || `cell-${index}`}
              fill={entry.color || "#005F87"}
            />
          ))}
        </Pie>
        {renderGaugeLabels(cx, cy)}
        <Tooltip content={() => null} />
      </PieChart>

      <svg
        width={320}
        height={180}
        className="gauge-needle-overlay"
      >
        {needleSvg}
      </svg>
    </div>
  );
};