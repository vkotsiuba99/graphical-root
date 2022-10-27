package org.jlab.groot.tree;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.graphics.EmbeddedCanvas;

public interface TreeProvider {

    Tree        tree();
    TreeModel   getTreeModel();
    List<DataVector>        actionTreeNode(TreePath[] path, int limit);
    void        setSource(String filename);
    JDialog     treeConfigure();

}
