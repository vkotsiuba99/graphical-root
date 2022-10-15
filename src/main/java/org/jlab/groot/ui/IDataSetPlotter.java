package org.jlab.groot.ui;

import java.awt.Graphics2D;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;

public interface IDataSetPlotter {
    String       getOptions();
    void         setOptions(String opt);
    String       getName();
    IDataSet     getDataSet();
    void         draw(Graphics2D  g2d, GraphicsAxisFrame frame);
    Dimension2D  getDataRegion();
}
