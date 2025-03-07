package com.led.broker.handler;

import com.google.gson.Gson;
import com.led.broker.model.Mensagem;
import static com.led.broker.model.constantes.Comando.*;
import com.led.broker.model.constantes.Topico;
import com.led.broker.service.DispositivoService;
import com.led.broker.util.MensagemFormater;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MqttMessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);

    private final DispositivoService dispositivoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final ConcurrentHashMap<Long, Future<?>> tasks = new ConcurrentHashMap<>();


    @Override
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        UUID clientId = (UUID) message.getHeaders().get("id");
        String topico = (String) message.getHeaders().get("mqtt_receivedTopic");

        try {
            Mensagem payload = MensagemFormater.formatarMensagem(message.getPayload().toString());
            if (Stream.of(ONLINE, CONCLUIDO, CONFIGURACAO, OCORRENCIA, BOTAO_ACIONADO, LORA_PARAMETROS).anyMatch(cmd -> cmd.equals(payload.getComando()))) {
                payload.setBrockerId(clientId.toString());
            var sincronizar = topico.equals(Topico.SINCRONIZAR);
               processarDispositivo(payload.getId(), payload, sincronizar);
            }
        } catch (Exception erro) {
            logger.error("Erro ao capturar id");
        }
        logger.info("Mensagem recebida do cliente " + clientId );
        logger.info("Mensagem: " + message.getPayload().toString());
    }

    private void processarDispositivo(long id, Mensagem payload, boolean apenasSincronizar) {
        Future<?> existingTask = tasks.put(id, executorService.submit(() -> {
            try {
                if(apenasSincronizar)
                    dispositivoService.sincronizar(payload);
                else dispositivoService.atualizarDispositivo(payload);
            } catch (Exception e) {
                logger.error("Erro ao atualizar dispositivo {}", id, e);
            } finally {
                tasks.remove(id);
            }
        }));

        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(true);
        }
    }

}
