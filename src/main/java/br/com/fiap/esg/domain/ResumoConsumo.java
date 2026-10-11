package br.com.fiap.esg.domain;

import org.springframework.data.annotation.Id;

/** Resultado do pipeline de agregacao (media de kWh e total de CO2 por medidor). */
public record ResumoConsumo(
        @Id String macDispositivo,
        double consumoMedioKwh,
        double totalCo2Kg,
        long totalLeituras) {}
