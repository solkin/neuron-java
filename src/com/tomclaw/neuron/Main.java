package com.tomclaw.neuron;

public class Main {

    public static void main(String[] args) {
        int sets = 1;

        InputNeuron i1 = new InputNeuron();
        InputNeuron i2 = new InputNeuron();

        HiddenNeuron h1 = new HiddenNeuron();
        HiddenNeuron h2 = new HiddenNeuron();

        OutputNeuron o1 = new OutputNeuron();

        i1.addReceiver(h1, 0.45);
        i1.addReceiver(h2, 0.78);

        i2.addReceiver(h1, -0.12);
        i2.addReceiver(h2, 0.13);

        h1.addReceiver(o1, 1.5);
        h2.addReceiver(o1, -2.3);

        i1.emit(1);
        i2.emit(0);

        Double output = o1.getOutput();
        if (output == null) {
            System.out.println("no output");
        } else {
            double ideal = 1 ^ 0;
            double error = Math.pow(ideal - output, 2) / sets;

            System.out.println("result: " + output + ", error: " + error);
        }
    }
}
