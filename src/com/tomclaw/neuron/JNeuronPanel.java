package com.tomclaw.neuron;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class JNeuronPanel extends JPanel {

    private static int GLOBAL_PADDING = 30;
    private static int VERTICAL_PADDING = 40;
    private static int HORIZONTAL_PADDING = 60;
    private static int SHADOW_PADDING = 3;
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

        g.setFont(font);
        g.setColor(new Color(0xececec));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (inputs.length == 0) {
            return;
        }

        int maxCount = Math.max(inputs.length, outputs.length);
        for (int h : hidden) {
            maxCount = Math.max(maxCount, h);
        }
        int maxHeight = maxCount * NEURON_DIAMETER + (maxCount - 1) * VERTICAL_PADDING;
        int baseline = GLOBAL_PADDING + maxHeight / 2;

        Set<Neuron> neurons = new LinkedHashSet<>(Arrays.asList(inputs));

        int y, x = GLOBAL_PADDING;
        Color color = Color.BLACK;
        boolean hasReceivers;
        do {
            hasReceivers = false;

            int neuronsSize = neurons.size() * NEURON_DIAMETER + (neurons.size() - 1) * VERTICAL_PADDING;
            y = baseline - neuronsSize / 2;

            Set<Neuron> current = new LinkedHashSet<>(neurons);
            neurons.clear();
            for (Neuron neuron : current) {
                if (neuron instanceof InputNeuron) {
                    color = new Color(0xF8CAC1);
                } else if (neuron instanceof HiddenNeuron) {
                    color = new Color(0x83D6DE);
                } else if (neuron instanceof OutputNeuron) {
                    color = new Color(0x97CE68);
                }

                String text = neuron.getName();
                int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
                int textHeight = (int) (font.getStringBounds(text, frc).getHeight());
                int ovalPadding = NEURON_DIAMETER / 2;
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(x, y + SHADOW_PADDING, NEURON_DIAMETER, NEURON_DIAMETER);
                g.setColor(color);
                g.fillOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
                g.setColor(color.darker());
                g.drawOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
                g.setColor(color.darker().darker());
                g.drawString(text, x + ovalPadding - textWidth / 2, y + ovalPadding + textHeight / 3);
                y += NEURON_DIAMETER + VERTICAL_PADDING;

                if (neuron instanceof Emitter) {
                    Emitter emitter = (Emitter) neuron;
                    for (Receiver receiver : emitter.receivers.keySet()) {
                        neurons.add((Neuron) receiver);
                    }
                    hasReceivers = !neurons.isEmpty();
                }
            }

            x += NEURON_DIAMETER / 2 + HORIZONTAL_PADDING;
        } while (hasReceivers);
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
