import React, { useState, useRef, useEffect } from "react";
import { Icon } from "@iconify/react";
import "./CsvUploadFeedback.css";

const fieldLabels = {
    ingreso_mensual: "Ingreso mensual",
    nivel_endeudamiento: "Nivel de endeudamiento",
    frecuencia_ahorro: "Frecuencia de ahorro",
    descripcion: "Descripción",
    valor: "Valor",
    fecha: "Fecha"
};

const MAX_VISIBLE_ERRORS = 5;

function CsvUploadFeedback({ status, type, message, errors = [] }) {
    const [showDetails, setShowDetails] = useState(false);
    const scrollRef = useRef(null);

    useEffect(() => {
        const container = scrollRef.current;
        if (!container) return;

        const handleWheel = (e) => {
            e.preventDefault();
            const scrollSpeed = 0.15;
            container.scrollTop += e.deltaY * scrollSpeed;
        };

        container.addEventListener("wheel", handleWheel, { passive: false });

        return () => {
            container.removeEventListener("wheel", handleWheel);
        };
    }, [showDetails]);

    //  AGREGA / VERIFICA ESTA FUNCIÓN DENTRO DEL COMPONENTE 
    const handleDownloadReport = () => {
        if (!errors || errors.length === 0) return;

        const date = new Date().toLocaleString();
        let reportContent = `========================================\n`;
        reportContent += `  REPORTE DE ERRORES - CARGA DE CSV\n`;
        reportContent += `  Fecha: ${date}\n`;
        reportContent += `  Total de errores encontrados: ${errors.length}\n`;
        reportContent += `========================================\n\n`;

        errors.forEach((err, index) => {
            const rowStr = err.row ? `Fila ${err.row}` : "Archivo general";
            const fieldStr = err.field ? ` [Campo: ${fieldLabels[err.field] || err.field}]` : "";
            reportContent += `${index + 1}. ${rowStr}${fieldStr}: ${err.message}\n`;
        });

        reportContent += `\n----------------------------------------\n`;
        reportContent += `Por favor, corrija estos datos en su archivo y vuelva a intentarlo.`;

        const blob = new Blob([reportContent], { type: "text/plain;charset=utf-8" });
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = `reporte_errores_csv_${Date.now()}.txt`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    };

    if (status === "idle" || status === "loading") {
        return null;
    }

    if (status === "success") {
        return (
            <div className="csv-feedback csv-feedback-success" role="status">
                <Icon icon="mdi:check-circle-outline" width="18" />
                <span>{message}</span>
            </div>
        );
    }

    if (type === "server") {
        return (
            <div className="csv-feedback csv-feedback-error" role="alert">
                <div className="csv-feedback-content">
                    <div className="csv-feedback-main">
                        <Icon
                            icon="mdi:alert-circle-outline"
                            width="18"
                            className="csv-feedback-icon"
                        />
                        <span>{message || "No se pudo procesar el archivo."}</span>
                    </div>
                    <span className="csv-feedback-hint">
                        Por favor, intente nuevamente.
                    </span>
                </div>
            </div>
        );
    }

    if (type === "validation") {
        const totalErrors = errors.length;
        const visibleErrors = errors.slice(0, MAX_VISIBLE_ERRORS);
        const hasMoreErrors = totalErrors > MAX_VISIBLE_ERRORS;

        return (
            <div className="csv-feedback csv-feedback-warning" role="alert">
                <div className="csv-feedback-header">
                    <Icon
                        icon="mdi:alert-circle-outline"
                        width="18"
                        style={{ flexShrink: 0 }}
                    />
                    <span className="csv-feedback-title">
                        Error al cargar el archivo.
                    </span>
                </div>

                <span className="csv-feedback-summary">
                    {totalErrors === 1
                        ? "Se encontró 1 error de formato."
                        : `Se encontraron ${totalErrors} errores de formato.`}
                </span>

                {totalErrors > 0 && (
                    <button
                        type="button"
                        className="csv-feedback-details-button"
                        onClick={() => setShowDetails(!showDetails)}
                    >
                        {showDetails ? "Ocultar detalles" : "Ver detalles"}
                    </button>
                )}

                <div className={`csv-feedback-accordion ${showDetails ? "is-open" : ""}`}>
                    <div className="csv-feedback-accordion-inner">
                        {/* Solo la lista de errores lleva scroll */}
                        <div className="csv-feedback-details" ref={scrollRef}>
                            {visibleErrors.map((error, index) => (
                                <div
                                    className="csv-feedback-detail"
                                    key={`${error.field}-${error.row}-${index}`}
                                >
                                    <div className="csv-feedback-detail-title">
                                        {error.row ? `Fila ${error.row}` : "Archivo"}
                                        {error.field && ` · ${fieldLabels[error.field] || error.field}`}
                                    </div>
                                    <div className="csv-feedback-detail-message">
                                        {error.message}
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/* El pie con el botón se mantiene fijo afuera del scroll */}
                        {hasMoreErrors && (
                            <div className="csv-feedback-more-errors">
                                <span>Se muestran {MAX_VISIBLE_ERRORS} de {totalErrors} errores.</span>
                                <button
                                    type="button"
                                    className="csv-feedback-download-btn"
                                    onClick={handleDownloadReport}
                                    title="Descargar reporte completo (.txt)"
                                >
                                    <Icon icon="mdi:download" width="14" />
                                    <span>Reporte (.txt)</span>
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="csv-feedback csv-feedback-error" role="alert">
            <Icon icon="mdi:alert-circle-outline" width="18" />
            <span>{message || "Ocurrió un error inesperado."}</span>
        </div>
    );
}

export default CsvUploadFeedback;