package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.groot.data.DataVector;

public class Tree implements ITree {


    private  static Map<String,ImageIcon>   treeNodeIcons  = Tree.initTreeIcons();


    private String treeName = "tree";
    private  final Map<String,Branch>              treeBranches  = new LinkedHashMap<String,Branch>();
    private  final TreeSelector                 defaultSelector  = new TreeSelector();
    private  final List<DataVector>                 scanResults  = new ArrayList<DataVector>();

    public Tree(String name){
        this.treeName = name;
    }
    /**
     * returns the name of the tree
     * @return
     */
    @Override
    public String getName(){return this.treeName;}

    public void addBranch(Branch br){
        treeBranches.put(br.getName(), br);
    }
    /**
     * add Branch with name description and units
     * @param name name of the branch
     * @param desc description of the branch
     * @param unit units of the branch
     */
    public void addBranch(String name, String desc, String unit){
        addBranch(new Branch(name,desc,unit));
    }
    /**
     * returns list of branch names. User to define cuts and expressions
     * @return
     */
    @Override
    public List<String> getListOfBranches() {
        List<String> listOfBranches = new ArrayList<String>();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            listOfBranches.add(entry.getKey());
        }
        return listOfBranches;
    }
    /**
     * returns icons for the tree
     * @return
     */
    public static Map<String,ImageIcon>  getTreeIcons(){
        return treeNodeIcons;
    }
    /**
     * initializes UI
     * @return
     */
    public static Map<String,ImageIcon>  initTreeIcons(){
        Map<String,ImageIcon> iconMap = new HashMap<String,ImageIcon>();
        try {
            ImageIcon leafIcon = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/leaf_t.png"));
            ImageIcon dirIcon  = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/tree_t.png"));
            UIManager.put("Tree.closedIcon", dirIcon);
            UIManager.put("Tree.openIcon",   dirIcon);
            UIManager.put("Tree.leafIcon",  leafIcon);
            UIManager.put("Tree.paintLines", Boolean.TRUE);
        } catch(Exception e){

        }
        return iconMap;
    }

    public void resetBranches(double number){
        for(Map.Entry<String,Branch> entry : this.treeBranches.entrySet()){
            entry.getValue().setValue(number);
        }
    }
    /**
     * returns a float array containing branch data.
     * @return float array
     */
    public float[] getBranchData(){
        float[] data = new float[this.treeBranches.size()];
        int icounter = 0;
        for(Map.Entry<String,Branch> entry : this.treeBranches.entrySet()){
            data[icounter] = entry.getValue().getValue().floatValue();
            icounter++;
        }
        return  data;
    }
    /**
     * Fills branches with values from the array.
     * @param data float array to fill branches
     */
    public void setBranchData(float[] data){
        if(data.length!=treeBranches.size()){
            System.out.println("[setBranchData] error. array size = " + data.length + " "
                    + "number of branches = " + treeBranches.size());
            return;
        }
        int icounter = 0;
        for(Map.Entry<String,Branch> entry : this.treeBranches.entrySet()){
            Double value = (double) data[icounter];
            entry.getValue().setValue(value);
            icounter++;
        }
    }

    @Override
    public Branch getBranch(String name) {
        return this.treeBranches.get(name);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean readNext() {
        return false;
    }

    public Map<String,Branch>  getBranches(){
        return this.treeBranches;
    }

    public void print(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            str.append(entry.getValue().toString());
            str.append("  ");
            str.append(entry.getValue().getValue());
            str.append("\n");
        }
        System.out.println( str.toString());
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            str.append(entry.getValue().toString());
            str.append("\n");
        }
        return str.toString();
    }

    public void show(){
        System.out.println(toString());
    }

    @Override
    public void configure() {
        System.out.println("[Tree] --> generic tree class does not require configuring.");
    }
    /**
     * returns default tree selector. cuts can be added to internal selector
     * @return
     */
    public TreeSelector  getSelector(){
        return this.defaultSelector;
    }
    /**
     * returns a list of values from the tree branch for all rows
     * that pass the cuts described in default tree selector
     * @param branch
     * @return
     */
    public List<Double>  getVector(String branch){
        return getVector(branch,defaultSelector);
    }
    /**
     * returns a list of values from the tree branch for rows that pass
     * the cuts described by selector.
     * @param branch
     * @param selector
     * @return
     */
    public List<Double>  getVector(String branch, TreeSelector selector){
        this.reset();
        List<Double> vector = new ArrayList<Double>();
        while(readNext()==true){
            if(selector.isValid(this)==true){
                vector.add(getBranch(branch).getValue().doubleValue());
            }
        }
        return vector;
    }
    /**
     * returns data vector filled with values evaluated with expression given
     * for entries that pass the given cut
     * @param expression variables expression
     * @param tcut cut string
     * @return DataVector with expression values
     */
    public DataVector  getDataVector(String expression, String tcut){
        return getDataVector(expression,tcut,-1);
    }
    /**
     * returns data vector filled with values evaluated with expression given
     * for entries that pass the given cut. The limit is set on variables.
     * @param expression variables expression
     * @param tcut cut string
     * @param limit limit the number of events to run through -1 for all events
     * @return
     */
    public DataVector  getDataVector(String expression, String tcut, int limit){

        DataVector vec = new DataVector();
        int nentries = getEntries();
        TreeExpression exp = new TreeExpression("oper",expression,getListOfBranches());
        TreeCut        cut = new TreeCut("cut",tcut,getListOfBranches());

        for(int i = 0; i < nentries; i++){
            readEntry(i);
            if(cut.isValid(this)==true){
                double result = exp.getValue(this);
                vec.add(result);
            }
            if(limit>0&&i>limit) return vec;
        }
        return vec;
    }
    /**
     * Scan a tree with expressions defined in expression string, multiple
     * expressions are separated by ":". The results are stored in internal array,
     * and can be accessed through getScanResults() method. Number of Data vectors in
     * the result is equal to number of expressions. If bothSides flag is ture
     * then the tree is parsed limit/2 events from the start of the tree and last
     * limit/2 event. This is useful to determine the limits of the variable, scanning
     * the end portion of the tree will ensure that monotonically changing variables
     * like indicies return boundaries that are correct.
     * @param expression expression list ":" separated
     * @param tcut cut expression
     * @param limit number of events to run over
     * @param bothSides flag indicating that start and end of the tree have to be sampled.
     */
    public void scanTree(String expression, String tcut, int limit, boolean bothSides){
        String[] tokens = expression.split(":");
        List<TreeExpression>  texp = new ArrayList<TreeExpression>();
        scanResults.clear();
        for(String token : tokens){
            TreeExpression exp = new TreeExpression(token,this.getListOfBranches());
            texp.add(exp);
            scanResults.add(new DataVector());
        }

        int eventsLimit = limit;

        if(limit>=getEntries()){
            eventsLimit = getEntries();
        }

        TreeCut cut = new TreeCut("cut",tcut,this.getListOfBranches());

        if(eventsLimit>0&&bothSides==true){

            int eventsLimitHalf = eventsLimit/2;
            for(int i = 0; i < eventsLimitHalf;i++){
                readEntry(i);
                if(cut.isValid(this)==true){
                    for(int k = 0; k < texp.size();k++){
                        double result = texp.get(k).getValue(this);
                        scanResults.get(k).add(result);
                    }
                }
            }

            for(int i = getEntries()-1; i > getEntries()-eventsLimitHalf; i--){
                readEntry(i);
                if(cut.isValid(this)==true){
                    for(int k = 0; k < texp.size();k++){
                        double result = texp.get(k).getValue(this);
                        scanResults.get(k).add(result);
                    }
                }
            }

        } else {
            for(int i = 0; i < getEntries();i++){
                readEntry(i);
                if(cut.isValid(this)==true){
                    for(int k = 0; k < texp.size();k++){
                        double result = texp.get(k).getValue(this);
                        scanResults.get(k).add(result);
                    }
                }
                if(i>eventsLimit) return;
            }
        }
    }
    /**
     * returns a list of data vectors stores after the last scan was done.
     * @return DataVector list
     */
    public List<DataVector> getScanResults(){
        return this.scanResults;
    }
    /**
     * check for branch name.
     * @param name name of the branch
     * @return true if branch with name exists, false otherwise
     */
    public boolean hasBranch(String name){
        return this.treeBranches.containsKey(name);
    }
    /**
     * returns a root node to be placed in the tree model
     * @return root tree node
     */
    public DefaultMutableTreeNode getRootNode() {

        DefaultMutableTreeNode root         = new DefaultMutableTreeNode(getName());
        DefaultMutableTreeNode rootbranch   = new DefaultMutableTreeNode("Branches");
        DefaultMutableTreeNode rootcuts     = new DefaultMutableTreeNode("Selector");
        root.add(rootbranch);
        root.add(rootcuts);

        List<String> branches = getListOfBranches();
        for(String item : branches){
            rootbranch.add(new DefaultMutableTreeNode(item));
        }

        Map<String,TreeCut> cuts = this.defaultSelector.getSelectorCuts();
        for(Map.Entry<String,TreeCut> entry : cuts.entrySet()){
            rootcuts.add(new DefaultMutableTreeNode(entry.getKey()));
        }
        return root;
    }

    @Override
    public int getEntries() {
        return 0;
    }

    @Override
    public int readEntry(int entry) {
        return 0;
    }

}
