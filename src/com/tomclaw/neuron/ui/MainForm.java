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

    public MainForm() {
        createButton.addActionListener(e -> {
            CreateDialog dialog = new CreateDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(contentPane);
            dialog.setVisible(true);

            InputNeuron[] inputs = dialog.getInputs();
            int[] hidden = dialog.getHiddens();
            OutputNeuron[] outputs = dialog.getOutputs();

            ((JNeuronPanel) neuronPanel).setData(inputs, hidden, outputs);
            neuronPanel.invalidate();
            neuronPanel.repaint();
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
