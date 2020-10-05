package com.blockchain.iot.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sensor {

    private String timeStamp;
    private double temperatureCelsius;
    private double temperatureFahrenheit;
    private double humidity;
    //private String blockNumber;
    private String hash;
}
