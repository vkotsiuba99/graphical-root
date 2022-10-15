package org.jlab.groot.base;

public class TStyle {

    public static Attributes  grootStyle = TStyle.createAttributes();

    public static Attributes createAttributes(){
        Attributes attr = new Attributes();
        attr.add(AttributeType.LINE_COLOR, 0);
        attr.add(AttributeType.LINE_WIDTH, 1);
        attr.add(AttributeType.LINE_STYLE, 1);
        attr.add(AttributeType.FILL_COLOR, 4);
        return attr;
    }

}
