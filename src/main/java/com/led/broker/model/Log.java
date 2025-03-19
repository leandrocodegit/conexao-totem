package com.led.broker.model;

import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.TipoLog;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "logs")
public class Log {

    @DBRef
    private Cliente cliente;
    private LocalDateTime data;
    private String descricao;
    private Comando comando;
    private String usuario;
    private String mensagem;
    private String id;
    private Cor cor;
    private TipoLog tipoLog;

}
