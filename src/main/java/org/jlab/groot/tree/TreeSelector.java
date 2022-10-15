package org.jlab.groot.tree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeSelector {

    Map<String,TreeCut>  treeCuts = new LinkedHashMap<String,TreeCut>();

    public TreeSelector(){

    }

    public void addCut(String name, String expression, List<String> branches){
        treeCuts.put(name, new TreeCut(name,expression,branches));
    }

    public void addCut(TreeCut cut){
        this.treeCuts.put(cut.getName(), cut);
    }

    public TreeCut getCut(String cut){
        return treeCuts.get(cut);
    }

    public boolean isValid(Tree tree){
        for(Map.Entry<String,TreeCut> entry : treeCuts.entrySet()){
            if(entry.getValue().isActive()==true){
                if(entry.getValue().isValid(tree)==false) return false;
            }
        }
        return true;
    }

    public Map<String,Integer>  getCuts(Tree tree){
        Map<String,Integer>  cuts = new LinkedHashMap<String,Integer>();
        for(Map.Entry<String,TreeCut> entry : treeCuts.entrySet()){
            if(entry.getValue().isActive()==true){
                if(entry.getValue().isValid(tree)==false){
                    cuts.put(entry.getKey(), 0);
                } else {
                    cuts.put(entry.getKey(), 1);
                }
            } else {
                cuts.put(entry.getKey(), 0);
            }
        }
        return cuts;
    }

    public Map<String,TreeCut>  getSelectorCuts(){
        return this.treeCuts;
    }

    public void reset(){
        treeCuts.clear();
    }
}
