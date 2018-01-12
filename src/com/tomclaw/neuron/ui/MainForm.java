package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.InputNeuron;
import com.tomclaw.neuron.core.OutputNeuron;

import javax.swing.*;

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
            TrainingDialog dialog = new TrainingDialog(inputs, outputs);
            dialog.pack();
            dialog.setLocationRelativeTo(contentPane);
            dialog.setVisible(true);
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
