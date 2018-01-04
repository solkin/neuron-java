package com.tomclaw.neuron;

/**
 * Created by solkin on 04/01/2018.
 */
public class HiddenNeuron extends ActiveNeuron {

    private Double output;

    @Override
    void onOutput(double output) {
        this.output = output;
        emit(output);
    }

    @Override
    public void couch(double ideal) {
//        double derivative = derivative(output);
//        double sum = 0.0f;
//        for (Receiver receiver : receivers.keySet()) {
//            double weight = receivers.get(receiver);
//
//            double delta = emitter.getDelta();
//            double gradient = synapse.weight * delta;
//            sum += gradient;
//
//            double weightDelta = velocity * gradient + moment * synapse.gradient;
//
//            synapse.weight += weightDelta;
//            synapse.gradient = gradient;
//        }
//        setDelta(derivative * sum);
    }

}
