package com.led.broker.model;

import com.led.broker.model.constantes.Comando;
import com.led.broker.model.constantes.StatusConexao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "dispositivos")
public class Dispositivo {

    @Id
    private long id;
    @DBRef
    private Cliente cliente;
    private Long topico;
    private String nome;
    private String ip;
    private Integer memoria;
    private String versao;
    private boolean ignorarAgenda;
    private boolean permiteComando;
    private boolean ativo;
    private Comando comando;
    private String latitude;
    private String longitude;
    private String brokerId;
    private Endereco endereco;
    private String enderecoCompleto;
    private Float sensibilidadeVibracao;
    private String corVibracao;
    private Integer tempoAtividade;
    @DBRef
    private Operacao operacao;
    @DBRef
    private Conexao conexao;
    @DBRef
    private Cor cor;
    @DBRef
    private Agenda agenda;


}
