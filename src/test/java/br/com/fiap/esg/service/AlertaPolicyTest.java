package br.com.fiap.esg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AlertaPolicyTest {

    @Test
    void dentroDoLimiteEhNormal() {
        assertEquals("NORMAL", AlertaPolicy.classificar(100, 120));
    }

    @Test
    void limiteInvalidoEhNormal() {
        assertEquals("NORMAL", AlertaPolicy.classificar(100, 0));
    }

    @Test
    void ate10PorCentoAcimaEhMedia() {
        assertEquals("MEDIA", AlertaPolicy.classificar(105, 100));
    }

    @Test
    void entre10e25PorCentoAcimaEhAlta() {
        assertEquals("ALTA", AlertaPolicy.classificar(138.9, 120));
    }

    @Test
    void acimaDe25PorCentoEhCritica() {
        assertEquals("CRITICA", AlertaPolicy.classificar(150, 100));
    }
}
