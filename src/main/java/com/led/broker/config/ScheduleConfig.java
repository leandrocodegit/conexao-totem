package com.led.broker.config;

import com.led.broker.model.Conexao;
import com.led.broker.model.constantes.StatusConexao;
import com.led.broker.model.constantes.TipoConexao;
import com.led.broker.model.constantes.Topico;
import com.led.broker.repository.LogRepository;
import com.led.broker.service.DashboardService;
import com.led.broker.service.DispositivoService;
import com.led.broker.service.MqttService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.led.broker.model.constantes.Topico.TOPICO_DASHBOARD;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);
    private final DispositivoService dispositivoService;
    private final DashboardService dashboardService;
    private final MqttService mqttService;
    private List<Conexao> conexoes;

    @Scheduled(fixedRate = 1 * 60 * 1000)
    public void checkarDipositivosOffline() {

        conexoes = dispositivoService.dispositivosQueFicaramOffilne();
        if (!conexoes.isEmpty()) {
            logger.warn("Dispositivos offline: " + conexoes.size());
            var conexoesFiltro = conexoes.stream().filter(conexao -> {
                LocalDateTime timeOut = conexao.getUltimaAtualizacao().plusMinutes(conexao.getTempoAtividade());
                if (conexao.getTipoConexao().equals(TipoConexao.LORA))
                    if (conexao.getStatus().equals(StatusConexao.Espera))
                        timeOut = conexao.getUltimaAtualizacao().plusMinutes(30);
                    else timeOut = conexao.getUltimaAtualizacao().plusMinutes(10);
                return LocalDateTime.now().isAfter(timeOut);
            }).toList();
            dispositivoService.salvarDispositivoComoOffline(conexoesFiltro);
            logger.warn("Dispositivos offline filtrados: " + conexoesFiltro.size());
        }
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void atualizacaoDashboard() {
        if (!DispositivoService.clientes.isEmpty()) {
            logger.info("Atualizando dashboard de clientes: " + DispositivoService.clientes.size());
            var clientes = DispositivoService.clientes.values();
            clientes.forEach(cliente -> {
                var clienteId = DispositivoService.clientes.remove(cliente.toString());
                dashboardService.atualizarDashboard(clienteId);
                mqttService.sendRetainedMessage(TOPICO_DASHBOARD + "/" + clienteId, "Atualizando dashboard");
            });
        }
    }
}
