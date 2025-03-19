package com.led.broker.repository;

import com.led.broker.model.Cor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CorRepository extends MongoRepository<Cor, String> {

    List<Cor> findByRapida(boolean rapida);
}
