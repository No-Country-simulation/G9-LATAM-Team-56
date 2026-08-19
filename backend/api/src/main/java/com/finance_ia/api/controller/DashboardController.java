package com.finance_ia.api.controller;

import com.finance_ia.api.dto.DashboardResponse;
import com.finance_ia.api.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        if (dashboardService == null) {
            throw new IllegalArgumentException(
                    "El servicio del dashboard es obligatorio"
            );
        }

        this.dashboardService = dashboardService;
    }

    @GetMapping("/{usuarioNombre}")
    public ResponseEntity<DashboardResponse> obtenerDashboard(
            @PathVariable String usuarioNombre
    ) {

        DashboardResponse response =
                dashboardService.obtenerDashboard(usuarioNombre);

        return ResponseEntity.ok(response);
    }
}
