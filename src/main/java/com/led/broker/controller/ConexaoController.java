package com.led.broker.controller;

import com.led.broker.config.ScheduleConfig;
import com.led.broker.model.constantes.Topico;
import com.led.broker.service.DashboardService;
import com.led.broker.service.MqttService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("conexao")
@RequiredArgsConstructor
public class ConexaoController {

    private static final Logger logger = LoggerFactory.getLogger(ConexaoController.class);
    private final DashboardService dashboardService;
    private final MqttService mqttService;

    @GetMapping
    //@CrossOrigin({"http://totem:8081"})
    public ResponseEntity<String> atualizarDashboar() {
        logger.warn("Atualizando dashboard");
        dashboardService.atualizarDashboard("");
        mqttService.sendRetainedMessage(Topico.TOPICO_DASHBOARD, "Atualizando dashboard");
        logger.warn("Atualizado com sucesso");
        return ResponseEntity.ok("atualizado com sucesso");
    }
}
