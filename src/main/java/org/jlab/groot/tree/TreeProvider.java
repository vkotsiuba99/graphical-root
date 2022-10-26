package org.jlab.groot.tree;

import javax.swing.JDialog;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jlab.groot.graphics.EmbeddedCanvas;

public interface TreeProvider {

    TreeModel   getTreeModel();
    void        actionTreeNode(TreePath path, EmbeddedCanvas canvas, int limit);
    void        setSource(String filename);
    JDialog     treeConfigure();

}
