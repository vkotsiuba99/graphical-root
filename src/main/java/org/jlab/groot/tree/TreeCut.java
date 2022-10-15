package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class TreeCut {

    String  cutName = "";
    String  cutExpression = "";
    List<String> cutVariables = new ArrayList<String>();
    Expression expr = null;
    private boolean  isCutActive = true;

    public ArrayList<String> getBranches(){
        return (ArrayList<String>) cutVariables;
    }

    Operator operatorGT = new Operator(">", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
        @Override
        public double apply(final double... args) {
            if(args[0]>args[1]) return 1.0;
            return 0.0;
        }
    };

    Operator operatorLT = new Operator("<", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
        @Override
        public double apply(final double... args) {
            if(args[0]<args[1]) return 1.0;
            return 0.0;
        }
    };

    Operator operatorEQ = new Operator("==", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
        @Override
        public double apply(final double... args) {
            if(args[0]==args[1]) return 1.0;
            return 0.0;
        }
    };

    Operator operatorAND = new Operator("&&", 2, true, Operator.PRECEDENCE_ADDITION) {
        @Override
        public double apply(final double... args) {
            if(args[0]>0.0&&args[1]>0.0) return 1.0;
            return 0.0;
        }
    };

    Operator operatorOR = new Operator("||", 2, true, Operator.PRECEDENCE_ADDITION) {
        @Override
        public double apply(final double... args) {
            if(args[0]>0.0||args[1]>0.0) return 1.0;
            return 0.0;
        }
    };

    /*
    public DataCut(String name, String exp){
        cutName = name;
        cutExpression = exp;
    }*/

    public TreeCut(String name, String exp, List<String> branches){
        cutName       = name;
        cutExpression = exp;
        cutVariables.clear();
        for(String br : branches){
            cutVariables.add(br);
        }
        init();
    }

    public boolean isActive(){
        return this.isCutActive;
    }

    public void setActive(boolean activeFlag){
        this.isCutActive = activeFlag;
    }

    public String getName(){
        return cutName;
    }

    public String getExpression(){
        return cutExpression;
    }

    public void setExpression(String expression){
        this.cutExpression = expression;
        init();
    }

    public void setName(String name){
        this.cutName = name;
    }

    final void init(){
        String[] variables = new String[cutVariables.size()];
        for(int i=0; i < variables.length; i++) variables[i] = cutVariables.get(i);

        ExpressionBuilder builder = new ExpressionBuilder(cutExpression)
                .operator(operatorAND)
                .operator(operatorOR)
                .operator(operatorGT)
                .operator(operatorLT)
                .operator(operatorEQ);
        builder.variables(variables);
        expr = builder.build();
    }

    public boolean isValid(ITree tree){
        for(int i = 0; i < cutVariables.size(); i++){
            expr.setVariable(cutVariables.get(i),
                    tree.getBranch(cutVariables.get(i)).getValue().doubleValue());
        }
        double result = expr.evaluate();
        if(result>0.0) return true;
        return false;
    }
}
