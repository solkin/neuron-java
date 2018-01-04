package com.tomclaw.neuron;

/**
 * Created by solkin on 04/01/2018.
 */
public class HiddenNeuron extends ActiveNeuron {

    @Override
    void onOutput(double output) {
        emit(output);
    }

}
