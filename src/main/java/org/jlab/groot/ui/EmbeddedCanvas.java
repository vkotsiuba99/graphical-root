package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EmbeddedCanvas extends JPanel {

    EmbeddedPad  pad = new EmbeddedPad();

    public EmbeddedCanvas(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = this.getSize().width;
        int h = this.getSize().height;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        pad.setDimension(0, 0, w, h);

        pad.draw(g2d);
    }

    public EmbeddedPad  getPad(int index){
        return this.pad;
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        EmbeddedCanvas canvas = new EmbeddedCanvas(400,400);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
