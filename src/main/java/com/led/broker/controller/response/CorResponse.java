package com.led.broker.controller.response;

import com.led.broker.model.constantes.Efeito;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.UUID;

@Getter
@Setter
public class CorResponse {

    private UUID id;
    private String nome;
    private Efeito efeito;
    private int[] cor;
    private String primaria;
    private String secundaria;
    private int[] correcao;
    private int velocidade;
    private long time;
    private boolean rapida;
    @Transient
    private boolean responder;

}
