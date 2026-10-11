package br.com.fiap.esg.web;

import br.com.fiap.esg.domain.LeituraConsumo;
import br.com.fiap.esg.domain.ResumoConsumo;
import br.com.fiap.esg.repository.LeituraConsumoRepository;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leituras")
public class LeituraController extends CrudController<LeituraConsumo> {

    private final MongoTemplate template;

    public LeituraController(LeituraConsumoRepository repo, MongoTemplate template) {
        super(repo);
        this.template = template;
    }

    @Override
    protected LeituraConsumo comId(LeituraConsumo e, String id) {
        return new LeituraConsumo(id, e.macDispositivo(), e.timestamp(), e.consumoKwh(),
                e.emissaoEstimadaCo2Kg(), e.atributos());
    }

    /** Aggregation Framework: media de consumo e CO2 por medidor (ignora geracao solar, kWh < 0). */
    @GetMapping("/resumo")
    public List<ResumoConsumo> resumo() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("consumo_kwh").gt(0)),
                Aggregation.group("mac_dispositivo")
                        .avg("consumo_kwh").as("consumoMedioKwh")
                        .sum("emissao_estimada_co2_kg").as("totalCo2Kg")
                        .count().as("totalLeituras"));
        return template.aggregate(agg, "leituras_consumo", ResumoConsumo.class).getMappedResults();
    }
}
