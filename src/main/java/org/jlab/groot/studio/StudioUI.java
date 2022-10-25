package org.jlab.groot.studio;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.tree.RandomTree;
import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeAnalyzer;
import org.jlab.groot.tree.TreeFile;
import org.jlab.groot.ui.CutPanel;
import org.jlab.groot.ui.DescriptorPanel;
import org.jlab.groot.ui.TreeEditor;
import org.jlab.groot.graphics.HistogramPlotter;
import org.jlab.groot.tree.TreeTextFile;

public class StudioUI implements MouseListener,ActionListener {

    JSplitPane  splitPane = null;
    JPanel      navigationPane = null;
    EmbeddedCanvas drawCanvas = null;
    JFrame  frame = null;
    Tree    studioTree = null;

    JTree   jtree = null;
    JTree   jtreeAnalyzer = null;

    JPanel  studioPane = null;
    JPanel  statusPane = null;
    JMenuBar menuBar = null;
    StudioToolBar  toolBar = null;
    TreeAnalyzer  analyzer = new TreeAnalyzer();

    public StudioUI(Tree tree){
        frame = new JFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studioTree = tree;
        frame.setSize(800, 800);
        frame.setMinimumSize(new Dimension(300,300));
        initUI();

        frame.pack();
        frame.setSize(900, 700);
        splitPane.setDividerLocation(0.4);
        frame.setVisible(true);
    }

    private void initUI(){

        studioPane = new JPanel();
        studioPane.setLayout(new BorderLayout());

        initMenu();


        splitPane = new JSplitPane();
        navigationPane = new JPanel();
        navigationPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel canvasPane = new JPanel();
        canvasPane.setLayout(new BorderLayout());
        canvasPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //canvasPane.setBorder(new EmptyBorder(5,5,5,5));
        drawCanvas     = new EmbeddedCanvas();//500,500,2,2);
        canvasPane.add(drawCanvas,BorderLayout.CENTER);


        //splitPane.setLeftComponent(navigationPane);
        splitPane.setRightComponent(canvasPane);


        DefaultMutableTreeNode top =
                studioTree.getTree();

        jtree = new JTree(top);
        jtree.addMouseListener(this);
        JScrollPane treeView = new JScrollPane(jtree);

        DefaultMutableTreeNode topa =
                analyzer.getTree();
        jtreeAnalyzer = new JTree(topa);
        JScrollPane treeViewAnalyzer = new JScrollPane(jtreeAnalyzer);
        treeViewAnalyzer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JSplitPane splitPaneNavigation = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneNavigation.setTopComponent(jtree);
        splitPaneNavigation.setBottomComponent(jtreeAnalyzer);
        splitPane.setLeftComponent(splitPaneNavigation);
//JSplitPane treeSplit =

        navigationPane.setBorder(new EmptyBorder(5,5,5,5));
        navigationPane.setLayout(new BorderLayout());
        navigationPane.add(treeView,BorderLayout.CENTER);
        splitPane.setDividerLocation(0.5);
        studioPane.add(splitPane,BorderLayout.CENTER);
        frame.add(studioPane);
    }

    private void initMenu(){

        statusPane = new JPanel();

        statusPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;

        //statusPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JPanel statusPane1 = new JPanel();
        JPanel statusPane2 = new JPanel();
        JPanel statusPane3 = new JPanel();
        statusPane1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        statusPane2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        statusPane3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        statusPane1.add(new JLabel("Status:"));
        statusPane2.add(new JLabel("Processed:"));
        statusPane3.add(new JLabel("Memory:"));
        statusPane.add(statusPane1,c);
        statusPane.add(statusPane2,c);
        statusPane.add(statusPane3,c);

        studioPane.add(statusPane,BorderLayout.PAGE_END);

        //JToolBar toolBar = new JToolBar("");
        //toolBar.add(new Button("Divide"));
        //toolBar.add(new Button("Add"));
        toolBar = new StudioToolBar(this);

        menuBar = new JMenuBar();
        JMenu  menuFile = new JMenu("File");
        JMenuItem menuFileOpen = new JMenuItem("Open ASCII File...");
        JMenuItem menuFileOpenHipo = new JMenuItem("Open HIPO File...");
        JMenuItem newHistogram = new JMenuItem("New Histogram...");
        JMenuItem newHistogram2D = new JMenuItem("New 2D Histogram...");
        JMenuItem newGraphErrors = new JMenuItem("New GraphErrors...");
        JMenuItem exit = new JMenuItem("Exit");

        menuFileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String file = chooseFile("Select ASCII File to Open",true);
                openASCIIFile(file);
            }
        });

        menuFileOpenHipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String file = chooseFile("Select HIPO File to Open",true);
                openHipoFile(file);
            }
        });

        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        newHistogram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addDescriptor(1);
            }
        });

        newHistogram2D.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addDescriptor(2);
            }
        });

        newGraphErrors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createNewGraphErrors();
            }
        });

        menuFile.add(menuFileOpen);
        menuFile.add(menuFileOpenHipo);
        menuFile.add(new JSeparator());
        menuFile.add(newHistogram);
        menuFile.add(newHistogram2D);
        menuFile.add(newGraphErrors);
        menuFile.add(new JSeparator());
        menuFile.add(exit);

        menuBar.add(menuFile);
        studioPane.add(toolBar.getToolBar(),BorderLayout.PAGE_START);
        frame.setJMenuBar(menuBar);
    }
    public void openHipoFile(String file) {
        System.out.println("Open new hipo File:"+file);
        TreeFile tree = new TreeFile("T");
        tree.openFile(file);
        StudioUI sui = new StudioUI(tree);
    }
    public void openASCIIFile(String file) {
        System.out.println("Open new ASCII File:"+file);
        TreeTextFile tree = new TreeTextFile("T");
        tree.readFile(file);
        StudioUI sui = new StudioUI(tree);
    }
    public void createNewGraphErrors() {
        System.out.println("Create new graph errors");
    }
    public void createNewHistogram() {
        System.out.println("Create new Histogram");

    }
    public void createNewHistogram2D() {
        System.out.println("Create new Histogram 2D");

    }
    public String chooseFile(String name, boolean open) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(chooser.getCurrentDirectory());
        chooser.setDialogTitle(name);
        chooser.setAcceptAllFileFilterUsed(true);

        if (open) {
            if (chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile().toString();
            } else {
                return null;
            }
        } else {
            if (chooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile().toString();
            } else {
                return null;
            }
        }
    }

    public void scanTreeItem(String item){
        if(this.studioTree.hasBranch(item)==true){
            //List<Double> vector = studioTree.getVector(item,studioTree.getSelector());
            System.out.println("getting vector for item = " + item);
            DataVector vec = studioTree.getDataVector(item, "aa>0");
            System.out.println("result = " + vec.getSize());
            H1F  h1d = H1F.create(item, 100, vec);
            h1d.setTitle(item);
            h1d.setTitleX(item);
            h1d.setOptStat(11111);
            h1d.setLineColor(1);
            h1d.setFillColor(43);
            drawCanvas.draw(h1d);
            //this.drawCanvas.drawNext(h1d);
            //this.drawCanvas.getPad(0).addPlotter(new HistogramPlotter(h1d));
            this.drawCanvas.update();
        }
    }

    public boolean isTree(String item){
        return this.studioTree.getName()==item;
    }

    public void addCut(){
        System.out.println("doing some stuff...");
        CutPanel cutPane = new CutPanel(studioTree);
        JFrame frame = new JFrame("Cut Editor");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cutPane);
        frame.pack();
        frame.setLocationRelativeTo(this.frame);
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    public void updateTree(){
        DefaultTreeModel model = new DefaultTreeModel(studioTree.getTree());
        this.jtree.setModel(model);
        DefaultTreeModel modelAnalyzer = new DefaultTreeModel(this.analyzer.getTree());
        this.jtreeAnalyzer.setModel(modelAnalyzer);
    }

    public static void main(String[] args){
        TreeTextFile tree = new TreeTextFile("T");
        //tree.readFile("/Users/vkotsiuba99/Desktop/pp_10k.txt");
        tree.readFile("/Users/vkotsiuba99/Desktop/GROOTTree/pp_10k_wlab.txt");
        //StudioUI sui = new StudioUI(new RandomTree());
        StudioUI sui = new StudioUI(tree);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {
            TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                System.out.println(path.getLastPathComponent().toString());
                scanTreeItem(path.getLastPathComponent().toString());
                String cutString = path.getLastPathComponent().toString();
                if(isTree(path.getLastPathComponent().toString())){
                    JFrame editorFrame = new JFrame("Tree Editor:"+path.getLastPathComponent().toString());
                    TreeEditor editor = new TreeEditor(this.studioTree);
                    editorFrame.add(editor);
                    editorFrame.pack();
                    editorFrame.setLocationRelativeTo(this.frame);
                    editorFrame.setVisible(true);
                }
                //this.updateTree();

                /*if(path.getLastPathComponent() instanceof Tree){
                	//path.getLastPathComponent());
                }*/

                //if(cutString.contains("Selector")==true){
                //    addCut();
                //}
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //this.updateTree();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //this.updateTree();

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void addDescriptor(int dim){
        // panel = new DescriptorPanel(studioTree,analyzer,2);
        JFrame frame = new JFrame("Edit Histogram");
        DescriptorPanel panel = new DescriptorPanel(studioTree,analyzer,dim);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(this.frame);
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action appeared = " + e.getActionCommand());
        if(e.getActionCommand().compareTo("Add Descriptor")==0){
            this.addDescriptor(1);
        }
        if(e.getActionCommand().compareTo("Add Cut")==0){
            this.addCut();
        }
    }
}
