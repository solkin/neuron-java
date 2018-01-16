package com.tomclaw.neuron.ui;

import com.tomclaw.neuron.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;
import static java.util.Collections.emptyList;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class JNeuronPanel extends JPanel {

    private static int GLOBAL_PADDING = 30;
    private static int VERTICAL_PADDING = 40;
    private static int HORIZONTAL_PADDING = 60;
    private static int SHADOW_PADDING = 3;
    private static int NEURON_DIAMETER = 40;

    private List<MapItem> items = emptyList();

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
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                items.forEach(item -> {
                    int x1 = item.getX();
                    int y1 = item.getY();
                    int x2 = x1 + NEURON_DIAMETER;
                    int y2 = y1 + NEURON_DIAMETER;
                    if (x1 <= e.getX() && y1 <= e.getY() && x2 >= e.getX() && y2 >= e.getY()) {
                        switch (e.getButton()) {
                            case BUTTON1:
                                if (item.neuron instanceof InputNeuron) {
                                    InputNeuron neuron = (InputNeuron) item.neuron;
                                    String result = JOptionPane.showInputDialog(JNeuronPanel.this,
                                            String.format("Enter value for %s", item.getText()), neuron.getOutput());
                                    if (result != null) {
                                        try {
                                            neuron.emit(Double.parseDouble(result));
                                        } catch (Throwable ignored) {
                                            JOptionPane.showMessageDialog(JNeuronPanel.this,
                                                    "Invalid double format", "Error", ERROR_MESSAGE);
                                        }
                                    }
                                } else if (item.neuron instanceof OutputNeuron) {
                                    OutputNeuron neuron = (OutputNeuron) item.neuron;
                                    JOptionPane.showMessageDialog(JNeuronPanel.this,
                                            String.format("Value of %s is: %f", item.getText(), neuron.getOutput()),
                                            "Value", INFORMATION_MESSAGE);
                                }
                                break;
                            case BUTTON3:
                                if (item.neuron instanceof OutputNeuron) {
                                    OutputNeuron neuron = (OutputNeuron) item.neuron;
                                    String result = JOptionPane.showInputDialog(JNeuronPanel.this,
                                            String.format("Enter ideal result for %s", item.getText()), neuron.getOutput());
                                    if (result != null) {
                                        try {
                                            neuron.couch(Double.parseDouble(result));
                                        } catch (Throwable ignored) {
                                            JOptionPane.showMessageDialog(JNeuronPanel.this,
                                                    "Invalid double format", "Error", ERROR_MESSAGE);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void setData(InputNeuron[] inputs, int[] hidden, OutputNeuron[] outputs) {
        items = prepareMap(inputs, hidden, outputs);
    }

    public List<Neuron> getNeurons() {
        List<Neuron> neurons = new LinkedList<>();
        for (MapItem item : items) {
            neurons.add(item.neuron);
        }
        return neurons;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        g.setColor(new Color(0xECECEC));
        g.fillRect(0, 0, getWidth(), getHeight());

        int ovalPadding = NEURON_DIAMETER / 2;
        for (MapItem item : items) {
            String text = item.getText();
            int x = item.getX();
            int y = item.getY();
            Color color = item.getColor();

            int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
            int textHeight = (int) (font.getStringBounds(text, frc).getHeight());

            item.getChilds().forEach(childItem -> {
                        int x1 = x + ovalPadding;
                        int x2 = childItem.x + ovalPadding;
                        int y1 = y + ovalPadding;
                        int y2 = childItem.y + ovalPadding;
                        g2d.setPaint(new GradientPaint(x1, y1, color.darker(), x2, y2, childItem.color.darker()));
                        g.drawLine(x1, y1, x2, y2);
                    }
            );
            g2d.setPaint(null);
            g.setColor(new Color(0x00, 0x00, 0x00, 0x20));
            g.fillOval(x, y + SHADOW_PADDING, NEURON_DIAMETER, NEURON_DIAMETER);
            g.setColor(color);
            g.fillOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
            g.setColor(color.darker());
            g.drawOval(x, y, NEURON_DIAMETER, NEURON_DIAMETER);
            g.setColor(color.darker().darker());
            g.drawString(text, x + ovalPadding - textWidth / 2, y + ovalPadding + textHeight / 3);
        }
    }

    private static List<MapItem> prepareMap(InputNeuron[] inputs, int[] hidden, OutputNeuron[] outputs) {
        if (inputs.length == 0) {
            return emptyList();
        }

        List<MapItem> items = new LinkedList<>();

        int maxCount = Math.max(inputs.length, outputs.length);
        for (int h : hidden) {
            maxCount = Math.max(maxCount, h);
        }
        int maxHeight = maxCount * NEURON_DIAMETER + (maxCount - 1) * VERTICAL_PADDING;
        int baseline = GLOBAL_PADDING + maxHeight / 2;

        Set<Neuron> neurons = new LinkedHashSet<>(Arrays.asList(inputs));
        Map<Neuron, MapItem> mapItemsMap = new LinkedHashMap<>();
        neurons.forEach(neuron -> mapItemsMap.put(neuron, new MapItem()));

        int y, x = GLOBAL_PADDING;
        Color color = Color.BLACK;
        boolean hasReceivers;
        do {
            hasReceivers = false;

            int neuronsSize = neurons.size() * NEURON_DIAMETER + (neurons.size() - 1) * VERTICAL_PADDING;
            y = baseline - neuronsSize / 2;

            Set<Neuron> current = new LinkedHashSet<>(neurons);
            Map<Neuron, MapItem> currentMapItemsMap = new LinkedHashMap<>(mapItemsMap);
            neurons.clear();
            mapItemsMap.clear();
            for (Neuron neuron : current) {
                MapItem mapItem = currentMapItemsMap.get(neuron);
                if (neuron instanceof InputNeuron) {
                    color = new Color(0xF8CAC1);
                } else if (neuron instanceof HiddenNeuron) {
                    color = new Color(0x83D6DE);
                } else if (neuron instanceof OutputNeuron) {
                    color = new Color(0x97CE68);
                }

                String text = neuron.getName();

                mapItem.neuron = neuron;
                mapItem.x = x;
                mapItem.y = y;
                mapItem.color = color;
                mapItem.text = text;
                items.add(mapItem);

                y += NEURON_DIAMETER + VERTICAL_PADDING;

                if (neuron instanceof Emitter) {
                    List<MapItem> childs = new LinkedList<>();
                    Emitter emitter = (Emitter) neuron;
                    for (Receiver receiver : emitter.getReceivers().keySet()) {
                        Neuron nextNeuron = (Neuron) receiver;
                        neurons.add(nextNeuron);
                        MapItem nextMapItem = mapItemsMap.get(nextNeuron);
                        if (nextMapItem == null) {
                            nextMapItem = new MapItem();
                        }
                        childs.add(nextMapItem);
                        mapItemsMap.put(nextNeuron, nextMapItem);
                    }
                    hasReceivers = !neurons.isEmpty();
                    mapItem.childs = childs.isEmpty() ? emptyList() : childs;
                }
            }

            x += NEURON_DIAMETER / 2 + HORIZONTAL_PADDING;
        } while (hasReceivers);

        return items;
    }

    private static class MapItem {

        private Neuron neuron;
        private int x, y;
        private String text;
        private Color color;
        private List<MapItem> childs;

        public MapItem() {
        }

        public Neuron getNeuron() {
            return neuron;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getText() {
            return text;
        }

        public Color getColor() {
            return color;
        }

        public List<MapItem> getChilds() {
            return childs;
        }
    }
}
