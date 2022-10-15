package org.jlab.groot.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsTests extends JPanel {

    ColorPalette palette = new ColorPalette();

    public GraphicsTests(){
        super();
        this.setPreferredSize(new Dimension(500,500));
    }

    @Override
    public void paint(Graphics g){

        Long st = System.currentTimeMillis();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = this.getSize().width;
        int h = this.getSize().height;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        int axisWidth = palette.getAxisWidth(g2d,20,20,15,h-30,0.0,400.0,true);
        palette.draw(g2d, w-axisWidth,20,20,h-40,0.0,400.0,true);
    }


    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsTests canvas = new GraphicsTests();
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

}
