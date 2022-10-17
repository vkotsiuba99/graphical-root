package org.jlab.groot.ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

public class LatexText {

    private String  textFamily   = "Avenir";
    private int     textFontSize = 12;
    private Double  relativeX  = 0.0;
    private Double  relativeY  = 0.0;
    private AttributedString  latexString = null;
    private String            asciiString = "";
    private Integer           textColor   = 1;


    public LatexText(String text, double xc, double yc){
        this.setText(text);
        this.setLocation(xc, yc);
        this.setFont(textFamily);
        this.setFontSize(textFontSize);
    }

    public LatexText(String text){
        this.setText(text);
        this.setLocation(0.0,0.0);
        this.setFont(textFamily);
        this.setFontSize(textFontSize);
    }

    public final void setText(String text){
        asciiString = text;
        String ltx  = LatexTextTools.convertUnicode(text);
        latexString = LatexTextTools.converSuperScript(ltx);
        this.setFont(this.textFamily);
        this.setFontSize(this.textFontSize);
    }

    public final String getTextString(){
        return asciiString;
    }

    public final void setLocation(double xr, double yr){
        this.relativeX = xr;
        this.relativeY = yr;
    }

    public int    getColor(){return this.textColor;}
    public void   setColor(int color){ this.textColor = color;}

    public double getX(){ return this.relativeX;}
    public double getY(){ return this.relativeY;}

    public AttributedString getText(){ return this.latexString;}

    public final void setFont(String fontname){
        this.textFamily = fontname;
        if(this.latexString.getIterator().getEndIndex()>0){
            //System.out.println("INDEX = " + this.latexString.getIterator().getEndIndex());
            latexString.addAttribute(TextAttribute.FAMILY, fontname);
        }
    }

    public final void setFontSize(int size){
        this.textFontSize = size;
        if(this.latexString.getIterator().getEndIndex()>0){
            latexString.addAttribute(TextAttribute.SIZE, (float) size);
        }
    }

    public void drawString(Graphics2D  g2d, int x, int y, int alignX, int alignY){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        int  ascend   = fmg.getAscent();
        int leading   = fmg.getLeading();
        //System.out.println("ascend = " + ascend + " leading = " + leading);
        int  xp     = x;
        int  yp     = y + ascend;
        if(alignX==1) xp = (int) (xp-0.5*rect.getWidth());
        if(alignX==2) xp = (int) (xp-rect.getWidth());
        if(alignY==1) yp = (int) (y + 0.5*(ascend));
        if(alignY==2) yp = (int)  y;
        g2d.drawString(latexString.getIterator(), xp, yp);
    }

    public  Rectangle2D  getBoundsNumber(Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        //System.out.println(" ACCEND = " + fmg.getAscent() + " LEAD " + fmg.getLeading()
        //+ "  DESCENT " + fmg.getDescent() + "  HEIGHT = " + fmg.getHeight());
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        //fmg.getHeight();
        return new Rectangle2D.Double(0,0,rect.getWidth(),fmg.getAscent());
    }

    public  Rectangle2D getBounds( Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        //System.out.println(" ACCEND = " + fmg.getAscent() + " LEAD " + fmg.getLeading()
        //+ "  DESCENT " + fmg.getDescent() + "  HEIGHT = " + fmg.getHeight());
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        //fmg.getHeight();
        //return new Rectangle2D.Double(0,0,rect.getWidth(),fmg.getAscent());
        return rect;
    }

    public  Rectangle2D getBounds(FontMetrics  fm, Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        return rect;
    }

    public static LatexText createFromDouble(double number, int order){
        //System.out.println("LatexUtils: number = " + number + "  order = " + order);
        String format = "%." + String.format("%df", order);
        String numString = String.format(format, number);
        //System.out.println("[opt] --> released string : " + numString);
        return new LatexText(numString);
    }

    public static void main(String[] args){
        LatexText lt = LatexText.createFromDouble(0.56789, 2);
    }
}
