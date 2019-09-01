package com.tomclaw.neuron.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class Emitter extends Neuron {

    private Map<Receiver, Synapse> receivers = new HashMap<>();

    public Emitter(String name) {
        super(name);
    }

    public void addReceiver(Receiver receiver, double weight) {
        receivers.put(receiver, new Synapse(weight));
        receiver.onAdded(this);
    }

    public void emit(double input) {
        for (Receiver receiver : receivers.keySet()) {
            Synapse synapse = receivers.get(receiver);
            synapse.setValue(input);
            receiver.accept(this, synapse);
        }
    }

    public Map<Receiver, Synapse> getReceivers() {
        return receivers;
    }

    public final void onReceiverCouched() {
        for (Receiver receiver : receivers.keySet()) {
            if (!receiver.hasDelta()) {
                return;
            }
        }

        couch();
    }

    protected abstract void couch();

}
