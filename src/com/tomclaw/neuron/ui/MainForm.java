package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.InputNeuron;
import com.tomclaw.neuron.core.OutputNeuron;

import javax.swing.*;
import java.util.List;

import static java.util.Collections.emptyList;

public class MainForm {
    private JPanel contentPane;
    private JPanel neuronPanel;
    private JTextPane logPane;
    private JButton createButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton runButton;
    private JButton couchButton;

    private InputNeuron[] inputs;
    private int[] hidden;
    private OutputNeuron[] outputs;

    private List<double[]> trainingSet = emptyList();
    private int epochCount = 100;

    public MainForm() {
        createButton.addActionListener(e -> {
            CreateDialog dialog = new CreateDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(contentPane);
            dialog.setVisible(true);

            inputs = dialog.getInputs();
            hidden = dialog.getHiddens();
            outputs = dialog.getOutputs();

            ((JNeuronPanel) neuronPanel).setData(inputs, hidden, outputs);
            neuronPanel.invalidate();
            neuronPanel.repaint();
        });

        runButton.addActionListener(e -> {
            InputDialog dialog = new InputDialog(inputs);
            dialog.pack();
            dialog.setLocationRelativeTo(contentPane);
            dialog.setVisible(true);
        });

        couchButton.addActionListener(e -> {
            TrainingDialog dialog = new TrainingDialog(inputs, outputs, trainingSet, epochCount);
            dialog.pack();
            dialog.setLocationRelativeTo(contentPane);
            dialog.setVisible(true);

            trainingSet = dialog.getTrainingSet();
            epochCount = dialog.getEpochCount();
            for (int epoch = 0; epoch < epochCount; epoch++) {
                for (double[] row : trainingSet) {
                    for (int i = 0; i < inputs.length; i++) {
                        inputs[i].emit(row[i]);
                    }
                }
                for (double[] row : trainingSet) {
                    for (int i = 0; i < outputs.length; i++) {
                        outputs[i].couch(row[i + inputs.length]);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Neural Network");
        frame.setContentPane(new MainForm().contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(720, 540);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        neuronPanel = new JNeuronPanel();
    }
}
