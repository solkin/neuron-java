package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.InputNeuron;
import com.tomclaw.neuron.core.OutputNeuron;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.*;

public class TrainingDialog extends JDialog {

    private InputNeuron[] inputs;
    private OutputNeuron[] outputs;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JButton addButton;
    private JTextField epochCount;
    private JButton deleteButton;

    private DefaultTableModel model;

    public TrainingDialog(InputNeuron[] inputs, OutputNeuron[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
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

        addButton.addActionListener(e -> {
            String[] row = new String[model.getColumnCount()];
            for (int c = 0; c < row.length; c++) {
                row[c] = "0.0";
            }
            model.addRow(row);
        });
        deleteButton.addActionListener(e -> {
            int[] rows = table1.getSelectedRows();
            if (rows != null && rows.length > 0) {
                for (int c = rows.length - 1; c >= 0; c--) {
                    model.removeRow(rows[c]);
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        Object columnNames[] = new String[inputs.length + outputs.length];
        Object rowData[][] = new Object[0][columnNames.length];
        int i;
        for (i = 0; i < inputs.length; i++) {
            InputNeuron neuron = inputs[i];
            columnNames[i] = neuron.getName();
        }
        for (int c = 0; c < outputs.length; c++) {
            OutputNeuron neuron = outputs[c];
            columnNames[i + c] = neuron.getName();
        }
        table1 = new JTable(rowData, columnNames);
        model = new DefaultTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);
        table1.setShowGrid(true);
        table1.setModel(model);
        table1.setTableHeader(new JTableHeader(table1.getColumnModel()));
    }
}
