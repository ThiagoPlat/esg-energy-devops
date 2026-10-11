package br.com.fiap.esg.repository;

import br.com.fiap.esg.domain.AcaoEficiencia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AcaoEficienciaRepository extends MongoRepository<AcaoEficiencia, String> {}
