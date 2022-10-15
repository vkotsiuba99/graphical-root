package org.jlab.groot.graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.math.Dimension1D;

public class GraphAxis {

    private final  Dimension1D                  axisRange = new Dimension1D();
    private final  Dimension1D              axisDimension = new Dimension1D();

    private        int                 numberOfMajorTicks = 10;
    private        Boolean                  isLogarithmic = false;
    private        Boolean                  isVertical    = false;

    private final  FontProperties           axisLabelFont     = new FontProperties();
    private final  FontProperties           axisTitleFont     = new FontProperties();

    /**
     * default
     */
    public GraphAxis(){
        this.setDimension( 0, 100);
        this.setRange( 0.0, 1.0);
        //this.axisLabels.setFontName("Avenir");
        //this.axisLabels.setFontSize(12);
    }
    /**
     * sets the dimension for the axis for plotting.
     * @param xmin
     * @param xmax
     * @return
     */
    public final GraphAxis setDimension(int xmin, int xmax){
        this.axisDimension.setMinMax(xmin, xmax);
        return this;
    }

    public Dimension1D getDimension(){
        return this.axisDimension;
    }

    /**
     * Sets the range of the axis, it also updates the axis labels.
     * @param min
     * @param max
     * @return
     */
    public final GraphAxis setRange(double min, double max){
        this.axisRange.setMinMax(min, max);
        if(this.isLogarithmic==true){
            List<Double> ticks = axisRange.getDimensionTicksLog(this.numberOfMajorTicks);
            axisLabels.updateLog(ticks);
        } else {
            List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
            axisLabels.update(ticks);
        }
        return this;
    }

    public Dimension1D  getRange(){
        return this.axisRange;
    }
    /**
     * prints string representation of the axis.
     */
    public void show(){
        System.out.println(this.toString());
    }
    /**
     * returns position along the axis, the dimension of the
     * axis has to be set.
     * @param value
     * @return
     */
    public double getAxisPosition(double value){
        double fraction = this.axisRange.getFraction(value);
        return this.axisDimension.getPoint(fraction);
    }
    /**
     * returns properties of the title font
     * @return
     */
    public FontProperties   getTitleFont(){
        return this.axisTitleFont;
    }
    /**
     * returns properties for the label fonts.
     * @return
     */
    public FontProperties   getLabelFont(){
        return this.axisLabelFont;
    }


    public int  getSize(Graphics2D g2d, boolean vertical){
        if(vertical==true){
            return this.axisLabels.getAxisMaxWidth(g2d);
        } else {
            return this.axisLabels.getAxisMaxHeight(g2d);
        }
    }

    public int   getAxisDivisions(){
        return this.numberOfMajorTicks;
    }

    public void  setAxisDivisions(int ndiv){
        this.numberOfMajorTicks = ndiv;
    }

    public List<Double>  getAxisTicks(){
        return null;//this.axisLabels.getTicks();
    }

    /*
    public List<LatexText>  getAxisStrings(){
        return this.axisLabels.getLabels();
    }*/
    /**
     * returns string representation of the axis.
     * @return
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Axis : ");
        str.append(" LOG = ");
        str.append(this.isLogarithmic);
        str.append(String.format("  (%4d x %4d ) : ->  ", (int) axisDimension.getMin(),
                (int) axisDimension.getMax()));
        //str.append(axisLabels.toString());
        return str.toString();
    }
    /**
     * main program for testing the axis
     * @param args
     */
    public static void main(String[] args){
        GraphAxis  axis = new GraphAxis();
        axis.setDimension(20, 100);
        axis.setRange(0.0, 120.0);
        //axis.setLog(true);
        axis.show();
        for(double d = 0; d < 120; d+=0.5){
            System.out.println( d + " fraction = " + axis.getAxisPosition(d));
        }
    }
}
