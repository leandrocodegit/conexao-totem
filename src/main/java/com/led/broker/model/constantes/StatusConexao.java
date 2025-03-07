package com.led.broker.model.constantes;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusConexao {


    Offline(0),
    Online(1);

    private int status;

    StatusConexao(int status) {
        this.status = status;
    }

    @JsonCreator
    public static StatusConexao fromDescricao(int status) {
        for (StatusConexao tipo : values()) {
            if (tipo.status == status) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Comando inválido: " + status);
    }
}
