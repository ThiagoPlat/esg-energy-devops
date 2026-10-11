package br.com.fiap.esg.web;

import br.com.fiap.esg.domain.DispositivoIot;
import br.com.fiap.esg.repository.DispositivoIotRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController extends CrudController<DispositivoIot> {

    public DispositivoController(DispositivoIotRepository repo) {
        super(repo);
    }

    @Override
    protected DispositivoIot comId(DispositivoIot e, String id) {
        return new DispositivoIot(id, e.macAddress(), e.modelo(), e.unidadeCodigo(), e.tipo(),
                e.status(), e.limiteAlertaKwh(), e.atributos());
    }
}
