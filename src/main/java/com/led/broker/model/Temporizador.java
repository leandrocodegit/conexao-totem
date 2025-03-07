package com.led.broker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Temporizador {

    private UUID idCor;
    private LocalDateTime time;
}
