package br.com.fiap.esg.web;

import br.com.fiap.esg.domain.AcaoEficiencia;
import br.com.fiap.esg.repository.AcaoEficienciaRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acoes")
public class AcaoController extends CrudController<AcaoEficiencia> {

    public AcaoController(AcaoEficienciaRepository repo) {
        super(repo);
    }

    @Override
    protected AcaoEficiencia comId(AcaoEficiencia e, String id) {
        return new AcaoEficiencia(id, e.codigoProjeto(), e.titulo(), e.unidadeAlvo(), e.investimentoBrl(),
                e.economiaEsperadaAnualKwh(), e.status(), e.progressoPct(), e.atributos());
    }
}
