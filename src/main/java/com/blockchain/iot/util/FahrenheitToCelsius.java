package com.blockchain.iot.util;

public class FahrenheitToCelsius {

    public static double toFahrenheit(double celsius) {
        double fahrenheit = 9 * (celsius / 5) + 32;
        return fahrenheit;
    }

    public static double toCelsius(double fahrenheit) {
        double celsius = (fahrenheit - 32) * 5 / 9;
        return celsius;
    }

}
