package org.jlab.groot.fitter;

import org.freehep.math.minuit.FCNBase;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Func1D;

public class FitterFunction implements FCNBase {

    private Func1D    function;
    private IDataSet  dataset;
    private String    fitOptions = "E";

    public FitterFunction(Func1D func, IDataSet data){
        dataset  = data;
        function = func;
    }

    public FitterFunction(Func1D func, IDataSet data,String options){
        dataset    = data;
        function   = func;
        fitOptions = options;
    }

    public Func1D getFunction(){return function;}

    @Override
    public double valueOf(double[] pars) {
        double chi2 = 0.0;
        function.setParameters(pars);
        chi2 = getChi2(pars,fitOptions);
        //function.show();
        //System.err.println("\n************ CHI 2 = " + chi2);
        return chi2;
    }


    public double getChi2(double[] pars, String options){

        double chi2 = 0.0;
        int npoints = dataset.getDataSize(0);
        function.setParameters(pars);

        for(int np = 0; np < npoints; np++){
            double x = dataset.getDataX(np);
            double y = dataset.getDataY(np);
            if(x>=function.getMin()&&x<=function.getMax()){
                double yv = function.evaluate(x);
                double normalization = 1.0;
                if(options.contains("R")==true){
                    normalization = yv;
                }
                if(options.contains("N")==true){
                    normalization = y;
                }
                if(normalization>0.000000000001){
                    chi2 += (yv-y)*(yv-y)/normalization;
                }
            }
        }
        return chi2;
    }
}
