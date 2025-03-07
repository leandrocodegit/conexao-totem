package com.led.broker.model.constantes;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoConexao {

    ETH(1),
    WIFI(2),
    LORA(3);


    public int codigo;

    TipoConexao(int codigo) {
        this.codigo = codigo;
    }

    @JsonCreator
    public static TipoConexao fromDescricao(int codigo) {
        for (TipoConexao tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("TipoConexao inválido: " + codigo);
    }
}
