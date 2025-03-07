package com.led.broker.repository;

import com.led.broker.model.Conexao;
import com.led.broker.model.Dispositivo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ConexaoRepository extends MongoRepository<Conexao, String> {

    @Query("{ 'ultimaAtualizacao' : { $lt: ?0 }, 'conexao.status' : 'Online', 'ativo' : true }")
    List<Conexao> findAllAtivosComUltimaAtualizacaoAntesQueEstavaoOnline(LocalDateTime dataLimite);

}
