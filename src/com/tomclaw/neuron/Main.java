package com.tomclaw.neuron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static DecimalFormat df = new DecimalFormat("#.####");

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();

        List<double[]> sets = new ArrayList<>();
        sets.add(new double[]{0, 0, 0});
        sets.add(new double[]{0, 1, 1});
        sets.add(new double[]{1, 0, 1});
        sets.add(new double[]{1, 1, 0});

        InputNeuron[] inputs = new InputNeuron[2];
        int[] hidden = new int[]{3, 1};
        OutputNeuron[] outputs = new OutputNeuron[1];

        createNeuralNetwork(inputs, hidden, outputs);

        boolean hasErrors;
        for (int epoch = 0; epoch < 1000; epoch++) {
            System.out.println("--- epoch " + (epoch + 1) + " ---");
            hasErrors = false;
            for (double[] set : sets) {
                double val1 = set[0];
                double val2 = set[1];
                double ideal = set[2];

                inputs[0].emit(val1);
                inputs[1].emit(val2);

                Double output = outputs[0].getOutput();
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

                    outputs[0].couch(ideal);
                }
            }
            if (!hasErrors) {
                System.out.println("\n\ncouched by " + (epoch + 1) + " epochs");
                onCouched(inputs[0], inputs[1], outputs[0], sets);
                break;
            }
        }

    }

    private static void createNeuralNetwork(InputNeuron[] inputs, int[] hidden, OutputNeuron[] outputs) {
        int inputCounter = 0;
        int outputCounter = 0;
        int hiddenCounter = 0;
        for (int c = 0; c < inputs.length; c++) {
            inputs[c] = new InputNeuron("I" + (++inputCounter));
        }
        for (int c = 0; c < outputs.length; c++) {
            outputs[c] = new OutputNeuron("O" + (++outputCounter));
        }
        List<List<HiddenNeuron>> hiddens = new ArrayList<>();
        for (int h : hidden) {
            List<HiddenNeuron> list = new ArrayList<>();
            for (int c = 0; c < h; c++) {
                list.add(new HiddenNeuron("H" + (++hiddenCounter)));
            }
            hiddens.add(list);
        }
        for (InputNeuron input : inputs) {
            for (HiddenNeuron firstLine : hiddens.get(0)) {
                input.addReceiver(firstLine, 0.5);
            }
        }
        for (int c = 0; c < hiddens.size() - 1; c++) {
            List<HiddenNeuron> actual = hiddens.get(c);
            List<HiddenNeuron> next = hiddens.get(c + 1);
            for (HiddenNeuron hiddenNeuron : actual) {
                for (HiddenNeuron n : next) {
                    hiddenNeuron.addReceiver(n, 0.5);
                }
            }
        }
        List<HiddenNeuron> last = hiddens.get(hiddens.size() - 1);
        for (HiddenNeuron l : last) {
            for (OutputNeuron o : outputs) {
                l.addReceiver(o, 0.5);
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

            System.out.println(val1 + " ~ " + val2 + " = " + rounded + " (" + df.format(output) + "), error: " + df.format(error));
        }
    }
}
