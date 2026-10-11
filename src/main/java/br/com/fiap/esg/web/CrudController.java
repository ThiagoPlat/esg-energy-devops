package br.com.fiap.esg.web;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** CRUD generico reaproveitado pelas 5 colecoes ESG. */
public abstract class CrudController<T> {

    protected final MongoRepository<T, String> repo;

    protected CrudController(MongoRepository<T, String> repo) {
        this.repo = repo;
    }

    /** Devolve uma copia da entidade com o id informado (records sao imutaveis). */
    protected abstract T comId(T entity, String id);

    @GetMapping
    public List<T> listar(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "50") int size) {
        return repo.findAll(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200))).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> buscar(@PathVariable String id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<T> criar(@Valid @RequestBody T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> atualizar(@PathVariable String id, @Valid @RequestBody T body) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repo.save(comId(body, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
