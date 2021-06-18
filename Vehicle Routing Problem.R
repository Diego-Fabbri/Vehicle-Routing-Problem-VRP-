#Set your own working directory
setwd("C:/Users/diego/Documents/R/Projects/GitHub_Projects/Optimization/Vehicle Routing Problem")

# Import lpSolve package
library(lpSolve)

#Import required packages (ompr)
library(dplyr)
library(ROI)
library(ROI.plugin.symphony)
library(ompr)
library(ompr.roi)
library(ggplot2)

#Set coordinates
xPos <- c(50, 91.96597675, 4.429534833, 70.62278085, 36.3491225, 20.25312943, 65.01756495, 80.47269263, 44.25742111, 55.37454121, 90.76322566, 90.20079397, 30.9490321, 89.52208309, 81.66672711, 17, 87.27887171, 83.6627887, 96.89989373, 59.63776655, 71.83902707, 92.31240214, 73.08969445, 21.40806172, 11.83393172, 6.887087363, 43.32531433, 86.01225057, 26.93871206, 29.06518566, 7.4)

yPos <- c(50, 2.429480545, 86.08245321, 29.59853816, 35.03474281, 93.31407738, 94.51487113, 75.14517172, 8.050213559, 46.94397809, 52.4313392, 87.32686729, 69.92298919, 72.81256499, 32.13648462, 83.28247292, 57.62395476, 8.733175191, 45.53407853, 16.50682552, 45.48331256, 29.08620109, 63.56253827, 9.609263291, 34.78660679, 42.58023397, 52.04592848, 86.82712686, 91.63189677, 55.63919829, 24)

#Set city names
city_names <- c(seq(1:length(xPos)))

#Plot Cities
cities <- data.frame(id = city_names, x = xPos, y = yPos)

ggplot(cities, aes(x= xPos, y= yPos, label=city_names)) + 
  geom_point(size = 2,alpha = 0.6) +
  theme_bw()+
  geom_text(aes(label = city_names), hjust=0.6, vjust=1.5)

#Set problem size
n <- length(xPos)


#Set Cists (euclidean distances)
c <- array(dim = c(n, n))

for (i in 1:n) {
  for (j in 1:n) {
    c[i, j] <- sqrt((xPos[j] - xPos[i])^ 2 + (yPos[j] - yPos[i])^ 2)
  }
}

#Set vehicle capacity
C <- 90

#Set demands
d <- c(0,17,4,5,10,2,13,19,17,5,12,7,14,7,9,10,17,20,18,14,8,20,12,8,19,17,8,13,17,6,16)
total_demand <- sum(d)
total_demand

#Build Model
Model <- MIPModel() %>%
  add_variable(x[i,j], i = 1:n, j = 1:n, type = "binary") %>% #define variables
  add_variable(f[i,j], i = 1:n, j = 1:n, type = "continuous", lb = 0)%>%
  set_objective(sum_expr(c[i, j] * x[i, j], i = 1:n, j = 1:n), "min") %>% #define objective
  add_constraint(sum_expr(x[i, j], j = 1:n) == 1, i = 2:n) %>% #define constraints
  add_constraint(sum_expr(x[j, i], j = 1:n) == 1, i = 2:n) %>%
  add_constraint(sum_expr(f[j, i] - f[i, j], j = 1:n) == d[i], i = 2:n) %>%
  add_constraint(f[i, j] <= C*x[i, j], i = 1:n, j = 1:n) %>%
  solve_model(with_ROI(solver = "symphony", verbosity = 1))

#Variables
for (a in 1:n) {
  for (b in 1:n) {
    tmp_x <- get_solution(Model, x[i, j]) %>%
      filter(variable == "x", i == a, j == b) %>%
      select(value)
    
    
    if (tmp_x != 0) {
      print(paste("--->x[", a, ",", b , "] =", tmp_x))
    }
  }
}

for (a in 1:n) {
  for (b in 1:n) {
    tmp_f <- get_solution(Model, f[i, j]) %>%
      filter(variable == "f", i == a, j == b) %>%
      select(value)
    
    
    if (tmp_f != 0) {
      print(paste("--->f[", a, ",", b , "] =", tmp_f))
    }
  }
}
