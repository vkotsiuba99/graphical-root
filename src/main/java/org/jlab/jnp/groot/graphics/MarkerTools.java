package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MarkerTools {

    public static void drawMarker(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){

    }

    public static void drawMarkerCyrcle(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        g2d.fillOval((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawOval((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
}
