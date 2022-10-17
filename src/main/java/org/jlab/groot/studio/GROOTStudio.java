package org.jlab.groot.studio;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class GROOTStudio {

    JInternalFrame frame = null;
    private Map<String,IDataSet>  datasetStore = new HashMap<String,IDataSet>();
    EmbeddedCanvas canvas = null;

    public GROOTStudio(){
        canvas = new EmbeddedCanvas();//(500,400);
        frame = new JInternalFrame("GROOT Studio", true, true, true, true);
        frame.add(canvas);
        frame.pack();
    }

    public JInternalFrame getFrame(){
        return frame;
    }
}
