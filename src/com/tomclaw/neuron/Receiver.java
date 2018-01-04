package com.tomclaw.neuron;

/**
 * Created by solkin on 04/01/2018.
 */
public interface Receiver {

    void onAdded(Emitter emitter);

    void accept(Emitter emitter, double value, double weight);

}
