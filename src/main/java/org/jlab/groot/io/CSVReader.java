package org.jlab.groot.io;

import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public CSVReader(){

    }

    public List<double[]> readColumn(String filename, int[] columns, int skip){
        TextFileReader reader = new TextFileReader(",");
        reader.openFile(filename);
        for(int i = 0; i < skip; i++){
            reader.readNext();
        }
        List<double[]> data = new ArrayList<>();
        while(reader.readNext()==true){
            double[] row = reader.getAsDouble(columns);
            data.add(row);
        }
        return data;
    }
}
