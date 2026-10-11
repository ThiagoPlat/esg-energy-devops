package br.com.fiap.esg.domain;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Projetos de eficiencia energetica e impacto social. */
@Document("acoes_eficiencia")
public record AcaoEficiencia(
        @Id String id,
        @NotBlank String codigoProjeto,
        @NotBlank String titulo,
        String unidadeAlvo,
        Double investimentoBrl,
        Double economiaEsperadaAnualKwh,
        String status,
        Integer progressoPct,
        Map<String, Object> atributos) {}
