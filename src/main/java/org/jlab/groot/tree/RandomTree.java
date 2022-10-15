package org.jlab.groot.tree;

import java.util.List;

public class RandomTree extends Tree {

    int   nrowsRead = 0;

    public RandomTree() {
        super("RandomTree");
        addBranch("q2","Virtual Photon","GeV^2");
        addBranch("w2","Missing Mass","GeV^2");
        addBranch("epX","Missing Mass (ep)","GeV");
    }

    @Override
    public void reset(){
        nrowsRead = 0;
    }

    public boolean readNext(){
        if(nrowsRead>=100) return false;
        Double q2 = 1.0 + Math.random()*5;
        Double w2 = 0.5 + Math.random()*8;
        Double epx = Math.random();
        this.getBranch("q2").setValue(q2);
        this.getBranch("w2").setValue(w2);
        this.getBranch("epX").setValue(epx);
        nrowsRead++;
        return true;
    }

    public static void main(String[] args){
        RandomTree  tree = new RandomTree();
        tree.configure();
        tree.show();

        List<String> branches = tree.getListOfBranches();
        TreeCut  q2cut = new TreeCut("q2cut","q2>2.0&epX>0.5",branches);

        int counter = 0;
        while(tree.readNext()==true){
            boolean status = q2cut.isValid(tree);
            counter++;
            System.out.println( counter + " : " + status);
        }
    }
}
