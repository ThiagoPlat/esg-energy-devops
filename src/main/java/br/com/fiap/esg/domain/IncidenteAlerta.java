package br.com.fiap.esg.domain;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Anomalias operacionais e picos de sobrecarga. */
@Document("incidentes_alertas")
public record IncidenteAlerta(
        @Id String id,
        @NotBlank String protocolo,
        @NotBlank String macDispositivo,
        String nivelSeveridade,
        String descricao,
        String status,
        Instant dataOcorrencia,
        Map<String, Object> atributos) {}
