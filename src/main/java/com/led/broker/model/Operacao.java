package com.led.broker.model;

import com.led.broker.model.constantes.ModoOperacao;
import com.led.broker.model.constantes.StatusConexao;
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
@Document(collection = "operacoes")
public class Operacao {

    @Id
    private String id;
    private ModoOperacao modoOperacao;
    @DBRef
    private Agenda agenda;
    @DBRef
    private Cor corTemporizador;
    @DBRef
    private Cor corVibracao;
    private LocalDateTime time;
}
