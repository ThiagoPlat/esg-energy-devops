package br.com.fiap.esg.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Serie temporal de consumo (kWh negativo = geracao solar). */
@Document("leituras_consumo")
public record LeituraConsumo(
        @Id String id,
        @NotBlank String macDispositivo,
        @NotNull Instant timestamp,
        @NotNull Double consumoKwh,
        Double emissaoEstimadaCo2Kg,
        Map<String, Object> atributos) {}
