package com.tomclaw.neuron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<double[]> sets = new ArrayList<>();
        sets.add(new double[]{0, 0, 0});
        sets.add(new double[]{0, 1, 1});
        sets.add(new double[]{1, 0, 1});
        sets.add(new double[]{1, 1, 0});

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

        for (int epoch = 0; epoch < 20; epoch++) {
            System.out.println("--- epoch " + (epoch + 1) + " ---");
            for (double[] set : sets) {
                double val1 = set[0];
                double val2 = set[1];
                double ideal = set[2];

                i1.emit(val1);
                i2.emit(val2);

                Double output = o1.getOutput();
                if (output == null) {
                    System.out.println("no output");
                } else {
                    double error = Math.pow(ideal - output, sets.size()) / 1;

                    DecimalFormat df = new DecimalFormat("#.####");
                    System.out.println(val1 + " ^ " + val2 + " = " + df.format(output) + ", error: " + df.format(error));

                    o1.couch(ideal);
                }
            }
        }
    }
}
