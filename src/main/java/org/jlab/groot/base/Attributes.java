package org.jlab.groot.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class Attributes {
    Map<AttributeType,Integer>  attributesMap = new LinkedHashMap<AttributeType,Integer>();

    public Attributes(){

    }

    public void add(AttributeType type, int value){
        this.attributesMap.put(type, value);
    }

    public boolean hasAttribute(AttributeType type){
        return this.attributesMap.containsKey(type);
    }

    public int get(AttributeType type){
        return this.attributesMap.get(type);
    }

    public Map<AttributeType,Integer>  getMap(){return this.attributesMap;}
    /**
     * makes a copy of an attributes.
     * @param attr
     */
    public void copy(Attributes attr){
        this.attributesMap.clear();
        for(Map.Entry<AttributeType,Integer> entry : attr.getMap().entrySet()){
            this.attributesMap.put(entry.getKey(), entry.getValue());
        }
    }
    /**
     * from given attributes copies values of entries that exist in this class
     * @param attr
     */
    public void copyValues(Attributes attr){
        for(Map.Entry<AttributeType,Integer> entry : getMap().entrySet()){
            if(attr.getMap().containsKey(entry.getKey())==true){
                this.attributesMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<AttributeType,Integer> entry : getMap().entrySet()){
            str.append(String.format("* %-24s * %14d *\n",entry.getKey().getName(),
                    entry.getValue()));
        }
        return str.toString();
    }
}
