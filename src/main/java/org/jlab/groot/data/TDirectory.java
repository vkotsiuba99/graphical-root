package org.jlab.groot.data;

import java.util.List;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.math.F1D;
import org.jlab.groot.ui.TCanvas;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoNode;
import org.jlab.hipo.io.HipoReader;
import org.jlab.hipo.io.HipoWriter;

public class TDirectory extends Directory<IDataSet> {


    public TDirectory(){

    }

    public void addDataSet(IDataSet ds){
        add(ds.getName(),ds);
    }

    public void writeFile(String filename){

        HipoWriter  writer = new HipoWriter();
        writer.setCompressionType(1);
        writer.open(filename);

        List<String>  objectList = this.getCompositeObjectList(this);
        for(String object : objectList){
            //System.out.println("writing object ---> " + object);
            IDataSet ds = getObject(object);
            if(ds!=null){
                ds.setName(object);
                List<HipoNode>  nodes = DataSetSerializer.serializeDataSet(ds);
                HipoEvent       event = new HipoEvent();
                event.addNodes(nodes);
                writer.writeEvent(event.getDataBuffer());
            } else {
                System.out.println("[TDirectory::writeFile] error getting object : " + object);
            }
        }
        writer.close();
    }

    public void readFile(String filename){

        HipoReader reader = new HipoReader();
        reader.open(filename);
        clear();
        int nevents = reader.getEventCount();
        for(int i = 0; i < nevents; i++){
            byte[] eventBuffer = reader.readEvent(i);
            //System.out.println(" EVENT # " + i + "  SIZE = " + eventBuffer.length);
            HipoEvent    event = new HipoEvent(eventBuffer);
            //System.out.println(event.toString());
            IDataSet h1 = DataSetSerializer.deserializeDataSet(event);
            String h1name = h1.getName();
            //System.out.println("name -> : " + h1name + " -> " + this.stringDirectoryFromPath(h1name)
            //+ " obj ---> " + this.stringObjectFromPath(h1name));
            String dirname = this.stringDirectoryFromPath(h1name);
            mkdir(dirname);
            cd(dirname);
            //pwd();
            h1.setName(this.stringObjectFromPath(h1name));
            addDataSet(h1);
            this.ls();
        }
    }


    public static void main(String[] args){

        TDirectory dir = new TDirectory();
        dir.mkdir("/calibration/PCAL");
        dir.mkdir("/calibration/ECIN");
        dir.mkdir("/calibration/ECOUT");

        dir.cd("/calibration/PCAL");

        H1F h1pcal = new H1F("h1pcal",100,0.0,2.0);
        for(int i = 0; i < 600; i++) h1pcal.fill(Math.random()+Math.random());
        dir.addDataSet(h1pcal);

        dir.cd("/calibration/ECIN");
        H1F h1ecin = new H1F("h1ecin",100,0.0,2.0);
        H2F h2ecin = new H2F("h2ecin",120,0.0,2.0,120,0.0,2.0);
        for(int i = 0; i < 60000; i++){
            h1ecin.fill(Math.random()+Math.random());
            h2ecin.fill(Math.random()+Math.random(),Math.random()+Math.random());
        }
        dir.addDataSet(h1ecin);
        dir.addDataSet(h2ecin);

        dir.cd("/calibration/ECOUT");
        H1F h1ecout = new H1F("h1ecout",100,0.0,2.0);
        h1ecout.setLineColor(4);
        h1ecout.setLineWidth(3);
        h1ecout.setFillColor(3);
        h1ecout.setTitle("Random Generator");
        h1ecout.setTitleX("ep #rarrow ep#pi^+#pi^- [GeV^2]");
        h1ecout.setTitleY("counts");
        h1ecout.setOptStat(1111001111);
        F1D func  = new F1D("func","[amp]*gaus(x,[mean],[sigma])",0.0,2.0);
        F1D funcL = new F1D("func","[a]+[b]*x",0.5,3.5);
        func.setParameter(0, 25.0);
        func.setParameter(1, 0.5);
        func.setParameter(2, 0.5);
        for(int i = 0; i < 600; i++) h1ecout.fill(Math.random()+Math.random());

        GraphErrors graph = new GraphErrors("Attenuation");
        graph.getAttributes().setOptStat("111111");
        graph.addPoint(1.0, 1.5, 0.0, 0.2);
        graph.addPoint(2.0, 2.6, 0.0, 0.15);
        graph.addPoint(3.0, 3.4, 0.0, 0.10);

        dir.addDataSet(h1ecout);
        dir.addDataSet(graph);
        DataFitter.fit(func, h1ecout, "N");
        DataFitter.fit(funcL, graph, "N");
        //graph.setFunction(funcL);

        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        System.out.println(func);
        func.setLineColor(2);
        func.setLineWidth(3);
        //h1ecout.setFunction(func);
        //dir.addDataSet(new H1F("h1",100,0.1,0.5));
        dir.cd();
        dir.tree();
        dir.writeFile("testfile.hipo");


        TDirectory dirRead = new TDirectory();
        dirRead.readFile("testfile.hipo");
        dirRead.cd();
        H1F h1 = (H1F) dirRead.getObject("/calibration/ECOUT/h1ecout");
        H2F h2 = (H2F) dirRead.getObject("/calibration/ECIN/h2ecin");
        GraphErrors graphR = (GraphErrors) dirRead.getObject("/calibration/ECOUT/Attenuation");
        dirRead.cd("calibration");
        dirRead.pwd();
        System.out.println(h1);

        dirRead.cd();
        dirRead.tree();
        TCanvas c1 = new TCanvas("c1",900,900);
        c1.divide(2,2);
        c1.cd(0);
        c1.draw(h1);
        c1.cd(1);
        c1.draw(graphR);
        c1.cd(2);
        c1.draw(h2);
        System.out.println("***********************");
        System.out.println(h1.getFunction());
        //c1.draw(h1.getFunction(),"same");


//c1.draw(h1.getFunction());
    }
}
