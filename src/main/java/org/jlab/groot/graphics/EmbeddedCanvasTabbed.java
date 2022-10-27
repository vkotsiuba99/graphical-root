package org.jlab.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jlab.groot.tree.Tree;

public class EmbeddedCanvasTabbed extends JPanel implements ActionListener {

    private JTabbedPane   tabbedPane = null;
    private JPanel       actionPanel = null;
    private int          canvasOrder = 1;
    private int iconSizeX = 35;
    private int iconSizeY = 35;
    private Map<String,EmbeddedCanvas>  tabbedCanvases = new LinkedHashMap<String,EmbeddedCanvas>();

    private int isDynamic = 0;

    public EmbeddedCanvasTabbed(){
        super();
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(400, 500);
        initUI();
        initBottomBar();
        addCanvas();
    }

    public EmbeddedCanvasTabbed(String... canvases){
        super();
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(400, 500);
        this.initUI();
        for(String canvas : canvases){
            addCanvas(canvas);
        }
    }

    private void initUI(){
        this.tabbedPane = new JTabbedPane();
        this.add(tabbedPane,BorderLayout.CENTER);
    }

    private void initBottomBar(){
        actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        this.add(actionPanel,BorderLayout.PAGE_END);
        ImageIcon newTabIcon = new ImageIcon();
        ImageIcon removeTabIcon = new ImageIcon();
        ImageIcon editTabIcon = new ImageIcon();
        ImageIcon clearTabIcon = new ImageIcon();
        try {
            Image addImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/canvas_add.png"));
            Image clearImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/1477453861_star_full.png"));
            Image deleteImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/canvas_delete.png"));
            Image editImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/1477454132_calendar.png"));
            newTabIcon.setImage(addImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
            clearTabIcon.setImage(clearImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
            editTabIcon.setImage(editImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
            removeTabIcon.setImage(deleteImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JButton buttonAdd = new JButton(newTabIcon);
        buttonAdd.setActionCommand("add canvas");
        buttonAdd.addActionListener(this);

        JButton buttonRemove = new JButton(removeTabIcon);
        buttonRemove.setActionCommand("remove canvas");
        buttonRemove.addActionListener(this);

        JButton buttonDivide = new JButton(editTabIcon);
        buttonDivide.setActionCommand("divide");
        buttonDivide.addActionListener(this);

        JButton buttonClear = new JButton(clearTabIcon);
        buttonClear.setActionCommand("clear");
        buttonClear.addActionListener(this);

        actionPanel.add(buttonAdd);
        actionPanel.add(buttonRemove);
        actionPanel.add(buttonDivide);
        actionPanel.add(buttonClear);
    }

    public EmbeddedCanvas getCanvas(){
        int    index = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(index);
        return this.tabbedCanvases.get(title);
    }

    public EmbeddedCanvas getCanvas(String title){
        return this.tabbedCanvases.get(title);
    }

    public final void addCanvas(){
        String name = "canvas" + canvasOrder;
        canvasOrder++;
        addCanvas(name);
    }

    public final void addCanvas(String name){
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        this.tabbedCanvases.put(name, canvas);
        tabbedPane.addTab(name, canvas);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().compareTo("add canvas")==0){
            addCanvas();
        }

        if(e.getActionCommand().compareTo("remove canvas")==0){
            //addCanvas();
            if(JOptionPane.showConfirmDialog(this,
                    "Are you sure to remove current canvas ?", "Really Removing?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                int    index = tabbedPane.getSelectedIndex();
                String title = tabbedPane.getTitleAt(index);
                tabbedPane.remove(index);
                this.tabbedCanvases.remove(title);
            }
        }

        if(e.getActionCommand().compareTo("divide")==0){
            String[] options = new String[]{"1","2","3","4","5","6","7"};
            JComboBox columns = new JComboBox(options);
            JComboBox    rows = new JComboBox(options);
            Object[] message = {
                    "Columns:", columns,
                    "Rows:", rows
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Divide Canvas", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String stringCOLS = (String) columns.getSelectedItem();
                String stringROWS = (String) rows.getSelectedItem();
                this.getCanvas().divide(Integer.parseInt(stringCOLS), Integer.parseInt(stringROWS));
                //System.out.println("----> Splitting " + columns.getSelectedItem() + " " + rows.getSelectedItem());
            }
        }

        if(e.getActionCommand().compareTo("clear")==0){
            getCanvas().clear();
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        //EmbeddedCanvasTabbed canvasTab = new EmbeddedCanvasTabbed("TDC","ADC","VALUES");
        EmbeddedCanvasTabbed canvasTab = new EmbeddedCanvasTabbed();
        frame.add(canvasTab);
        frame.pack();
        frame.setMinimumSize(new Dimension(300,300));
        frame.setVisible(true);
    }

}
