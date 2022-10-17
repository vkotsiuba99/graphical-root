package org.jlab.groot.data;

import org.jlab.groot.base.Attributes;
import org.jlab.groot.ui.PaveText;

public interface IDataSet {
    void        setName(String name);
    String      getName();
    int         getDataSize(int axis);
    Attributes  getAttributes();
    double      getDataX(int bin);
    double      getDataY(int bin);
    double      getDataEX(int bin);
    double      getDataEY(int bin);
    double      getData(int xbin, int ybin);
    PaveText    getStatBox();
}
