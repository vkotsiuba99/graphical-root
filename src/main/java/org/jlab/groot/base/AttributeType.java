package org.jlab.groot.base;

public enum AttributeType {

    UNDEFINED        ( 0, "undefined"),
    LINE_COLOR       ( 1, "LINE_COLOR"),
    LINE_STYLE       ( 2, "LINE_STYLE"),
    LINE_WIDTH       ( 3, "LINE_WIDTH"),
    FILL_COLOR       ( 4, "FILL_COLOR"),
    FILL_STYLE       ( 5, "FILL_STYLE"),
    MARKER_COLOR     ( 6, "MARKER_COLOR"),
    MARKER_STYLE     ( 7, "MARKER_STYLE"),
    MARKER_SIZE      ( 8, "MARKER_SIZE"),
    MARING_TOP       ( 10, "MARGIN_TOP"),
    MARING_BOTTOM    ( 11, "MARGIN_TOP"),
    MARING_LEFT      ( 12, "MARGIN_TOP"),
    MARING_RIGHT     ( 13, "MARGIN_TOP"),
    AXIS_LINE_WIDTH    ( 14, "AXIS_LINE_WIDTH"),
    AXIS_TICKMARK_SIZE (15,"AXIS_TICKMARK_SIZE"),
    AXIS_BACKGROUND_COLOR (16,"AXIS_BACKGROUND_COLOR")
    ;

    private int attributeId ;
    private String attributeName;
    //private String attributeDescription;

    AttributeType(){
        attributeId   = 0;
        attributeName = "undefined";
    }

    AttributeType(int id, String name){
        attributeId   = id;
        attributeName = name;
    }

    public int    getType(){ return attributeId; }
    public String getName() { return attributeName; }

    public static AttributeType getType(String name) {
        name = name.trim();
        for(AttributeType id: AttributeType.values())
            if (id.getName().equalsIgnoreCase(name))
                return id;
        return UNDEFINED;
    }

    public static AttributeType getType(Integer detId) {

        for(AttributeType id: AttributeType.values())
            if (id.getType() == detId)
                return id;
        return UNDEFINED;
    }
}
