package com.finance_ia.api.service;

import org.springframework.stereotype.Component;

@Component
public class PerfilFinancieroMapper {

  public String toResponse(String perfil) {

    if (perfil == null) {
      return null;
    }

    return switch (
        perfil
            .trim()
            .toUpperCase()
            .replace("Ó", "O")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ú", "U")
            .replace("Á", "A")
        ) {

      case "SALUDABLE" ->
          "Saludable";

      case "EN OBSERVACION" ->
          "En observación";

      case "RIESGO" ->
          "En riesgo";

      default ->
          throw new IllegalArgumentException(
              "Perfil financiero desconocido: " + perfil
          );
    };
  }
}