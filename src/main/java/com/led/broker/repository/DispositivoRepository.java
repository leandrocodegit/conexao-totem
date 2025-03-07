package com.led.broker.repository;

import com.led.broker.model.Conexao;
import com.led.broker.model.Dispositivo;
import com.led.broker.model.constantes.Comando;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DispositivoRepository extends MongoRepository<Dispositivo, String> {



    long countByAtivo(boolean ativo);
    Optional<Dispositivo> findByIdAndComando(long id, Comando comando);
    List<Dispositivo> findAllByIdInAndAtivo(List<Long> ids, boolean ativo);
    @Query("{ 'ativo': ?0, 'configuracao': { $ne: null } }")
    List<Dispositivo> findAllByAtivo(boolean ativo);
    @Query("{'id': ?0, 'ativo': ?1 }")
    Optional<Dispositivo> findByIdAndAtivo(long id, boolean ativo);
    @Query("{ 'ativo': ?0, 'ignorarAgenda': ?0, 'configuracao': { $ne: null } }")
    List<Dispositivo> findAllByAtivoIgnorarAgenda(boolean ativo, boolean ignorarAgenda);
    @Query("{ 'ativo': ?0, 'configuracao': { $ne: null } }")
    Page<Dispositivo> findAllByAtivo(boolean ativo, Pageable pageable);
    @Query("{ 'ativo': ?0 }")
    Page<Dispositivo> findAllByInativo(boolean ativo, Pageable pageable);
    @Query("{ 'ativo' : true, 'ultimaAtualizacao' : { $lt: ?0 }, 'configuracao': { $ne: null } }")
    List<Dispositivo> findAllAtivosComUltimaAtualizacaoAntes(Date dataLimite);
    @Query("{ 'ativo' : true, 'ultimaAtualizacao' : { $lt: ?0 }, 'configuracao': { $ne: null } }")
    Page<Dispositivo> findAllAtivosComUltimaAtualizacaoAntes(Date dataLimite, Pageable pageable);


    @Query("{ 'configuracao': null }")
    List<Dispositivo> findDispositivosSemConfiguracao();
    @Query("{ 'configuracao': null }")
    Page<Dispositivo> findDispositivosSemConfiguracao(Pageable pageable);

    @Query("{" +
            "   $or: [" +
            "       { 'id': ?0 }," +
            "       { 'nome': { $regex: ?0, $options: 'i' } }," +
            "       { 'enderecoCompleto': { $regex: ?0, $options: 'i' } }" +
            "   ]," +
//            "   'ativo': true" +
            "}")
    Page<Dispositivo> findByIdAndNomeContaining(String texto, Pageable pageable);

    @Query(value = "{ 'conexao': {'ultimaAtualizacao' : { $lt: ?0 }}, 'conexao': {'status' : 'Online'}, 'ativo' : true }")
    List<Dispositivo> findAllAtivosComUltimaAtualizacaoAntesQueEstavaoOnline(Date dataLimite);

}
