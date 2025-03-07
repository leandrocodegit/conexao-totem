package com.led.broker.service;


import com.led.broker.model.Dispositivo;
import com.led.broker.model.constantes.Topico;
import com.led.broker.repository.CorRepository;
import com.led.broker.repository.DispositivoRepository;
import com.led.broker.util.ConfiguracaoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.MonoSink;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComandoServiceS {

    private final MqttService mqttService;
    private final DispositivoRepository dispositivoRepository;
    private final AgendaDeviceService agendaDeviceService;
    private final CorRepository corRepository;
    public static Map<String, MonoSink<String>> streams = new HashMap<>();



}
