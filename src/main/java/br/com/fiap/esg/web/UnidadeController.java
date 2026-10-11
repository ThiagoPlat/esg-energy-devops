package br.com.fiap.esg.web;

import br.com.fiap.esg.domain.UnidadeOperacional;
import br.com.fiap.esg.repository.UnidadeOperacionalRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeController extends CrudController<UnidadeOperacional> {

    public UnidadeController(UnidadeOperacionalRepository repo) {
        super(repo);
    }

    @Override
    protected UnidadeOperacional comId(UnidadeOperacional e, String id) {
        return new UnidadeOperacional(id, e.codigo(), e.nome(), e.tipo(), e.metaReducaoAnualPct(), e.atributos());
    }
}
