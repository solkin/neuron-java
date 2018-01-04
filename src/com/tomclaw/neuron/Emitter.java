package com.tomclaw.neuron;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by solkin on 04/01/2018.
 */
public abstract class Emitter {

    private Map<Receiver, Double> receivers = new HashMap<>();

    public void addReceiver(Receiver receiver, double weight) {
        receivers.put(receiver, weight);
        receiver.onAdded(this);
    }

    public void emit(double input) {
        for (Receiver receiver : receivers.keySet()) {
            double weight = receivers.get(receiver);
            receiver.accept(this, input, weight);
        }
    }

}
