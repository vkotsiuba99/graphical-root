package org.jlab.groot.tree;

import java.util.List;

public interface ITree {
    String        getName();
    List<String>  getListOfBranches();
    Branch        getBranch(String name);
    int           getEntries();
    int           readEntry(int entry);
    void          reset();
    boolean       readNext();
    void          configure();
}
