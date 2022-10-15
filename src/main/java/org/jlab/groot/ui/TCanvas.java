package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.math.FunctionFactory;

public class TCanvas extends JFrame {

    JPanel  framePane = null;
    EmbeddedCanvas  canvas = null;
    JPanel  statusPane  = null;
    JLabel  statusLabel = null;

    public TCanvas(String name, int xsize, int ysize){
        super();
        this.setTitle(name);
        this.setSize(xsize, ysize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new EmbeddedCanvas(xsize,ysize);

        framePane= new JPanel();
        framePane.setLayout(new BorderLayout());
        framePane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 4, 4, 4), new EtchedBorder()));
        //framePane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        statusPane = new JPanel();
        statusPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Avenir",Font.PLAIN,12));
        statusPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPane.add(statusLabel);

        framePane.add(canvas, BorderLayout.CENTER);
        framePane.add(statusPane,BorderLayout.PAGE_END);

        this.add(framePane);
        this.pack();
        this.setVisible(true);
    }

    public EmbeddedCanvas  getCanvas(){
        return this.canvas;
    }

    public static void main(String[] args){
        TCanvas c1 = new TCanvas("c1",600,400);
        GraphErrors  graph = new GraphErrors();
        graph.addPoint(1.0, 1.0, 0.0, 0.0);
        graph.addPoint(2.0, 8.0, 0.0, 0.0);
        graph.addPoint(3.0, 12.0, 0.0, 0.0);
        graph.addPoint(4.0, 9.0, 0.0, 0.0);

        H1F  h1 = FunctionFactory.randomGausian(80, 0.1, 0.8, 8000, 0.6, 0.1);
        H1F  h2 = FunctionFactory.randomGausian(80, 0.1, 0.8, 20000, 0.3, 0.05);
        h1.setName("h100");
        h2.setName("h200");
        h2.setLineColor(1);
        h2.setFillColor(44);
        h1.setFillColor(33);
        GraphErrors  gr = h1.getGraph();
        c1.getCanvas().divide(5,4);
        c1.getCanvas().initTimer(3000);
        //c1.getCanvas().getPad(0).setAxisFontSize(14);
        //c1.getCanvas().getPad(0).addPlotter(new GraphErrorsPlotter(gr));
        for(int i = 0; i < c1.getCanvas().getNPads(); i++){
            c1.getCanvas().getPad(i).setAxisFontSize(12);
            c1.getCanvas().getPad(i).addPlotter(new HistogramPlotter(h1));
            c1.getCanvas().getPad(i).addPlotter(new HistogramPlotter(h2));
        }
        c1.getCanvas().update();
    }
}
