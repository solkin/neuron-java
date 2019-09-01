package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
public class Synapse {

    private double value;
    private double weight;
    private double gradient;

    public Synapse(double weight) {
        this.weight = weight;
    }

    public Synapse(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addWeight(double weight) {
        this.weight += weight;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }
}
