package com.tomclaw.neuron;

/**
 * Created by solkin on 04/01/2018.
 */
public class InputNeuron extends Emitter {

    private Double output;

    @Override
    public void emit(double input) {
        super.emit(input);
        output = input;
    }

    @Override
    protected void couch() {
        for (Receiver receiver : receivers.keySet()) {
            Synapse synapse = receivers.get(receiver);

            double delta = receiver.getDelta();
            double gradient = output * delta;

            double weightDelta = velocity * gradient + moment * synapse.gradient;

            synapse.weight += weightDelta;
            synapse.gradient = gradient;

            System.out.println("input synapse weight: " + synapse.weight);
        }
    }
}
