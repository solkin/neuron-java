package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class Neuron {

    public static double velocity = 0.7;
    public static double moment = 0.3;

    private final String name;

    public Neuron(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
