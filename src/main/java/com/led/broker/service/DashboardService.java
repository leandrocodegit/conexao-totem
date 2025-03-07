package com.led.broker.service;


import com.led.broker.config.FeignConfig;
import com.led.broker.controller.response.DashboardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "dashboard", url = "${totem-url}", configuration = FeignConfig.class)
public interface DashboardService {
    @GetMapping("/totem/dashboard/atualizar/conexoes")
    public void atualizarDashboard(@RequestHeader("Authorization") String authorization);

}