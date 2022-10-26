package org.jlab.groot.tree;

import javax.swing.JDialog;
import org.jlab.groot.data.TDirectory;

public abstract class AbstractDescriptor {

    TDirectory directory = new TDirectory();

    public AbstractDescriptor(){

    }

    public abstract void     processTreeEvent(Tree tree);
    public abstract JDialog  edit();

}
