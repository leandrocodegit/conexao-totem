package com.led.broker.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(value = "comando", url = "${comando-url}")
public interface ComandoService {

    @GetMapping("/interno/{id}")
    public void sincronizar(@PathVariable("id") long id);
    @GetMapping("/interno/{id}/{topico}")
    public void sincronizarId(@PathVariable("id") long id, @PathVariable("topico") long topico);
    @GetMapping("/interno/sincronizar/{user}/{responder}")
    public void sincronizarTodos(@PathVariable String  user, @PathVariable boolean responder);
    @GetMapping("/interno/temporizar/{idCor}/{id}")
    public void temporizarInterno(@PathVariable UUID idCor, @PathVariable long id);
}
