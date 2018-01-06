package com.tomclaw.neuron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static DecimalFormat df = new DecimalFormat("#.####");

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

        i1.addReceiver(h1, 0.6);
        i1.addReceiver(h2, 1.1);

        i2.addReceiver(h1, 0.6);
        i2.addReceiver(h2, 1.1);

        h1.addReceiver(o1, -2);
        h2.addReceiver(o1, 1.1);

        boolean hasErrors;
        for (int epoch = 0; epoch < 1000; epoch++) {
            System.out.println("--- epoch " + (epoch + 1) + " ---");
            hasErrors = false;
            for (double[] set : sets) {
                double val1 = set[0];
                double val2 = set[1];
                double ideal = set[2];

                i1.emit(val1);
                i2.emit(val2);

                Double output = o1.getOutput();
                if (output == null) {
                    System.err.println("no output");
                    return;
                } else {
                    long rounded = Math.round(output);
                    double error = Math.pow(ideal - output, 2) / sets.size();

                    System.out.println(val1 + " ^ " + val2 + " = " + rounded + " (" + df.format(output) + "), error: " + df.format(error));

                    if (rounded != ideal) {
                        hasErrors = true;
                    }

                    o1.couch(ideal);
                }
            }
            if (!hasErrors) {
                System.out.println("\n\ncouched by " + (epoch + 1) + " epochs");
                onCouched(i1, i2, o1, sets);
                break;
            }
        }

    }

    private static void onCouched(InputNeuron i1, InputNeuron i2, OutputNeuron o1, List<double[]> sets) {
        for (double[] set : sets) {
            double val1 = set[0];
            double val2 = set[1];
            double ideal = set[2];

            i1.emit(val1);
            i2.emit(val2);

            Double output = o1.getOutput();
            long rounded = Math.round(output);
            double error = Math.pow(ideal - output, 2) / sets.size();

            System.out.println(val1 + " & " + val2 + " = " + rounded + " (" + df.format(output) + "), error: " + df.format(error));
        }
    }
}
