package br.com.fiap.esg.web;

import br.com.fiap.esg.domain.IncidenteAlerta;
import br.com.fiap.esg.repository.IncidenteAlertaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteController extends CrudController<IncidenteAlerta> {

    public IncidenteController(IncidenteAlertaRepository repo) {
        super(repo);
    }

    @Override
    protected IncidenteAlerta comId(IncidenteAlerta e, String id) {
        return new IncidenteAlerta(id, e.protocolo(), e.macDispositivo(), e.nivelSeveridade(),
                e.descricao(), e.status(), e.dataOcorrencia(), e.atributos());
    }
}
