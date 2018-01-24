package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.InputNeuron;

import javax.swing.*;
import java.awt.event.*;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class InputDialog extends JDialog {

    private InputNeuron[] neurons;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel formPanel;
    private JTable table1;

    public InputDialog(InputNeuron[] neurons) {
        this.neurons = neurons;
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
            IntStream.range(0, table1.getRowCount()).forEach(i -> {
                String value = String.valueOf(table1.getValueAt(i, 1));
                if (value != null) {
                    neurons[i].emit(Double.parseDouble(value));
                }
            });
        } catch (Throwable ex) {
            String message = String.format("Invalid input:\n%s", ex.getMessage());
            JOptionPane.showMessageDialog(this, message, "Error", ERROR_MESSAGE);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        Object columnNames[] = {"Name", "Value"};
        Object rowData[][] = new Object[neurons.length][2];
        table1 = new JTable(rowData, columnNames);
        table1.setShowGrid(false);
        IntStream.range(0, rowData.length).forEach(i -> {
            InputNeuron neuron = neurons[i];
            String value = neuron.getOutput() == null ? "0.0" : String.valueOf(neuron.getOutput());
            rowData[i] = new Object[]{neuron.getName(), value};
        });
    }
}
