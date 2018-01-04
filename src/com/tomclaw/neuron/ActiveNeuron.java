package com.tomclaw.neuron;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class ActiveNeuron extends Neuron implements Receiver {

    private Map<Emitter, Synapse> inputs = new HashMap<>();

    @Override
    public void onAdded(Emitter emitter) {
        inputs.put(emitter, null);
    }

    @Override
    public void accept(Emitter emitter, double value, double weight) {
        inputs.put(emitter, new Synapse(value, weight));

        checkInputs();
    }

    private void checkInputs() {
        double input = 0.0f;
        for (Synapse synapse : inputs.values()) {
            if (synapse == null) {
                return;
            }
            input += synapse.weight * synapse.value;
        }

        double output = sigmoid(input);

        onOutput(output);
    }

    abstract void onOutput(double output);

    private static double sigmoid(double value) {
        return 1.0f / (1.0f + Math.pow(Math.E, (-1.0f * value)));
    }

    private static class Synapse {

        double value;
        double weight;

        Synapse(double value, double weight) {
            this.value = value;
            this.weight = weight;
        }
    }
}
