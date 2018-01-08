package com.tomclaw.neuron;

import com.tomclaw.neuron.ui.MainForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable ignored) {
            }
            MainForm.main(args);
        });
    }
}
