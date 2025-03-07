package com.led.broker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "agendas")
public class Agenda {

    @Id
    private UUID id;
    @DBRef
    private Cliente cliente;
    private String nome;
    private boolean ativo;
    private String status;
    private LocalDate execucao;
    private LocalDate inicio;
    private LocalDate termino;
    @DBRef
    private Cor cor;
    @DBRef
    private List<Long> dispositivos;
    private boolean todos;
}
