package com.led.broker.repository;

import com.led.broker.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, Long> {



}
