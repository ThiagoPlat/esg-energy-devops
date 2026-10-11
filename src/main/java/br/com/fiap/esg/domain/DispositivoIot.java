package br.com.fiap.esg.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Parque de sensores/equipamentos com especificacoes heterogeneas. */
@Document("dispositivos_iot")
public record DispositivoIot(
        @Id String id,
        @NotBlank String macAddress,
        String modelo,
        String unidadeCodigo,
        String tipo,
        String status,
        Double limiteAlertaKwh,
        Map<String, Object> atributos) {}
