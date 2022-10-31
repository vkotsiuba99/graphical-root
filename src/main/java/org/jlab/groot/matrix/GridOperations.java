package org.jlab.groot.matrix;

import java.util.Map;
import org.jlab.groot.data.DataVector;

public class GridOperations {
    public static SparseVectorGrid divideGrid(SparseVectorGrid gridNom,
                                              SparseVectorGrid gridDenom, int orderNom, int orderDenom, boolean extend){
        if(gridDenom.getIndexer().isCompatible(gridNom.getIndexer())==true){
            SparseVectorGrid grid = new SparseVectorGrid(gridNom.getIndexer().getBinsPerAxisCopy());
            for(Map.Entry<Long,DataVector> entry : gridNom.getGrid().entrySet()){
                double valueNom = entry.getValue().getValue(orderNom);
                if(gridDenom.getGrid().containsKey(entry.getKey())==true){
                    double valueDenom = gridDenom.getGrid().get(entry.getKey()).getValue(orderDenom);
                    if(valueDenom!=0){
                        double result = valueNom/valueDenom;
                        DataVector  vec = new DataVector(1);
                        vec.set(0, result);
                        grid.getGrid().put(entry.getKey(),vec );
                    }
                }
            }
        }
        return new SparseVectorGrid();
    }


}
