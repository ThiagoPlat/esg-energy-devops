package br.com.fiap.esg.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Governanca: plantas, predios e setores monitorados. */
@Document("unidades_operacionais")
public record UnidadeOperacional(
        @Id String id,
        @NotBlank String codigo,
        @NotBlank String nome,
        String tipo,
        Double metaReducaoAnualPct,
        /** Campos flexiveis (schema-less): certificacoes, gestor_esg, painel_solar... */
        Map<String, Object> atributos) {}
