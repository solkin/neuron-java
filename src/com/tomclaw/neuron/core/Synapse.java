package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
class Synapse {

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
}
