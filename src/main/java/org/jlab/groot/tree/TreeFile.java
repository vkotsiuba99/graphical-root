package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.DataVector;

public class TreeFile extends Tree {

    List<DataVector>  dataVectors = new ArrayList<DataVector>();

    public TreeFile(String name) {
        super(name);
    }

    public TreeFile(String name, String filename, int columns) {
        super(name);
    }

    public void readFile(String file){

    }

    public void openFile(){

    }
}
