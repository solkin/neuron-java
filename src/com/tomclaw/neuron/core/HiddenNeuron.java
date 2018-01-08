package com.tomclaw.neuron.core;

import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public class HiddenNeuron extends ActiveNeuron {

    private Double output;

    public HiddenNeuron(String name) {
        super(name);
    }

    @Override
    void onOutput(double output) {
        this.output = output;
        emit(output);
    }

    @Override
    protected void couch() {
        double derivative = derivative(output);
        double sum = 0.0f;

        Map<Receiver, Synapse> receivers = getReceivers();
        for (Receiver receiver : receivers.keySet()) {
            Synapse synapse = receivers.get(receiver);

            double delta = receiver.getDelta();
            double gradient = output * delta;
            sum += synapse.weight * delta;

            double weightDelta = velocity * gradient + moment * synapse.gradient;

            synapse.weight += weightDelta;
            synapse.gradient = gradient;
        }

        setDelta(derivative * sum);

        notifyCouched();
    }

}
