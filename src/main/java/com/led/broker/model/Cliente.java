package com.led.broker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clientes")
public class Cliente {

    @Id
    private UUID id;
    private String nome;
    private Boolean ativo = Boolean.FALSE;
    private Endereco endereco;
    private boolean principal;
    private String enderecoCompleto;
    @DBRef
    private List<Cliente> clientes;

}
