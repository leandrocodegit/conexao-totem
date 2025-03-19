package com.led.broker.model;

import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.Efeito;
import com.led.broker.model.constantes.StatusConexao;
import com.led.broker.model.constantes.TipoConexao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ParamsLora {

    private String payload;
    private String encrypted_payload;
    private Hardware hardware;
    private String code;
    private List<Localizacao> solutions;
}



