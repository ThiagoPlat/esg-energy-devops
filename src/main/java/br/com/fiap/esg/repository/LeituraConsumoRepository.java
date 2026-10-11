package br.com.fiap.esg.repository;

import br.com.fiap.esg.domain.LeituraConsumo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeituraConsumoRepository extends MongoRepository<LeituraConsumo, String> {}
