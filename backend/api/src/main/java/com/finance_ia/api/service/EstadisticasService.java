package com.finance_ia.api.service;

import com.finance_ia.api.dto.EstadisticasResponseDto;
import com.finance_ia.api.dto.CategoryCardDto;
import com.finance_ia.api.model.TransaccionEntity;
import com.finance_ia.api.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    private final TransaccionRepository transaccionRepository;

    public EstadisticasService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public EstadisticasResponseDto obtenerDatosEstadisticos(String usuarioNombre, LocalDate inicio, LocalDate fin) {
        // 1. Obtener todas las transacciones del usuario en el rango seleccionado
        List<TransaccionEntity> transacciones = transaccionRepository
                .findByAnalisisFinanciero_UsuarioNombreAndFechaBetween(usuarioNombre, inicio, fin);

        // 2. Calcular el Top 3 de categorías para las tarjetas (independiente del gráfico)
        Map<String, Double> porCategoria = transacciones.stream()
                .collect(Collectors.groupingBy(
                        TransaccionEntity::getCategoria,
                        Collectors.summingDouble(TransaccionEntity::getValor)
                ));

        List<CategoryCardDto> top3 = porCategoria.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(e -> new CategoryCardDto(e.getKey(), "$" + String.format("%.2f", e.getValue())))
                .collect(Collectors.toList());

        // 3. Mapear las transacciones a un formato detallado para que el Frontend haga los cálculos de los botones
        List<EstadisticasResponseDto.TransaccionDetalleDto> detalle = transacciones.stream()
                .map(t -> new EstadisticasResponseDto.TransaccionDetalleDto(
                        t.getFecha().toString(),
                        t.getValor(),
                        t.getCategoria()
                ))
                .collect(Collectors.toList());

        return new EstadisticasResponseDto(detalle, top3);
    }
}