package br.com.fiap.esg.web;

import br.com.fiap.esg.service.AlertaPolicy;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SistemaController {

    private final String ambiente;

    public SistemaController(@Value("${app.environment:local}") String ambiente) {
        this.ambiente = ambiente;
    }

    /** Mostra em qual ambiente (local/staging/production) a API esta rodando - util para evidencias. */
    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of("app", "esg-energy-platform", "environment", ambiente, "version", "1.0.0");
    }

    @GetMapping("/alertas/avaliar")
    public Map<String, Object> avaliar(@RequestParam double consumoKwh, @RequestParam double limiteKwh) {
        return Map.of("consumoKwh", consumoKwh, "limiteKwh", limiteKwh,
                "severidade", AlertaPolicy.classificar(consumoKwh, limiteKwh));
    }
}
