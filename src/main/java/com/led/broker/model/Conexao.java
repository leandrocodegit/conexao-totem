package com.led.broker.model;

import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.StatusConexao;
import com.led.broker.model.constantes.TipoConexao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "conexoes")
public class Conexao {

    @Id
    private String id;
    private LocalDateTime ultimaAtualizacao;
    private StatusConexao status;
    private StatusConexao statusMCU;
    private TipoConexao tipoConexao;
    private Boolean habilitarWifi;
    private String ssid;
    private String senha;
    private Boolean habilitarLoraWan;
    private Integer modoLora;
    private String classe;
    private String devEui;
    private String appEui;
    private String appKey;
    private String nwkSKey;
    private String appSKey;
    private String devAddr;
    private String loraSecret;
    private Integer txPower;
    private Integer dataRate;
    private Boolean adr = Boolean.FALSE;
    private Double snr;
    private Integer rssi;
    private Boolean autoJoin;
    private Integer tempoAtividade;
    private Boolean fracionarMensagem = Boolean.FALSE;
    private String latitude;
    private String longitude;


    public Integer getTempoAtividade() {
        if(tempoAtividade == null)
            return 2;
        return tempoAtividade;
    }
}
