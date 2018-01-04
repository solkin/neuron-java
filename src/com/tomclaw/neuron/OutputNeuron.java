package com.tomclaw.neuron;

/**
 * Created by solkin on 04/01/2018.
 */
public class OutputNeuron extends ActiveNeuron {

    private Double output;

    @Override
    void onOutput(double output) {
        this.output = output;
    }

    public Double getOutput() {
        return output;
    }
}
