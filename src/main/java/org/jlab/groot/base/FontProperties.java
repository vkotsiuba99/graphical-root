package org.jlab.groot.base;

public class FontProperties {
    private String fontName = "Avenir";
    private int    fontSize = 12;

    public FontProperties(){

    }

    public FontProperties setFontName(String name){
        this.fontName = name;
        return this;
    }

    public FontProperties setFontSize(int size){
        this.fontSize = size;
        return this;
    }

    public int getFontSize(){
        return this.fontSize;
    }

    public String getFontName(){
        return this.fontName;
    }
}
