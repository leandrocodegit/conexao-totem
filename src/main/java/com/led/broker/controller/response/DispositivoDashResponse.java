package com.led.broker.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispositivoDashResponse {

    private int total;
    private int online;
    private int offline;
}
