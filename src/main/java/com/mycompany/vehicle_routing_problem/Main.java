
package com.mycompany.vehicle_routing_problem;



import Utility.Data;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class Main {
     public static void main(String[] args) throws FileNotFoundException, IloException{
        System.setOut(new PrintStream("VRP.log"));
        double [][] distances = Data.distances();
        Data.printMatrix(distances);
        double capacity = Data.capacity();
        double[] demand = Data.demand();
        double total_demand= Data.Total_demand(demand);
        System.out.println();
        System.out.println("Total demans value is : " + total_demand);
        System.out.println();
        int minimum_number_of_vehicles = (int) Math.ceil((double)total_demand/capacity);
        System.out.println("Continuous minimum number of required vehicles : " + minimum_number_of_vehicles);
        System.out.println();
       VRP_Model model = new VRP_Model(distances,capacity,demand);
      
  model.solveModel();
    
}
}