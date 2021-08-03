package Utility;

//data taken from: https://www.youtube.com/watch?v=-DjyO0DK9Ys&feature=share

import java.text.DecimalFormat;

public class Data {

    public static double[][] distances() {
        double[] xPos
                = {50, 91.96597675, 4.429534833, 70.62278085, 36.3491225, 20.25312943, 65.01756495, 80.47269263, 44.25742111, 55.37454121, 90.76322566, 90.20079397, 30.9490321, 89.52208309, 81.66672711, 17, 87.27887171, 83.6627887, 96.89989373, 59.63776655, 71.83902707, 92.31240214, 73.08969445, 21.40806172, 11.83393172, 6.887087363, 43.32531433, 86.01225057, 26.93871206, 29.06518566, 7.4
                };
        double[] yPos
                = {50, 2.429480545, 86.08245321, 29.59853816, 35.03474281, 93.31407738, 94.51487113, 75.14517172, 8.050213559, 46.94397809, 52.4313392, 87.32686729, 69.92298919, 72.81256499, 32.13648462, 83.28247292, 57.62395476, 8.733175191, 45.53407853, 16.50682552, 45.48331256, 29.08620109, 63.56253827, 9.609263291, 34.78660679, 42.58023397, 52.04592848, 86.82712686, 91.63189677, 55.63919829, 24

                };

        double[][] distances = new double[xPos.length][xPos.length];

        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = Math.sqrt(Math.pow(xPos[j] - xPos[i], 2) + Math.pow(yPos[j] - yPos[i], 2));
                
            }
        }

        return distances;
    }
 public static double capacity() {
 
 return 90.00;
 
 }
  public static double[] demand() {
   double[] demand= {0,17,4,5,10,2,13,19,17,5,12,7,14,7,9,10,17,20,18,14,8,20,12,8,19,17,8,13,17,6,16};
   return demand;
  
  
  
  }
    public static double Total_demand(double[] d) {
   double total_demand=0;
   for (int i = 0; i < d.length; i++) {
   total_demand+=d[i];
   
   }
   
   
   return total_demand;
  
  
  
  }
 
 
 
    public static void printMatrix(double[][] x) {
        System.out.println("Matrix of costs c_{ij}:");
        System.out.println();
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
               // System.out.print("\t" + x[i][j]);
               DecimalFormat df = new DecimalFormat("0.##");
               System.out.print(df.format(x[i][j]) + " ");
            }
            System.out.println();
        }
    }

}
