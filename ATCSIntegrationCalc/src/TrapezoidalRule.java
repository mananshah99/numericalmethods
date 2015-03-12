/*************************************************************************
 *  Compilation:  javac TrapezoidalRule.java
 *  Execution:    java TrapezoidalRule a b
 *  
 *  Numerically integrate the function in the interval [a, b].
 *
 *  % java TrapezoidalRule -3 3
 *  0.9973002031388447                 // true answer = 0.9973002040...
 *
 *  Observation: this says that 99.7% of time a standard normal random
 *  variable is within 3 standard deviation of its mean.
 *
 *  %  java TrapezoidalRule 0 100000
 *  1.9949108930964732                 // true answer = 1/2
 *
 *  Caveat: this is not the best way to integrate the normal density
 *  function. See what happens if you make b very big.
 *
 *************************************************************************/


public class TrapezoidalRule {
    
   static double f(double x) {
      return x*x;
   }

   static double integrate(double a, double b, int N) {
      double h = (b - a) / N;              // step size
      double sum = 0.5 * (f(a) + f(b));    // area
      for (int i = 1; i < N; i++) {
         double x = a + h * i;
         sum = sum + f(x);
      } 
      return sum * h;
   }

   public static void main(String[] args) { 
      double a = 0;
      double b = 10000;
      System.out.println(integrate(a, b, 1000));
   }

}