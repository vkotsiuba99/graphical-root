package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import org.jlab.groot.base.TColorPalette;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.math.Dimension3D;

public class Histogram2DPlotter implements IDataSetPlotter {

    String plotterName = "";
    String plotterOptions = "";
    IDataSet dataSet = null;
    Dimension3D dataRegion = new Dimension3D();
    TColorPalette palette = new TColorPalette();

    public Histogram2DPlotter(IDataSet ds) {
        this.dataSet = ds;
    }

    @Override
    public String getOptions() {
        return plotterOptions;
    }

    @Override
    public void setOptions(String opt) {
        this.plotterOptions = opt;
    }

    @Override
    public String getName() {
        return dataSet.getName();
    }

    @Override
    public IDataSet getDataSet() {
        return this.dataSet;
    }

    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {

        int npointsX = dataSet.getDataSize(0);
        int npointsY = dataSet.getDataSize(1);
        frame.setDrawAxisZ(true);
        double dimMin = dataSet.getMin();
        double dimMax = dataSet.getMax();

        for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
            for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {
                double dataX = dataSet.getDataX(xd);
                double dataY = dataSet.getDataY(yd);
                double errorX = dataSet.getDataEX(xd);
                double errorY = dataSet.getDataEY(yd);

                double xps = frame.getAxisPointX(dataX - errorX * 0.5);
                double xpe = frame.getAxisPointX(dataX + errorX * 0.5);
                double yps = frame.getAxisPointY(dataY - errorY * 0.5);
                double ype = frame.getAxisPointY(dataY + errorY * 0.5);
                double height = yps - ype;

                double dataWeight = this.dataSet.getData(xd, yd);
                boolean zAxisLog = frame.getAxisZ().getLog();
                //System.out.println("2D plotter axis Z " + zAxisLog);

                Color weightColor;
                if (frame.getAxisZ().isAutoScale()) {
                    weightColor = palette.getColor3D(dataWeight,
                            dimMin, dimMax, zAxisLog);
                } else {
                    weightColor = palette.getColor3D(dataWeight,
                            frame.getAxisZ().getRange().getMin(), frame.getAxisZ().getRange().getMax(), zAxisLog);
                }
                g2d.setColor(weightColor);
                g2d.fillRect((int) xps, (int) ype,
                        (int) (xpe - xps) + 1,
                        (int) (yps - ype) + 1);
            }
        }

        if (this.dataSet.getAttributes().isDrawAxis()) {
            palette.draw(g2d, 50, 50, 50, 50, frame.getAxisZ().getRange().getMin(), dataSet.getMax(), frame.getAxisZ().getLog());
        }

    }

    @Override
    public Dimension3D getDataRegion() {
        if (this.dataSet.getDataSize(0) > 0 && this.dataSet.getDataSize(1) > 0) {
            this.dataRegion.set(
                    this.dataSet.getDataX(0),
                    this.dataSet.getDataX(0),
                    this.dataSet.getDataY(0),
                    this.dataSet.getDataY(0),
                    this.dataSet.getData(0, 0),
                    this.dataSet.getData(0, 0)
            );

            for (int xd = 0; xd < dataSet.getDataSize(0); xd++) {
                for (int yd = 0; yd < dataSet.getDataSize(1); yd++) {
                    dataRegion.grow(
                            dataSet.getDataX(xd) - dataSet.getDataEX(xd) * 0.5,
                            dataSet.getDataY(yd) - dataSet.getDataEY(yd) * 0.5,
                            dataSet.getData(xd, yd));
                    dataRegion.grow(
                            dataSet.getDataX(xd) + dataSet.getDataEX(xd) * 0.5,
                            dataSet.getDataY(yd) + dataSet.getDataEY(yd) * 0.5,
                            dataSet.getData(xd, yd)
                    );
                }
            }
        }
        //System.out.println(this.dataRegion.toString());
        return this.dataRegion;
    }

    Histogram2DPlotter setPalette(TColorPalette palette) {
        this.palette = palette;
        return this;
    }
}
