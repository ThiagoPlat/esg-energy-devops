package br.com.fiap.esg.repository;

import br.com.fiap.esg.domain.DispositivoIot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DispositivoIotRepository extends MongoRepository<DispositivoIot, String> {}
