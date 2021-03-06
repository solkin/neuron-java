package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
public class OutputNeuron extends ActiveNeuron {

    private Double output;

    public OutputNeuron(String name) {
        super(name);
    }

    @Override
    void onOutput(double output) {
        this.output = output;
    }

    public Double getOutput() {
        return output;
    }

    public void couch(double ideal) {
        setDelta((ideal - output) * derivative(output));

        notifyCouched();
    }

    @Override
    protected void couch() {
        // just empty, nobody can invoke output neuron to couch
        // except of manual couch(ideal) invocation
    }
}
