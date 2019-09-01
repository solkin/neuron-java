package com.tomclaw.neuron.core;

import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public class InputNeuron extends Emitter {

    private Double output;

    public InputNeuron(String name) {
        super(name);
    }

    @Override
    public void emit(double input) {
        super.emit(input);
        output = input;
    }

    public Double getOutput() {
        return output;
    }

    @Override
    protected void couch() {
        Map<Receiver, Synapse> receivers = getReceivers();
        for (Receiver receiver : receivers.keySet()) {
            Synapse synapse = receivers.get(receiver);

            double delta = receiver.getDelta();
            double gradient = output * delta;

            double weightDelta = velocity * gradient + moment * synapse.getGradient();

            synapse.addWeight(weightDelta);
            synapse.setGradient(gradient);
        }
    }
}
