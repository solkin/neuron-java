package com.tomclaw.neuron;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class ActiveNeuron extends Emitter implements Receiver {

    private Map<Emitter, Synapse> inputs = new HashMap<>();
    private Set<Synapse> accepted = new HashSet<>();

    private Double delta;

    public ActiveNeuron(String name) {
        super(name);
    }

    @Override
    public void onAdded(Emitter emitter) {
        inputs.put(emitter, null);
    }

    @Override
    public void accept(Emitter emitter, Synapse synapse) {
        inputs.put(emitter, synapse);
        accepted.add(synapse);

        checkInputs();
    }

    private void checkInputs() {
        double input = 0.0f;
        for (Synapse synapse : inputs.values()) {
            if (synapse == null || !accepted.contains(synapse)) {
                return;
            }
            input += synapse.weight * synapse.value;
        }

        double output = sigmoid(input);

        onOutput(output);

        accepted.clear();
        resetDelta();
    }

    abstract void onOutput(double output);

    void notifyCouched() {
        for (Emitter emitter : inputs.keySet()) {
            emitter.onReceiverCouched();
        }
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public boolean hasDelta() {
        return delta != null;
    }

    public void resetDelta() {
        delta = null;
    }

    static double derivative(double value) {
        return (1.0f - value) * value;
    }

    private static double sigmoid(double value) {
        return 1.0f / (1.0f + Math.pow(Math.E, (-1.0f * value)));
    }

}
