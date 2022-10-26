package org.jlab.groot.tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jlab.groot.data.DataVector;

public class TreeTextFile extends Tree {

    private static int TREEFILE_CSV       = 1;
    private static int TREEFILE_SPACE     = 2;
    private static int TREEFILE_SEMICOLON = 3;

    private String dataSeparator = "\\s+";
    private int    textFileType  = 2;

    private List<DataVector>  dataVectors = new ArrayList<DataVector>();
    private int               currentData = 0;

    public TreeTextFile(String name) {
        super(name);

    }

    public TreeTextFile(String name, int type) {
        super(name);
        this.textFileType = type;
        if(type==TreeTextFile.TREEFILE_CSV){
            dataSeparator = ",";
        }
        if(type==TreeTextFile.TREEFILE_SEMICOLON){
            dataSeparator = ";";
        }
    }

    public TreeTextFile(String name, String filename, int columns) {
        super(name);
    }

    public void readFile(String filename){
        this.readFile(filename, dataSeparator);
    }

    public void readFile(String filename, String separator){

        dataVectors.clear();
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =  new FileReader(filename);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =  new BufferedReader(fileReader);
            int lineNumber = 0;
            int firstLineRead = 0;

            while((line = bufferedReader.readLine()) != null) {
                if(line.startsWith("#")==false){
                    //System.out.println(line);
                    String[] tokens = line.split(separator);
                    if(tokens.length>0){
                        DataVector  vector = new DataVector();
                        for(int i = 0; i < tokens.length; i++){

                            try {
                                double value = Double.parseDouble(tokens[i]);
                                vector.add(value);
                            } catch (Exception e) {

                            }
                        }

                        if(dataVectors.size()>0){
                            if(vector.getSize()!=dataVectors.get(0).getSize()){
                                System.out.println("[TreeTextFile::read] ---> error on line # " + lineNumber);
                            } else {
                                dataVectors.add(vector);
                            }
                        } else {
                            dataVectors.add(vector);
                        }

                        if(firstLineRead==0&&this.getListOfBranches().size()==0){
                            int size = vector.getSize();
                            String[] names = this.generateBranchNames(size);
                            this.initBranches(names);
                            firstLineRead++;
                        }
                    }
                }else{
                    line = line.replace("#!","");
                    String[] labels = line.split(":");
                    initBranches(labels);
                }
                lineNumber++;
            }
            // Always close files.
            bufferedReader.close();
            this.currentData = 0;
        }
        catch(FileNotFoundException ex) {
            //ClasUtilsFile.printLog("Unable to open file : '" + filename + "'");
        }
        catch(IOException ex) {
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }


    private String[] generateBranchNames(int count){
        byte currentH = 'a';
        byte currentL = 'a';
        int  clow = 0;
        String[] branchNames = new String[count];
        for(int i = 0; i < count; i++){
            branchNames[i] = new String(new byte[]{currentH,currentL});
            currentL++;
            clow++;
            if(clow>=26){
                clow = 0;
                currentL = 'a';
                currentH++;
            }
        }
        return branchNames;
    }

    public void initBranches(String[] names){
        this.getBranches().clear();
        for(String n : names){
            addBranch(n,"","");
            //System.out.println("---> adding branch : " + n);
        }
        System.out.println("[TreeTextFile::init] ---> initializing branches. count = " + this.getBranches().size());
    }

    public double[] getRow(){
        double[] data = new double[getBranches().size()];

        return data;
    }

    @Override
    public void reset(){
        this.currentData = 0;
    }

    @Override
    public boolean readNext() {
        if(this.currentData>=this.dataVectors.size()) return false;
        this.readEntry(currentData);
        currentData++;
        return true;
    }

    public void openFile(){

    }

    @Override
    public int getEntries(){
        return this.dataVectors.size();
    }

    @Override
    public int readEntry(int entry){
        DataVector vec = this.dataVectors.get(entry);
        int icounter = 0;
        for(Map.Entry<String,Branch> branches : getBranches().entrySet()){
            String key = branches.getKey();
            branches.getValue().setValue(vec.getValue(icounter));
            icounter++;
        }
        return 1;
    }

    public static void main(String[] args){
        TreeTextFile tree = new TreeTextFile("T");
        tree.readFile("/Users/vkotsiuba99/Desktop/pp_10k.txt");
        System.out.println( " entries = " + tree.getEntries());
        for(int i = 0; i < 10; i++){
            tree.readEntry(i);
            tree.print();
        }

        DataVector vec = tree.getDataVector("vec3m(ac,ad,ae)", "ac>0.5&&ad>0.5", 10);
        System.out.println(" datavector size =  " + vec.getSize());
        for(int i = 0; i < vec.getSize(); i++){
            System.out.println(" element " + i + " =  " + vec.getValue(i));
        }
    }
}
