package com.led.broker.model.constantes;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCor {

    RGB(0),
    RBG(1),
    BGR(2),
    BRG(3),
    GRB(4),
    GBR(5);

    public int codigo;

    TipoCor(int codigo) {
        this.codigo = codigo;
    }

    @JsonCreator
    public static TipoCor fromDescricao(int codigo) {
        for (TipoCor tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Cor inválido: " + codigo);
    }
}
