package br.com.fiap.esg.repository;

import br.com.fiap.esg.domain.UnidadeOperacional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnidadeOperacionalRepository extends MongoRepository<UnidadeOperacional, String> {}
