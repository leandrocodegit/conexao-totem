package com.led.broker.controller.request;

import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.Efeito;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class ComandoRequest {

    private Efeito efeito;
    private Comando comando;
    private int[] cor;
    private int leds;
    private int faixa;
    private int intensidade;
    private int[] correcao;
    private int velocidade;
    private boolean responder;
}
