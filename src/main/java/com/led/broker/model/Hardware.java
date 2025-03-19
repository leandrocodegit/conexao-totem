package com.led.broker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hardware {
    private int status;
    private int chain;
    private int power;
    private long tmst;
    private double snr;
    private int rssi;
    private int channel;
    private Gps gps;
}
@Getter
@Setter
class Gps {
    private double lat;
    private double lng;
    private int alt;
}
