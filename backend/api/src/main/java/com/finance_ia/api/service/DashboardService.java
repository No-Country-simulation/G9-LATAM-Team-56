package com.finance_ia.api.service;

import com.finance_ia.api.dto.DashboardResponse;
import com.finance_ia.api.dto.DashboardTransaccionDto;
import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import com.finance_ia.api.model.AnalisisFinancieroEntity;
import com.finance_ia.api.model.RecomendacionEntity;
import com.finance_ia.api.model.TransaccionEntity;
import com.finance_ia.api.repository.AnalisisFinancieroRepository;
import com.finance_ia.api.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardService {

    private final AnalisisFinancieroRepository analisisRepository;
    private final TransaccionRepository transaccionRepository;

    public DashboardService(
            AnalisisFinancieroRepository analisisRepository, TransaccionRepository transaccionRepository
    ) {
        this.analisisRepository = analisisRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public DashboardResponse obtenerDashboard(
            String usuarioNombre
    ) {

        if (usuarioNombre == null || usuarioNombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario es obligatorio"
            );
        }

        // Buscamos mediante Optional para manejar el caso de que no exista
        Optional<AnalisisFinancieroEntity> analisisOpt =
                analisisRepository.findByUsuarioNombre(usuarioNombre);

        // Si no existe el análisis, devolvemos un DTO con valores por defecto (vacíos/ceros)
        if (analisisOpt.isEmpty()) {
            return new DashboardResponse(
                    "Desconocido", // perfilFinanciero
                    0.0,              // probabilidad
                    0.0,              // ingresoMensual
                    0.0,              // nivelEndeudamiento
                    "Sin registrar",  // frecuenciaAhorro
                    0.0,              // saldoTotal
                    new HashMap<>(),  // resumenGastos vacíos
                    List.of(),        // transacciones vacías
                    List.of()         // recomendaciones vacías
            );
        }

        AnalisisFinancieroEntity analisis = analisisOpt.get();

        Map<String, Double> resumenGastos =
                construirResumenGastos(
                        analisis.getTransacciones()
                );

        List<DashboardTransaccionDto> transacciones =
                convertirTransacciones(
                        analisis.getTransacciones(),
                        usuarioNombre
                );

        List<RecommendationResultDto> recomendaciones =
                convertirRecomendaciones(
                        analisis.getRecomendaciones()
                );

        return new DashboardResponse(
                analisis.getPerfilFinanciero(),
                analisis.getProbabilidad(),
                analisis.getIngresoMensual(),
                analisis.getNivelEndeudamiento(),
                analisis.getFrecuenciaAhorro(),
                analisis.getSaldoTotal(),
                resumenGastos,
                transacciones,
                recomendaciones
        );
    }

    /**
     * Construye el resumen de gastos correspondiente al mes calendario actual.
     *
     * <p>Este resumen Se limita al mes actual para que sea coherente con los indicadores
     * mensuales del dashboard, como el ingreso mensual.</p>
     */
    private Map<String, Double> construirResumenGastos(
            List<TransaccionEntity> transacciones
    ) {

        Map<String, Double> resumenGastos = new HashMap<>();

        if (transacciones == null) {
            return resumenGastos;
        }

        LocalDate hoy = LocalDate.now();

        YearMonth mesActual = YearMonth.from(hoy);

        for (TransaccionEntity transaccion : transacciones) {

            if (transaccion.getFecha() == null) {
                continue;
            }

            YearMonth mesTransaccion =
                    YearMonth.from(transaccion.getFecha());

            if (!mesActual.equals(mesTransaccion)) {
                continue;
            }

            if (transaccion.getCategoria() == null) {
                continue;
            }

            resumenGastos.merge(
                    transaccion.getCategoria(),
                    transaccion.getValor(),
                    Double::sum
            );
        }

        return resumenGastos;
    }

    private List<RecommendationResultDto> convertirRecomendaciones(
            List<RecomendacionEntity> recomendaciones
    ) {

        if (recomendaciones == null) {
            return List.of();
        }

        return recomendaciones.stream()
                .map(recomendacion ->
                        new RecommendationResultDto(
                                recomendacion.getCategoria(),
                                recomendacion.getMensaje()
                        )
                )
                .toList();
    }

    private List<DashboardTransaccionDto> convertirTransacciones(
            List<TransaccionEntity> transacciones, String usuarioNombre
    ) {

        if (transacciones == null) {
            return List.of();
        }

        return transaccionRepository.findTop10ByAnalisisFinanciero_UsuarioNombreOrderByFechaDesc(usuarioNombre)
                .stream()
                .map(transaccion ->
                        new DashboardTransaccionDto(
                                transaccion.getCategoria(),
                                transaccion.getDescripcion(),
                                transaccion.getValor(),
                                transaccion.getFecha()
                        )
                )
                .toList();


    }


}
