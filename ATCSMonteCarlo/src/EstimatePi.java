/**
 * EstimatePi is a class that includes the following methods:
 * 
 *  main(String[] args): runs a simple accuracy test on both methods of the Monte
 *   Carlo simulation (yes and no line delineation) to determine how close the calculated
 *   value of pi is to the true value (Math.PI). This method runs both the lineDelineation()
 *   and noLineDelineation() methods ITER_TO_ESTIMATE times and prints the error percentages
 *   for each method.
 * 
 *  lineDelineation(): calculates and returns the value of pi, placing points that land on the center
 *   of the circle either inside or outside of the circle with a 50 percent chance of either
 *   result. By flipping a boolean switch every time a point is determined to be on the 
 *   circumference of the estimating circle, noLineDelineation attempts to achieve a more
 *   uniform distribution of points both inside and outside the circle.
 * 
 *  noLineDelineation(): calculates and returns the value of pi in the same way as lineDelineation(), but
 *   instead always places points that land on the circumference of the circle inside the circle.
 * 
 * Random Number Generator Characterization: the Java built-in random number generator (Math.random())
 *  provides a uniformly distributed set of values from -1 to 1. By graphing around 100,000
 *  pesudorandom values on a simple Excel plot, it was determined that there was very little variance
 *  and the distribution looked uniform as opposed to the Gaussian normal distribution. Thus, no 
 *  "zooming in" would be required to obtain a useful result. The Java documentation
 *  also specifies the uniformity of the generator and the types of values that it produces, 
 *  stating that Math.random()
 *      "Returns a double value with a positive sign, greater than or equal to 0.0 
 *      and less than 1.0. Returned values are chosen pseudorandomly with (approximately) 
 *      uniform distribution from that range."
 * Therefore, since the values were uniformly distributed, the Java Math.random() method
 *  could be applied in this instance to the Monte Carlo simulation.
 * 
 * Brief technique overview: a 1x1 square is modeled, with a circle (of radius r=1) 
 *  inscribed in it. Random points are generated and a counter for the number of points
 *  that have landed inside of the circle is incremented each time a random point lands
 *  in the circle. The aforementioned methods (lineDelineation() and noLineDelination())
 *  provide unique ways to deal with points that land on the circumference of the circle. 
 *  After the iterations have been completed, the value of pi is calculated 
 *  (with r = radius of circle = side length of square) as
 *      (Number of Inside Points) / (Total Number of Points) 
 *       = (pi*r*r) / ((2*r)*(2*r)) 
 *       = (pi/4)
 * 
 * Author: Manan Shah
 * Date of Creation: 9/15/2014
 */
public class EstimatePi 
{
    
    /*
     * ITERATIONS delineates the number of iterations that the 
     *  program will run to determine pi in each simulation. If you increase the number 
     *  of iterations, the value of pi will become more precise, but the 
     *  program will run more slowly.
     * 
     * DEBUG is a boolean that, when true, prints the values that each function
     *  attains during every iteration (greatly slows down the program)
     * 
     * ITER_TO_ESTIMATE is a long that determines the number of iterations the main
     *  method will use to determine the error difference between both Monte Carlo
     *  methods (yes line delineation and no line delineation)
     */
    final static long ITERATIONS = 1000;
    final static boolean DEBUG = false;
    final static long ITER_TO_ESTIMATE = 1000;
    
    /**
     * main is the primary method that runs the comparison specification tests
     *  between the two Monte Carlo methods for determining the value of pi. The 
     *  methods used differ slightly in implementation; noLineDelineation() does not
     *  automatically delineate between a point being on/not on the border of a circle,
     *  whereas lineDelineation() puts a point on the border in the circle half of 
     *  the time and out of the circle the other half. 
     *  
     * A small difference is observed between the two methods, although this 
     *  difference becomes marginal as the number of iterations increases.
     * 
     * @param args the command line arguments (none needed for the purposes of this program)
     */
    public static void main(String[] args) 
    {
        
        /*
         * The aggregated pi values for the no line differentiation
         *  and line differentation methods. These values are used
         *  to determine the percent error for both implementations.
         */
        double noLine = 0.;
        double yesLine = 0.;
        
        /*
         * Iterate for the number of iterations defined above and add the values determined
         *  by noLineDelineation() and lineDelineation() to their respective
         *  total values.
         */
        for(int i = 0; i < ITER_TO_ESTIMATE; i++) 
        {
            //line is always part of the circle
            noLine += noLineDelineation(); 
            
            //line is delineated
            yesLine += lineDelineation(); 
        }
        
        /*
         * ndiff and ydiff are the respective values that are obtained for PI
         *  from the lineDelineation() and noLineDelineation() functions.
         */
        double ndiff = ((noLine - (Math.PI*ITER_TO_ESTIMATE))/(Math.PI*ITER_TO_ESTIMATE));
        double ydiff = ((yesLine - (Math.PI*ITER_TO_ESTIMATE))/(Math.PI*ITER_TO_ESTIMATE));
        
        System.out.printf("%.30f percent => no delineation\n%.30f percent => yes delineation\n", Math.abs(ndiff), Math.abs(ydiff));
    }
    
    /**
     * noLineDelineation() is a method that automatically places points that land
     *  directly on the circumference of the circle (x^2 + y^2 = 1) inside the circle.
     *  This method differs from lineDelineation() as it does not assign points on the
     *  circumference to the inside/outside count evenly, but instead places all points
     *  on the circumference in the inside of the circle.
     *   
     * @return the value of pi estimated using a Monte Carlo simulation, with the number
     *  of iterations equal to the constant defined above (ITERATIONS).
     */
    public static double noLineDelineation() 
    {
        int nTries = 0; //the number of tries (landing both inside and outside the circle)
        int inCircle = 0; //the number of points that landed inside the circle
        
        double x, y; //the x and y coordinates (random) of the point chosen
        
        for (int i = 0; i < ITERATIONS; i++) 
        {
            // (2*Math.random()-1) returns a random number from -1 to 1
            
            x = 2 * Math.random() - 1; 
            y = 2 * Math.random() - 1;
            
            //if the point lands on the line or in the circle, increment the inCircle count
            if (x * x + y * y <= 1) 
            {
                inCircle++;
            }
            
            nTries++;
        }
        
        /*
         * Prints the output if DEBUG is set to true. The output consists of two lines:
         *  an estimate of pi to 30 decimal places using the no line delineation method 
         *  as well as an error percentage from the real value (Math.PI).
         */
        if(DEBUG) 
        {
            System.out.printf("[no line boolean] pi = %.30f\n", 4. * (double) inCircle / (double) nTries);
            System.out.printf("[no line boolean] Error = %.30f\n", (((4. * (double) inCircle / (double) nTries) - Math.PI)/Math.PI));
        }
        
        //returns the estimated value of pi
        return 4. * (double) inCircle / (double) nTries;
    }
    
    /**
     * lineDelineation() is a method that places points that land directly on the 
     *  circumference of the circle (x^2 + y^2 = 1) inside the circle half of the time.
     *  This method differs from nolineDelineation() as it assigns points on the
     *  circumference to the inside/outside count evenly rather than placing all points
     *  that land on the circumference inside the circle.
     *   
     * @return the value of pi estimated using a Monte Carlo simulation, with the number
     *  of iterations equal to the constant defined above (ITERATIONS).
     */
    public static double lineDelineation() 
    {
        int nTries = 0; //the number of tries (landing both inside and outside the circle)
        int inCircle = 0; //the number of points that landed inside the circle
        boolean lineBool = false; //flipped every time a point lands on the circumference
        
        double x, y; //the x and y coordinates (random) of the point chosen

        for (int i = 0; i < ITERATIONS; i++) 
        {
            // (2*Math.random()-1) returns a random number from -1 to 1
            
            x = 2 * Math.random() - 1; 
            y = 2 * Math.random() - 1;
            
            // if the point is clearly within the circle, increment the inCircle counts
            if (x * x + y * y < 1) 
            {
                inCircle++;
            }
            
            /*
             * If the point is on the circumference of the circle, check the line
             * boolean value. If the line boolean value is set to true, increment
             * the inCircle count and set the boolean value to false. If the line
             * boolean value is set to false, do not increment the inCircle count, but
             * flip the boolean for the next point that lands on the circumference.
             */
            else if (x * x + y * y == 1) 
            {
                if(lineBool) 
                {
                    inCircle++;
                }
                lineBool = !lineBool;
            }
            
            nTries++;
        }

        /*
         * Prints the output if DEBUG is set to true. The output consists of two lines:
         *  an estimate of pi to 30 decimal places using th line delineation method 
         *  as well as an error percentage from the real value (Math.PI).
         */
        if(DEBUG) 
        {
            System.out.printf("[yes line boolean] pi = %.30f\n", 4. * (double) inCircle / (double) nTries);
            System.out.printf("[yes line boolean] Error = %.30f\n", (((4. * (double) inCircle / (double) nTries) - Math.PI)/Math.PI));
        }
        
        //returns the estimated value of pi
        return 4. * (double) inCircle / (double) nTries;
    }
}