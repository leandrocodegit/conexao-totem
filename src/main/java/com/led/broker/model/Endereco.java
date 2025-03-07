package com.led.broker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Endereco {

    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String numero;

}
