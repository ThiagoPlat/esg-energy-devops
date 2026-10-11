package br.com.fiap.esg.repository;

import br.com.fiap.esg.domain.IncidenteAlerta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncidenteAlertaRepository extends MongoRepository<IncidenteAlerta, String> {}
