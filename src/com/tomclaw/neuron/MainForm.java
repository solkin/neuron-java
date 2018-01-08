package com.tomclaw.neuron;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    private JPanel contentPane;
    private JPanel neuronPanel;
    private JTextPane logPane;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Neural Network");
        frame.setContentPane(new MainForm().contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(480, 380);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        JNeuronPanel panel = new JNeuronPanel();
        neuronPanel = panel;

        InputNeuron[] inputs = new InputNeuron[2];
        int[] hidden = new int[]{3, 2, 1};
        OutputNeuron[] outputs = new OutputNeuron[1];

        createNeuralNetwork(inputs, hidden, outputs);
        panel.setInputs(inputs);
        panel.setHidden(hidden);
        panel.setOutputs(outputs);
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
}
