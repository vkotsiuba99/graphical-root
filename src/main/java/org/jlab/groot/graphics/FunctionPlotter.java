package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

public class FunctionPlotter implements IDataSetPlotter {

    private Dimension3D dataRegion  = new Dimension3D();
    private String      drawOptions = "";
    private IDataSet    functionData = null;

    public FunctionPlotter(IDataSet func){
        functionData = func;
    }

    @Override
    public String getOptions() {
        return this.drawOptions;
    }

    @Override
    public void setOptions(String opt) {
        this.drawOptions = opt;
    }

    @Override
    public String getName() {
        return this.functionData.getName();
    }

    @Override
    public IDataSet getDataSet() {
        return this.functionData;
    }

    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {
        int npoints = functionData.getDataSize(0);
        GeneralPath path = new GeneralPath();
        double xp = frame.getAxisPointX(functionData.getDataX(0));
        double yp = frame.getAxisPointY(functionData.getDataY(0));
        path.moveTo(xp, yp);
        for(int p = 0; p < npoints; p++){
            xp = frame.getAxisPointX(functionData.getDataX(p));
            yp = frame.getAxisPointY(functionData.getDataY(p));
            path.lineTo(xp, yp);
        }
        int lineColor = functionData.getAttributes().get(AttributeType.LINE_COLOR);
        int lineWidth = functionData.getAttributes().get(AttributeType.LINE_WIDTH);

        g2d.setColor(TStyle.getColor(lineColor));
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.draw(path);
    }

    @Override
    public Dimension3D getDataRegion() {
        double xp = this.functionData.getDataX(0);
        double yp = this.functionData.getDataY(0);
        this.dataRegion.set(xp,xp,yp,yp,0.0,1.0);
        int npoints = functionData.getDataSize(0);
        for(int i = 0; i < npoints; i++){
            this.dataRegion.grow(functionData.getDataX(i), functionData.getDataY(i),0.5);
        }
        return dataRegion;
    }

}
