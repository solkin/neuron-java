package com.tomclaw.neuron;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class Emitter {

    private double delta;

    Map<Receiver, Synapse> receivers = new HashMap<>();

    public void addReceiver(Receiver receiver, double weight) {
        receivers.put(receiver, new Synapse(weight));
        receiver.onAdded(this);
    }

    public void emit(double input) {
        for (Receiver receiver : receivers.keySet()) {
            Synapse synapse = receivers.get(receiver);
            synapse.value = input;
            receiver.accept(this, synapse);
        }
    }

    abstract void couch(double ideal);

    public double getDelta() {
        return delta;
    }

    void setDelta(double delta) {
        this.delta = delta;
    }

}
