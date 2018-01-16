package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
public class Synapse {

    double value;
    double weight;
    double gradient;

    Synapse(double weight) {
        this.weight = weight;
    }

    Synapse(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }
}
