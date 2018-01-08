package com.tomclaw.neuron.core;

/**
 * Created by solkin on 04/01/2018.
 */
public interface Receiver {

    void onAdded(Emitter emitter);

    void accept(Emitter emitter, Synapse synapse);

    void setDelta(double delta);

    double getDelta();

    boolean hasDelta();

    void resetDelta();

}
