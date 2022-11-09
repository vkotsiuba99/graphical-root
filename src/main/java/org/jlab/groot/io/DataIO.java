package org.jlab.groot.io;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.H1F;

public class DataIO {

    public static void csvH1(H1F h, String filename, int column, int skip){
        TextFileReader reader = new TextFileReader(",");
        reader.openFile(filename);
        for(int i = 0; i < skip; i++){
            reader.readNext();
        }
        int[] columns = new int[]{column};

        while(reader.readNext()==true){

            double[] row = reader.getAsDouble(columns);
            h.fill(row[0]);
        }
    }


}
