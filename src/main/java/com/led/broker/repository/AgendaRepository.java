package com.led.broker.repository;

import com.led.broker.model.Agenda;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgendaRepository extends MongoRepository<Agenda, UUID> {

    @Query("{ 'cor._id': ?0 }")
    List<Agenda> findAgendasByCorId(UUID configuracaoId);

    @Query("{ 'dispositivos.id': ?0 }")
    List<Agenda> findAgendasByDispositivoId(long id);

    @Query("{" +
            "   $expr: {" +
            "     $and: [" +
            "       { $lte: [ { $dateToString: { format: '%m-%d', date: '$inicio' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "       { $gte: [ { $dateToString: { format: '%m-%d', date: '$termino' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "       { $or: [" +
            "           { $not: { $ifNull: ['$execucao', false] } }, " +
            "           { $ne: [ { $dateToString: { format: '%m-%d', date: '$execucao' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }" +
            "       ] }" +
            "     ]" +
            "   }," +
            "   'ativo': true" +
            "}")
    List<Agenda> findAgendasByDataDentroDoIntervalo(LocalDate data);

    @Query("{" +
            "   $expr: {" +
            "     $and: [" +
            "       { $lte: [ { $dateToString: { format: '%m-%d', date: '$inicio' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "       { $gte: [ { $dateToString: { format: '%m-%d', date: '$termino' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "     ]" +
            "   }," +
            "   'ativo': true" +
            "   'todos': ?1" +
            "}")
    List<Agenda> findAgendasByDataDentroDoIntervalo(LocalDate data, boolean todos);

    @Query("{" +
            "   $expr: {" +
            "     $and: [" +
            "       { $lte: [ { $dateToString: { format: '%m-%d', date: '$inicio' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "       { $gte: [ { $dateToString: { format: '%m-%d', date: '$termino' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "     ]" +
            "   }," +
            "   'ativo': true" +
            "}")
    List<Agenda> findAllAgendasByDataDentroDoIntervalo(LocalDate data);
    @Query("{" +
            "   $expr: {" +
            "     $and: [" +
            "       { $lte: [ { $dateToString: { format: '%m-%d', date: '$inicio' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "       { $gte: [ { $dateToString: { format: '%m-%d', date: '$termino' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }," +
            "     ]" +
            "   }," +
            "   'ativo': true" +
            "   'todos': ?1" +
            "}")
    List<Agenda> findAllAgendasByDataDentroDoIntervalo(LocalDate data, boolean todos);

    @Query("{" +
            " $expr: {" +
            "   $and: [" +
            "       { $eq: [ { $month: '$inicio' }, ?0 ] }," +
            "   ]" +
            " }," +
            " 'ativo': ?1" +
            "}")
    List<Agenda> findAllDoMesAtualInOrderByInicioDesc(int mes, boolean ativo, Sort sort);
    @Aggregation(pipeline = {
                    "     {" +
                    "       $project:" +
                    "         {" +
                    "           month: { $month: '$inicio' }" +
                    "         }" +
                    "     }," +
            "{ $match: { month: 11, ativo: ?0 }} "
    })
    List<Integer> findAllDoMes(boolean ativo);

    List<Agenda> findAllByAtivo(boolean ativo);

    @Query("{" +
            " $expr: {" +
            "   $and: [" +
            "       { $lte: [ { $dateToString: { format: '%m-%d', date: '$inicio' } }, { $dateToString: { format: '%m-%d', date: ?1 } } ] }," +
            "       { $gte: [ { $dateToString: { format: '%m-%d', date: '$termino' } }, { $dateToString: { format: '%m-%d', date: ?0 } } ] }" +
            "   ]" +
            " }," +
            " 'dispositivos.id': ?2," +
            " 'ativo': true" +
            " '_id': { $ne: ?3 }" +
            "}")
    List<Agenda> findFirstByDataAndDispositivo(LocalDate inicio, LocalDate termino, long dispositivos, UUID agendaId);


}


