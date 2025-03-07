package com.led.broker.model.constantes;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Efeito {

    SEM_EFEITO(0),
    CILONIO(1),
    GIROFLEX(2),
    CONTADOR(3),
    COLORIDO(4),
    SINALIZADOR(5),
    GIRATORIO(6),
    BATIDA(7),
    FAIXA(8),
    GOTEIRA(9),
    PULSANTE(10),
    AMANHECER(11),
    ONDA(12),
    QUEBRANDO(13),
    CONSTELACAO(14),
    GAME(15),
    FAIXA_3(16),
    UPDATE(10);
    public int codigo;

    Efeito(int codigo) {
        this.codigo = codigo;
    }

    @JsonCreator
    public static Efeito fromDescricao(int codigo) {
        for (Efeito tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        return  SEM_EFEITO;
    }
}
