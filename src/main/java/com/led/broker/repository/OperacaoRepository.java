package com.led.broker.repository;

import com.led.broker.model.Operacao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperacaoRepository extends MongoRepository<Operacao, String> {}
