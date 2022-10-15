package org.jlab.groot.base;

public class PadMargins {
    int  leftMargin   = 0;
    int  rightMargin  = 0;
    int  topMargin    = 0;
    int  bottomMargin = 0;
    boolean isMarginsFixed    = false;


    public PadMargins(){

    }

    public boolean isFixed(){return isMarginsFixed;}
    public void    setFixed(boolean flag){ isMarginsFixed = flag;}
    public int  getLeftMargin(){ return leftMargin;}
    public int  getRightMargin(){ return rightMargin;}
    public int  getTopMargin() { return topMargin;}
    public int  getBottomMargin() { return bottomMargin;}
    public PadMargins setTopMargin(int margin){
        if(isMarginsFixed==false) topMargin = margin;
        return this;
    }
    public PadMargins setBottomMargin(int margin){
        if(isMarginsFixed==false) bottomMargin = margin;
        return this;
    }
    public PadMargins setLeftMargin(int margin){
        if(isMarginsFixed==false) leftMargin = margin;
        return this;
    }
    public PadMargins setRightMargin(int margin){
        if(isMarginsFixed==false) rightMargin = margin;
        return this;
    }

    public void copy(PadMargins p){
        setTopMargin(p.getTopMargin()).setBottomMargin(p.getBottomMargin());
        setLeftMargin(p.getLeftMargin()).setRightMargin(p.getRightMargin());
        setFixed(p.isFixed());
    }
}
