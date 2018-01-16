package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.*;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.util.Collections.emptyList;

public class MainForm {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss.SSS");

    private JPanel contentPane;
    private JPanel neuronPanel;
    private JTextPane logPane;
    private JButton createButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton runButton;
    private JButton couchButton;
    private JButton randomButton;

    private InputNeuron[] inputs;
    private int[] hidden;
    private OutputNeuron[] outputs;

    private List<double[]> trainingSet = emptyList();
    private int epochCount = 100;

    public MainForm() {
        log("Log started");

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
                    for (int i = 0; i < row.length; i++) {
                        if (i < inputs.length) {
                            inputs[i].emit(row[i]);
                        } else {
                            outputs[i - inputs.length].couch(row[i]);
                        }
                    }
                }
            }
        });

        randomButton.addActionListener(e -> {
            Random random = new Random(System.currentTimeMillis());
            List<Neuron> neurons = ((JNeuronPanel) neuronPanel).getNeurons();
            for (Neuron neuron : neurons) {
                if (neuron instanceof Emitter) {
                    Emitter emitter = (Emitter) neuron;
                    for (Receiver receiver : emitter.getReceivers().keySet()) {
                        Synapse synapse = emitter.getReceivers().get(receiver);
                        double weight = random.nextDouble() * 2;
                        synapse.setWeight(weight);
                        log("Weight " + neuron.getName() + " → " + ((Neuron) receiver).getName() + ": " + weight);
                    }
                }
            }
        });
    }

    private void log(String message) {
        logPane.setText(logPane.getText() + dateFormat.format(new Date()) + " " + message + "\n");
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
