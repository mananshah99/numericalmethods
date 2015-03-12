/**
 * NewtonRhapson.java is a class that contains one publicly accessible method (findRoot()) 
 *  and a main method to test it. 
 * 
 * findRoot(double iterations, double delta, double tolerance, double startPoint): 
 *  uses the Newton-Rhapson method in order to find one root of a function specified
 *  in the class as public static double f(double x). Parameters that are passed
 *  to this method (specified more in-detail in the method comments) are as follows:
 *      double iterations: the number of iterations the method should perform to
 *          find the root. 
 *      double delta: the change in x value for each iteration performed. If 
 *          delta is smaller than the Machine Epsilon, the x value will never change 
 *          and the program will not obtain a correct result.
 *      double tolerance: the amount of tolerance allowed to determine if f(x)/f'(x) 
 *          is close enough to zero to be counted as an acceptable root. 
 *      double startPoint: the x-coordinate of the point from which to start finding
 *          the root. The program searches to the left of startPoint if startPoint
 *          is positive and to the right if startPoint is negative for the given number
 *          of iterations. 
 *  The Newton-Rhapson method is described in more detail in the method comments. 
 * 
 * findRoot() special behavior is defined below: 
 * 
 *  findRoot() returns NaN under two conditions: 
 *    1) if there are no zeroes at all in f(x) within the range specified
 *    2) if there are no real roots in f(x) within the range specified.
 *  
 *  Note that NaN maybe returned if the root(s) of f(x) exist but lie outside
 *    of the range specified in the parameters of the method. The method only searches
 *    for roots within the input range, so any extraneous roots are ignored
 * 
 * 
 * main(String[] args) contains one single call to the findRoot function with 
 *      parameters specified that determines the root of the defined function f(x) in the 
 *      NewtonRhapson class. The method then prints the root of the function (if it exists)
 *      or NaN in order to compare the found root with the correct root. 
 * 
 * Compile instructions:
 *  javac NewtonRhapson.java ThreePointSum.java
 *  java NewtonRhapson
 * 
 * Required files:
 *  ThreePointSum.java from Derivative assignment. This file includes the Point
 *  class as well as the threePointDerivative function that is required for 
 *  computing f(x)/f'(x).
 * 
 * Author: Manan Shah
 * Date of Creation: 9/24/14
 */
public class NewtonRhapson 
{
    /**
     * The findRoot() method uses the Newton-Rhapson technique to determine the closest
     *  root to the left of START_POINT (if START_POINT is positive) or the 
     *  right of START_POINT (if START_POINT is negative) based on the above
     *  constants. 
     * 
     * Newton-Rhapson Method description: 
     *  Given a function f(x), the method begins with an initial guess (START_POINT or x_0) 
     *  for a root of the function. A better approximation (x_1) is then provided by the formula
     *  x_1 = x_0 - (f(x_0)/f'(x_0)). The process is repeated based on the relation 
     *          x_(n+1) = x_n - (f(x_n)/f'(x_n))
     *  until a "sufficiently accurate" value (determined by TOLERANCE) is reached. 
     * 
     * @param iterations determines how many iterations the program runs to 
     *  find the zero of the function specified.
     * 
     * @param delta determines the x shift between points used to find the root 
     *  of the equation. The program starts with the initial x value startPoint
     *  and tests values in the appropriate direction (towards zero, shifting by delta).
     * 
     * @param tolerance determines when the program has found a "sufficiently close" 
     *  value for the root of the function f(x). If |f(x)/f'(x)| < 0, 
     *  the method returns the result. 
     * 
     * @param startPoint is the x-coordinate of the point from which to start finding
     *  the root. 
     * 
     * @return the x-coordinate of the root of the function f(x) or NaN if no root
     *  is found within the specified range. 
     */
    public static double findRoot(double iterations, double delta, double tolerance, double startPoint) 
    {    
        /*
         * Variables as described by the relation in the block 
         *  header comment above. x_0 is initially set as START_POINT but
         *  is altered as the method progresses. 
         */
        double x_1;
        double x_0 = startPoint;
                
        // keeps track of the number of iterations 
        for(int i = 0; i < iterations; i++)
        {
            // we need three points in order for the threePointDerivative function to work
            double prev = f(x_0 - delta);
            double curr = f(x_0);
            double next = f(x_0 + delta);
            
            // calculate the derivative of the function f(x) based on the three points above
            double derivative = ThreePointSum.threePointDerivative(
                                                        new Point(x_0 - delta, prev), 
                                                        new Point(x_0, curr), 
                                                        new Point(x_0 + delta, next), 
                                                        x_0
                                                    );
            
            // calculate f(x) / f'(x) 
            double dx = (double) curr / (double) derivative;
            
            /*
             * If one of the three outlined conditions -    
             * 
             *  |dx| < TOLERANCE: dx is close enough to zero such that we can 
             *      stop the loop and return the root.
             *  dx is +/- Infinity: the derivative doesn't exist; we can break
             *      and exit the loop to avoid propogating Infinity
             *  dx is NaN: the derivative is 0 so we are at the precise zero
             *      we would like to return.
             * 
             * is met, the program exits and the estimated root or NaN is
             *  returned. 
             */
            if(Math.abs(dx) < tolerance || Double.isInfinite(dx) || Double.isNaN(dx)) 
            {
                // Case 1: f(x)/f'(x) is infinite; we can't find a root.
                if(Double.isInfinite(dx))
                {
                    return Double.NaN;
                }
                
                // Cases 2 & 3: f(x)/f'(x) is either 0 or less than TOLERANCE; we have found a root
                else 
                {
                    return x_0;
                }
                
            } // if(Math.abs(dx) < tolerance || Double.isInfinite(dx) || Double.isNaN(dx)) 
            
            // Alter x_1 and x_0 for the next iteration. 
            x_1 = x_0;
            x_0 = x_0 - dx;
            
        } //for(int i = 0; i < iterations; i++)
        
        return Double.NaN;
        
    } // public static double findRoot(double iterations, double delta, double tolerance, double startPoint) 
        
    /**
     * main(String[] args) contains one call to the findRoot() function
     *  with parameters specified in order to determine the closest root 
     *  to the left of 10 of the function f(x) defined below. 
     * 
     * @param args the command line arguments (not required for this program)
     */
    public static void main(String[] args) 
    {   
        System.out.println(findRoot(1e8, 1e-7, 1e-10, 10));
        return;
    }

    /**
     * This method defines the function that is used in the Newton-Rhapson
     *  calculations. The roots of the function expressed below are calculated
     *  and returned by findRoot().
     * 
     * @param x the variable (input) of the function f(x). 
     */
    public static double f(double x) 
    {
        return 5*Math.pow(x, 4) - 3*Math.pow(x, 3) + 4*Math.pow(x, 2) + x - 2;
        // => Mathemtica Solution: Select[NSolve[-2 + x + 4*x^2 - 3*x^3 + 5*x^4 == 0, x], Element[x /. #1, Reals] & ]
    }
    
} // public class NewtonRhapson