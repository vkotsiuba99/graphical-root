package org.jlab.groot.data;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class DatasetOperations extends JPanel {

    Map<String,IDataSet>  datasetObjects = null;

    public DatasetOperations(Map<String,IDataSet> datasets){
        super();
        datasetObjects = datasets;

    }

    public static JDialog createOperations(JComponent parent,Map<String,IDataSet> datasets){
        return null;
    }

}
