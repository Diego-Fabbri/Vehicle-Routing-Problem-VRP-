package com.mycompany.vehicle_routing_problem;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.cplex.IloCplex;

public class VRP_Model {

    protected IloCplex model;

    protected double capacity;

    protected double[][] distances;
    protected double[] demand;

    protected IloIntVar[][] x;//  matrix of variables x_ij
    protected IloNumVar[][] f;//  matrix of variables f_ij
    protected int size;

    VRP_Model(double[][] distances, double capacity, double[] demand) throws IloException {
        this.capacity = capacity;
        this.distances = distances;
        this.demand = demand;
        this.model = new IloCplex();
        this.x = new IloIntVar[distances.length][distances[0].length];
        this.f = new IloNumVar[distances.length][distances[0].length];
        this.size = distances.length;
        
    }

    protected void addVariables() throws IloException {
        for (int i = 0; i < size; i++) {
            int pos_i = i + 1;
            for (int j = 0; j < size; j++) {
                int pos_j = j + 1;
                x[i][j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "x[" + pos_i + "][" + pos_j + "]");
                f[i][j] = (IloNumVar) model.numVar(0, Float.MAX_VALUE, IloNumVarType.Float, "f[" + pos_i + "][" + pos_j + "]");

            }
        }

    }

    //The following code creates the objective function for the problem
    protected void addObjective() throws IloException {
        IloLinearNumExpr objective = model.linearNumExpr();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
               
                    objective.addTerm(x[i][j], distances[i][j]);
                

            }
        }

        IloObjective Obj = model.addObjective(IloObjectiveSense.Minimize, objective);
    }

    //The following code creates the constraints for the problem.
    protected void addConstraints() throws IloException {
        // Constraint (2)
        for (int i = 1; i < size; i++) {
            IloLinearNumExpr expr_2 = model.linearNumExpr();
            for (int j = 0; j < size; j++) {

                expr_2.addTerm(x[i][j], 1);// x_ij
            }
            model.addEq(expr_2, 1);
        }
        // Constraint (3)
        for (int i = 1; i < size; i++) {
            IloLinearNumExpr expr_3 = model.linearNumExpr();
            for (int j = 0; j < size; j++) {

                expr_3.addTerm(x[j][i], 1);// x_ji
            }
            model.addEq(expr_3, 1);
        }
        // Constraint (4)
        for (int i = 1; i < size; i++) {
            IloLinearNumExpr expr_4 = model.linearNumExpr();
            for (int j = 0; j < size; j++) {

                expr_4.addTerm(f[j][i], 1);// f_ji
                expr_4.addTerm(f[i][j], -1);// -f_ij
            }
            model.addEq(expr_4, demand[i]);
        }

        // Constraint (5)
        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {
                IloLinearNumExpr expr_5 = model.linearNumExpr();
                expr_5.addTerm(f[i][j], 1);
                expr_5.addTerm(x[i][j], -capacity);
                model.addLe(expr_5, 0);

            }

        }

    }
    
    
    public void solveModel() throws IloException {
        addVariables();
        addObjective();
        addConstraints();
        model.exportModel("VRP.lp");
       // model.setParam(IloCplex.Param.MIP.Tolerances.MIPGap,0.23);// max gap of 23%
       model.setParam(IloCplex.Param.TimeLimit,600);// set a 600 seconds = 10 minutes time limit for solving
        model.solve();

        if (model.getStatus() == IloCplex.Status.Feasible
                | model.getStatus() == IloCplex.Status.Optimal) {
            System.out.println();
            System.out.println("Solution status = " + model.getStatus());
            System.out.println();
            System.out.println("Total Distance " + model.getObjValue());
             System.out.println("The variables x_{ij} ");
             System.out.println();
             for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
              if(model.getValue(x[i][j])!=0.0){ 
                  System.out.println("---->" + x[i][j].getName()+ " = "+ model.getValue(x[i][j]));
                  
            }
                
            }}
             System.out.println();
              System.out.println("The flow variables f_{ij} ");
              System.out.println();
             for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
              if(model.getValue(x[i][j])!=0.0){ 
                  System.out.println("---->" + f[i][j].getName()+ " = "+ model.getValue(f[i][j]));
                  
            }
                
            }}
              System.out.println();
              System.out.println("Balance");
              System.out.println();
             for (int i = 0; i < size; i++) {
                 System.out.println("In node "+ (i+1)+": ");
                 double In_flow=0;
                 double Out_flow=0;
            for (int j = 0; j < size; j++) {
                In_flow+= model.getValue(f[j][i]);
                Out_flow+= model.getValue(f[i][j]);
             }
              System.out.println("------> Inflow value is : "+ In_flow);  
              System.out.println("------> Outflow value is : "+ Out_flow); 
              System.out.println("------> Inflow - Outflow : "+ (In_flow-Out_flow)); 
              System.out.println("------> Demand value is : "+ demand[i]); 
            }}
           
            
         else {
            System.out.println("The problem status is: " + model.getStatus());
        }
    }

    
    
    
    
    
    
    
    
    
    
    

}
