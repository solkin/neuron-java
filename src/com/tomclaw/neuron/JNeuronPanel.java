package com.tomclaw.neuron;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class JNeuronPanel extends JPanel {

    private static int GLOBAL_PADDING = 30;
    private static int VERTICAL_PADDING = 40;
    private static int HORIZONTAL_PADDING = 60;
    private static int NEURON_DIAMETER = 40;

    private InputNeuron[] inputs = new InputNeuron[0];
    private int[] hidden = new int[]{0};
    private OutputNeuron[] outputs = new OutputNeuron[0];

    private FontRenderContext frc;
    private Font font;

    public JNeuronPanel() {
        AffineTransform affinetransform = new AffineTransform();
        frc = new FontRenderContext(affinetransform, true, true);
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font f : fonts) {
            if (f.getFamily().equals("Monospaced")) {
                font = new Font(f.getName(), Font.BOLD, 12);
                break;
            }
        }
    }

    public void setInputs(InputNeuron[] inputs) {
        this.inputs = inputs;
    }

    public void setHidden(int[] hidden) {
        this.hidden = hidden;
    }

    public void setOutputs(OutputNeuron[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(0xececec));
        g.fillRect(0, 0, getWidth(), getHeight());

        int x = GLOBAL_PADDING;
        int y = GLOBAL_PADDING;

        g.setColor(Color.RED);
        g.setFont(font);

        int maxCount = Math.max(inputs.length, outputs.length);
        for (int h : hidden) {
            maxCount = Math.max(maxCount, h);
        }
        int maxHeight = maxCount * NEURON_DIAMETER + (maxCount - 1) * VERTICAL_PADDING;
        int baseline = GLOBAL_PADDING + maxHeight / 2;

        int inputsSize = inputs.length * NEURON_DIAMETER + (inputs.length - 1) * VERTICAL_PADDING;
        y = baseline - inputsSize / 2;
        Set<Receiver> receivers = new LinkedHashSet<>();
        for (InputNeuron neuron : inputs) {
            int ovalPadding = NEURON_DIAMETER / 2;
            g.drawOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
            String text = neuron.getName();
            int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
            int textHeight = (int) (font.getStringBounds(text, frc).getHeight());
            g.drawString(text, x + ovalPadding - textWidth / 2, y + ovalPadding + textHeight / 3);
            y += NEURON_DIAMETER + VERTICAL_PADDING;
            receivers.addAll(neuron.receivers.keySet());
        }

        boolean hasReceivers = !receivers.isEmpty();
        while (hasReceivers) {
            x += NEURON_DIAMETER / 2 + HORIZONTAL_PADDING;

            int receiversSize = receivers.size() * NEURON_DIAMETER + (receivers.size() - 1) * VERTICAL_PADDING;

            y = baseline - receiversSize / 2;

            Set<Receiver> set = new LinkedHashSet<>(receivers);
            receivers.clear();
            hasReceivers = false;
            for (Receiver receiver : set) {
                Neuron neuron = (Neuron) receiver;
                if (neuron instanceof HiddenNeuron) {
                    g.setColor(Color.BLUE);
                } else if (neuron instanceof OutputNeuron) {
                    g.setColor(Color.GREEN);
                }
                int ovalPadding = NEURON_DIAMETER / 2;
                g.drawOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
                String text = neuron.getName();
                int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
                int textHeight = (int) (font.getStringBounds(text, frc).getHeight());
                g.drawString(text, x + ovalPadding - textWidth / 2, y + ovalPadding + textHeight / 3);
                y += NEURON_DIAMETER + VERTICAL_PADDING;
                if (neuron instanceof Emitter) {
                    Emitter emitter = (Emitter) neuron;
                    receivers.addAll(emitter.receivers.keySet());
                    hasReceivers = true;
                }
            }
        }
    }

    private static class NeuronItem {

        static final int TYPE_INPUT = 0x01;
        static final int TYPE_HIDDEN = 0x02;
        static final int TYPE_OUTPUT = 0x03;

        int x, y;
        Color color;
        int type;
        List<NeuronItem> receivers;

        public NeuronItem(int x, int y, Color color, int type, List<NeuronItem> receivers) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.type = type;
            this.receivers = receivers;
        }
    }
}
