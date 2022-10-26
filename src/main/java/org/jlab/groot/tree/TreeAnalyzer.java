package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.groot.data.IDataSet;

public class TreeAnalyzer {
    List<DatasetDescriptor>   datasets = new ArrayList<DatasetDescriptor>();

    public TreeAnalyzer(){

    }

    public void addDescriptor(DatasetDescriptor desc){
        this.datasets.add(desc);
    }

    public void process(ITree tree){
        tree.reset();

        while(tree.readNext()==true){
            for(DatasetDescriptor desc : datasets){
                //if(desc.getDataSet() instanceof )
            }
        }
    }

    public List<DatasetDescriptor>  getDescriptors(){
        return this.datasets;
    }

    public DefaultMutableTreeNode getTree() {

        DefaultMutableTreeNode root         = new DefaultMutableTreeNode("Analyzer");

        for(DatasetDescriptor desc : datasets){
            //root.add(new DefaultMutableTreeNode(desc.getName()));
        }
        return root;
    }

}
