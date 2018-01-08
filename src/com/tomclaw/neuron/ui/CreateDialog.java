package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.HiddenNeuron;
import com.tomclaw.neuron.InputNeuron;
import com.tomclaw.neuron.OutputNeuron;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class CreateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinner1;
    private JTextField hiddensField;
    private JTextField inputsField;
    private JTextField outputsField;
    private JSpinner spinner2;

    private InputNeuron[] inputs;
    private int[] hiddens;
    private OutputNeuron[] outputs;

    public CreateDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            inputs = new InputNeuron[Integer.parseInt(inputsField.getText())];
            String[] values = hiddensField.getText().split(",");
            hiddens = new int[values.length];
            for (int c = 0; c < values.length; c++) {
                hiddens[c] = Integer.parseInt(values[c]);
            }
            outputs = new OutputNeuron[Integer.parseInt(outputsField.getText())];
            createNeuralNetwork(inputs, hiddens, outputs);
        } catch (Throwable ex) {
            String message = String.format("Invalid input:\n%s", ex.getMessage());
            JOptionPane.showMessageDialog(this, message, "Error", ERROR_MESSAGE);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public InputNeuron[] getInputs() {
        return inputs;
    }

    public int[] getHiddens() {
        return hiddens;
    }

    public OutputNeuron[] getOutputs() {
        return outputs;
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
