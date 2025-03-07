package com.led.broker.service;

import com.led.broker.model.Agenda;
import com.led.broker.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendaDeviceService {

    private final AgendaRepository agendaRepository;

    public List<Agenda> listaTodosAgendasPrevistaHoje() {
        LocalDate data = LocalDateTime.now().toLocalDate();
        return agendaRepository.findAgendasByDataDentroDoIntervalo(data, true);
    }
    public Agenda buscarAgendaDipositivoPrevistaHoje(long id) {
        List<Agenda> agendaList = agendaRepository.findFirstByDataAndDispositivo(LocalDateTime.now().minusHours(3).toLocalDate(), LocalDateTime.now().minusHours(3).toLocalDate(), id, UUID.randomUUID());
        if(!agendaList.isEmpty()){
            System.out.println("Com agenda");
            return agendaList.get(0);
        }
        System.out.println("Sem agenda");
        return null;
    }

    public boolean possuiAgendaDipositivoPrevistaHoje(Agenda agenda, long id) {
        return !agendaRepository.findFirstByDataAndDispositivo(agenda.getInicio(), agenda.getTermino(), id, agenda.getId()).isEmpty();
    }

    public void atualizarDataExecucao(Agenda agenda) {
        agenda.setExecucao(LocalDateTime.now().toLocalDate());
        agendaRepository.save(agenda);
    }
}
