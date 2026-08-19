package com.finance_ia.api.service;

import com.finance_ia.api.dto.TransaccionRequest;
import com.finance_ia.api.infra.exception.ErrorDetail;
import com.finance_ia.api.infra.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialValidationService {

    // Reglas de negocio definidas para los datos de entrada del análisis financiero.
// El nivel de endeudamiento se expresa como porcentaje (0-100).
    private static final double MIN_ENDEUDAMIENTO = 0.0;
    private static final double MAX_ENDEUDAMIENTO = 100.0;

    // Valores aceptados por el modelo de perfil financiero.
    private static final List<String> FRECUENCIAS_AHORRO_VALIDAS =
            List.of("alta", "media", "baja");

    public void validarDatosFinancieros(
            Double ingresoMensual,
            Double nivelEndeudamiento,
            String frecuenciaAhorro
    ) {

        List<ErrorDetail> errors = new ArrayList<>();

        validarIngresoMensual(
                ingresoMensual,
                errors
        );

        validarNivelEndeudamiento(
                nivelEndeudamiento,
                errors
        );

        validarFrecuenciaAhorro(
                frecuenciaAhorro,
                errors
        );

        lanzarSiHayErrores(errors);
    }

    public void validarTransaccion(
            TransaccionRequest transaccion
    ) {

        List<ErrorDetail> errors = new ArrayList<>();

        validarTransaccionInterna(
                transaccion,
                "",
                errors
        );

        lanzarSiHayErrores(errors);
    }

    public void validarTransacciones(
            List<TransaccionRequest> transacciones
    ) {

        List<ErrorDetail> errors = new ArrayList<>();

        if (transacciones == null) {

            errors.add(new ErrorDetail(
                    "transacciones",
                    "La lista de transacciones es obligatoria.",
                    null
            ));

            lanzarSiHayErrores(errors);
            return;
        }

        if (transacciones.isEmpty()) {

            errors.add(new ErrorDetail(
                    "transacciones",
                    "Debe existir al menos una transacción.",
                    null
            ));

            lanzarSiHayErrores(errors);
            return;
        }

        for (int i = 0; i < transacciones.size(); i++) {

            validarTransaccionInterna(
                    transacciones.get(i),
                    "transacciones[" + i + "].",
                    errors
            );
        }

        lanzarSiHayErrores(errors);
    }

    private void validarTransaccionInterna(
            TransaccionRequest transaccion,
            String fieldPrefix,
            List<ErrorDetail> errors
    ) {

        if (transaccion == null) {

            errors.add(new ErrorDetail(
                    fieldPrefix.isEmpty()
                            ? "transaccion"
                            : fieldPrefix.substring(
                            0,
                            fieldPrefix.length() - 1
                    ),
                    "La transacción es obligatoria.",
                    null
            ));

            return;
        }

        validarDescripcion(
                transaccion.getDescripcion(),
                fieldPrefix,
                errors
        );

        validarValor(
                transaccion.getValor(),
                fieldPrefix,
                errors
        );

        validarFecha(
                transaccion.getFecha(),
                fieldPrefix,
                errors
        );
    }

    private void validarIngresoMensual(
            Double ingresoMensual,
            List<ErrorDetail> errors
    ) {

        if (ingresoMensual == null) {

            errors.add(new ErrorDetail(
                    "ingreso_mensual",
                    "El ingreso mensual es obligatorio.",
                    null
            ));

            return;
        }

        if (ingresoMensual < 0) {

            errors.add(new ErrorDetail(
                    "ingreso_mensual",
                    "El ingreso mensual debe ser mayor o igual a 0.",
                    null
            ));
        }
    }

    private void validarNivelEndeudamiento(
            Double nivelEndeudamiento,
            List<ErrorDetail> errors
    ) {

        if (nivelEndeudamiento == null) {

            errors.add(new ErrorDetail(
                    "nivel_endeudamiento",
                    "El nivel de endeudamiento es obligatorio.",
                    null
            ));

            return;
        }

        if (
                nivelEndeudamiento < MIN_ENDEUDAMIENTO
                        || nivelEndeudamiento > MAX_ENDEUDAMIENTO
        ) {

            errors.add(new ErrorDetail(
                    "nivel_endeudamiento",
                    "El nivel de endeudamiento debe estar entre 0 y 100.",
                    null
            ));
        }
    }

    private void validarFrecuenciaAhorro(
            String frecuenciaAhorro,
            List<ErrorDetail> errors
    ) {

        if (
                frecuenciaAhorro == null
                        || frecuenciaAhorro.trim().isEmpty()
        ) {

            errors.add(new ErrorDetail(
                    "frecuencia_ahorro",
                    "La frecuencia de ahorro es obligatoria.",
                    null
            ));

            return;
        }

        String frecuenciaNormalizada =
                frecuenciaAhorro.trim().toLowerCase();

        if (!FRECUENCIAS_AHORRO_VALIDAS.contains(
                frecuenciaNormalizada
        )) {

            errors.add(new ErrorDetail(
                    "frecuencia_ahorro",
                    "La frecuencia de ahorro debe ser alta, media o baja.",
                    null
            ));
        }
    }

    private void validarDescripcion(
            String descripcion,
            String fieldPrefix,
            List<ErrorDetail> errors
    ) {

        if (
                descripcion == null
                        || descripcion.trim().isEmpty()
        ) {

            errors.add(new ErrorDetail(
                    fieldPrefix + "descripcion",
                    "La descripción de la transacción es obligatoria.",
                    null
            ));
        }
    }

    private void validarValor(
            Double valor,
            String fieldPrefix,
            List<ErrorDetail> errors
    ) {

        if (valor == null) {

            errors.add(new ErrorDetail(
                    fieldPrefix + "valor",
                    "El valor de la transacción es obligatorio.",
                    null
            ));

            return;
        }

        if (valor <= 0) {

            errors.add(new ErrorDetail(
                    fieldPrefix + "valor",
                    "El valor de la transacción debe ser mayor que 0.",
                    null
            ));
        }
    }

    private void validarFecha(
            LocalDate fecha,
            String fieldPrefix,
            List<ErrorDetail> errors
    ) {

        if (fecha == null) {

            errors.add(new ErrorDetail(
                    fieldPrefix + "fecha",
                    "La fecha de la transacción es obligatoria.",
                    null
            ));
        }
    }

    private void lanzarSiHayErrores(
            List<ErrorDetail> errors
    ) {

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public List<ErrorDetail> validarRegistroCsv(
            String ingresoMensual,
            String nivelEndeudamiento,
            String frecuenciaAhorro,
            String descripcion,
            String valor,
            String fecha,
            int row
    ) {

        List<ErrorDetail> errors = new ArrayList<>();

        validarIngresoMensualCsv(
                ingresoMensual,
                row,
                errors
        );

        validarNivelEndeudamientoCsv(
                nivelEndeudamiento,
                row,
                errors
        );

        validarFrecuenciaAhorroCsv(
                frecuenciaAhorro,
                row,
                errors
        );

        validarDescripcionCsv(
                descripcion,
                row,
                errors
        );

        validarValorCsv(
                valor,
                row,
                errors
        );

        validarFechaCsv(
                fecha,
                row,
                errors
        );

        return errors;
    }

    private void validarIngresoMensualCsv(
            String valor,
            int row,
            List<ErrorDetail> errors
    ) {

        if (valor == null || valor.trim().isEmpty()) {

            errors.add(new ErrorDetail(
                    "ingreso_mensual",
                    "El ingreso mensual es obligatorio.",
                    row
            ));

            return;
        }

        try {

            double ingreso = Double.parseDouble(
                    valor.trim()
            );

            if (ingreso < 0) {

                errors.add(new ErrorDetail(
                        "ingreso_mensual",
                        "El ingreso mensual debe ser mayor o igual a 0.",
                        row
                ));
            }

        } catch (NumberFormatException e) {

            errors.add(new ErrorDetail(
                    "ingreso_mensual",
                    "El ingreso mensual debe ser un número válido.",
                    row
            ));
        }
    }

    private void validarNivelEndeudamientoCsv(
            String valor,
            int row,
            List<ErrorDetail> errors
    ) {

        if (valor == null || valor.trim().isEmpty()) {

            errors.add(new ErrorDetail(
                    "nivel_endeudamiento",
                    "El nivel de endeudamiento es obligatorio.",
                    row
            ));

            return;
        }

        try {

            double endeudamiento =
                    Double.parseDouble(valor.trim());

            if (
                    endeudamiento < MIN_ENDEUDAMIENTO
                            || endeudamiento > MAX_ENDEUDAMIENTO
            ) {

                errors.add(new ErrorDetail(
                        "nivel_endeudamiento",
                        "El nivel de endeudamiento debe estar entre 0 y 100.",
                        row
                ));
            }

        } catch (NumberFormatException e) {

            errors.add(new ErrorDetail(
                    "nivel_endeudamiento",
                    "El nivel de endeudamiento debe ser un número válido.",
                    row
            ));
        }
    }

    private void validarDescripcionCsv(
            String descripcion,
            int row,
            List<ErrorDetail> errors
    ) {

        if (
                descripcion == null
                        || descripcion.trim().isEmpty()
        ) {

            errors.add(new ErrorDetail(
                    "descripcion",
                    "La descripción de la transacción es obligatoria.",
                    row
            ));
        }
    }

    private void validarValorCsv(
            String valor,
            int row,
            List<ErrorDetail> errors
    ) {

        if (valor == null || valor.trim().isEmpty()) {

            errors.add(new ErrorDetail(
                    "valor",
                    "El valor de la transacción es obligatorio.",
                    row
            ));

            return;
        }

        try {

            double valorNumerico =
                    Double.parseDouble(valor.trim());

            if (valorNumerico <= 0) {

                errors.add(new ErrorDetail(
                        "valor",
                        "El valor de la transacción debe ser mayor que 0.",
                        row
                ));
            }

        } catch (NumberFormatException e) {

            errors.add(new ErrorDetail(
                    "valor",
                    "El valor de la transacción debe ser un número válido.",
                    row
            ));
        }
    }

    private void validarFechaCsv(
            String fecha,
            int row,
            List<ErrorDetail> errors
    ) {

        if (fecha == null || fecha.trim().isEmpty()) {

            errors.add(new ErrorDetail(
                    "fecha",
                    "La fecha de la transacción es obligatoria.",
                    row
            ));

            return;
        }

        try {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d/M/yyyy");

            LocalDate.parse(
                    fecha.trim(),
                    formatter
            );

        } catch (DateTimeParseException e) {

            errors.add(new ErrorDetail(
                    "fecha",
                    "La fecha debe tener un formato válido (d/M/yyyy).",
                    row
            ));
        }
    }

    private void validarFrecuenciaAhorroCsv(
            String frecuencia,
            int row,
            List<ErrorDetail> errors
    ) {

        if (
                frecuencia == null
                        || frecuencia.trim().isEmpty()
        ) {

            errors.add(new ErrorDetail(
                    "frecuencia_ahorro",
                    "La frecuencia de ahorro es obligatoria.",
                    row
            ));

            return;
        }

        String normalizada =
                frecuencia.trim().toLowerCase();

        if (!FRECUENCIAS_AHORRO_VALIDAS.contains(normalizada)) {

            errors.add(new ErrorDetail(
                    "frecuencia_ahorro",
                    "La frecuencia de ahorro debe ser alta, media o baja.",
                    row
            ));
        }
    }
}
