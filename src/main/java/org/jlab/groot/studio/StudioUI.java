package org.jlab.groot.studio;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jlab.groot.tree.RandomTree;
import org.jlab.groot.tree.Tree;
import org.jlab.groot.ui.EmbeddedCanvas;

public class StudioUI implements MouseListener {

    JSplitPane  splitPane = null;
    JPanel      navigationPane = null;
    EmbeddedCanvas drawCanvas = null;
    JFrame  frame = null;
    Tree    studioTree = null;
    JTree jtree = null;

    public StudioUI(Tree tree){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studioTree = tree;
        initUI();
        frame.pack();
        frame.setVisible(true);
    }

    private void initUI(){
        splitPane = new JSplitPane();
        navigationPane = new JPanel();
        navigationPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel canvasPane = new JPanel();
        canvasPane.setLayout(new BorderLayout());
        canvasPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //canvasPane.setBorder(new EmptyBorder(5,5,5,5));
        drawCanvas     = new EmbeddedCanvas(500,500);
        canvasPane.add(drawCanvas,BorderLayout.CENTER);


        splitPane.setLeftComponent(navigationPane);
        splitPane.setRightComponent(canvasPane);


        DefaultMutableTreeNode top =
                studioTree.getTree();

        jtree = new JTree(top);
        JScrollPane treeView = new JScrollPane(jtree);
        navigationPane.setBorder(new EmptyBorder(5,5,5,5));
        navigationPane.setLayout(new BorderLayout());
        navigationPane.add(treeView,BorderLayout.CENTER);
        splitPane.setDividerLocation(0.5);
        frame.add(splitPane);
    }

    public static void main(String[] args){
        StudioUI sui = new StudioUI(new RandomTree());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                System.out.println(path.getLastPathComponent().toString());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
