package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.InputNeuron;
import com.tomclaw.neuron.core.OutputNeuron;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class TrainingDialog extends JDialog {

    private InputNeuron[] inputs;
    private OutputNeuron[] outputs;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JButton addButton;
    private JTextField epochCountField;
    private JButton deleteButton;

    private DefaultTableModel model;

    private List<double[]> trainingSet;
    private int epochCount;

    public TrainingDialog(InputNeuron[] inputs, OutputNeuron[] outputs, List<double[]> trainingSet, int epochCount) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.trainingSet = trainingSet;
        this.epochCount = epochCount;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
        try {
            this.epochCount = Integer.parseInt(epochCountField.getText());
            this.trainingSet = new ArrayList<>(model.getRowCount());
            int columnCount = model.getColumnCount();
            for (int r = 0; r < model.getRowCount(); r++) {
                double[] row = new double[columnCount];
                for (int c = 0; c < columnCount; c++) {
                    String value = String.valueOf(model.getValueAt(r, c));
                    row[c] = Double.parseDouble(value);
                }
                trainingSet.add(row);
            }
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

        trainingSet.stream().map(set ->
                Arrays.stream(set)
                        .boxed()
                        .toArray(Double[]::new)
        ).forEach(row -> model.addRow(row));
    }

    public List<double[]> getTrainingSet() {
        return trainingSet;
    }

    public int getEpochCount() {
        return epochCount;
    }
}
