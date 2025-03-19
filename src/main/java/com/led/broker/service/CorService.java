package com.led.broker.service;

import com.led.broker.model.Cor;
import com.led.broker.model.Dispositivo;
import com.led.broker.model.Log;
import com.led.broker.model.Temporizador;
import com.led.broker.model.constantes.Comando;
import com.led.broker.repository.CorRepository;
import com.led.broker.repository.DispositivoRepository;
import com.led.broker.repository.LogRepository;
import com.led.broker.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CorService {

    private final CorRepository corRepository;

    public Optional<Cor> buscaCor(String id){
        return corRepository.findById(id);
    }

}
