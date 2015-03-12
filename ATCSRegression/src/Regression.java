import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Regression.java contains least squares implementations with closed form solutions
 *  (that is, linear and quadratic fits) as well as the Gradient Descent general
 *  fitting approach. All implementations require passes through the data to
 *  aggregate values. Whereas the linear and quadratic implementations have
 *  closed-form solutions (so they may simply compute the answer after passing through
 *  the data once), the Gradient Descent method uses a more complicated approach to fit
 *  the function f(x) to generatedFn(x).
 *
 * Notes on the Least Squares (closed-form) implementations:
 *  The least squares method involves adjusting the parameters of a model function to
 *  best fit a data set. The sample data set used in this project may either be generated
 *  by the helper function generatePoints() which uses a predefined function f(x), or may
 *  be saved at the directory located at the final String FILEDIR. The least squares method
 *  uses the cost function (sum of squared residuals) in order to determine the best fit
 *  of a function to a group of points to minimize the error.
 *
 * Notes on the Gradient Descent implementation:
 *  The gradient descent method involves finding a local minimum of a function f(x) by
 *  finding the location at which the partial derivative with respect to each coefficient
 *  is "close to" zero. By repeatedly iterating over each parameter and the data points
 *  in the function, the coefficients of the function f(x) are reduced to minimize the
 *  error function with respect to generatedFn(x). It is important to note that physically,
 *  to go "downhill" it is necessary to take the derivative of the function with respect to each coefficient.
 *  This means that a negative sign must be placed in front of the derivative to ensure that
 *  the function "goes" the right way.
 *
 * List of methods included (each method's respective Javadoc comments provides an
 *  adequate description of its implementation).
 *
 *  public static double f(double x, double[] parameters): the method to fit to 
 *      either a set of points or generatedFn()
 * 
 *  public static double generatedFn(double x): the method used to generate
 *      points to use in the program. This function is also used to regress in 
 *      the linear and quadratic fits.
 * 
 *  public static void main(String[] args): the main method; generates points
 *      according to the defined static constants and runs the linearFit(),
 *      quadraticFit(), and gradientDescent() methods respectively.
 * 
 *  public static void linearFit(Point[] xy): fits a line (y = mx + b) 
 *      to generatedFn() or an arbitrary set of points.
 * 
 *  public static void quadraticFit(Point[] xy): fits a parabola (y = Ax^2 + Bx + C) 
 *      to generatedFn() or an arbitrary set of points.
 * 
 *  public static void gradientDescent(double[] params): fits the arbitrary function
 *      f(x) to generatedFn() or an arbitrary set of points.
 * 
 *  public static double cost(double[] params) uses the cost function 
 *      Ɵ = (1/N)(Σ(f(x) - y_i)^2) to calculate the cost of the array params[]
 * 
 *  public static void generatePoints(): generates points from -(MAXN/2) to (MAXN/2)
 *      using the function generatedFn()
 * 
 *  public static void read(final String filename): reads points from the filename
 *      specified into the global data[] array.
 * 
 * Compile & Execution instructions:
 *  javac Regression.java ThreePointSum.java
 *  java Regression
 * 
 * Required files:
 *  ThreePointSum.java from Derivative assignment. This file includes the Point
 *  class as well as the threePointDerivative function.
 * 
 * Altering the function f(x) for Gradient Descent:
 *  Modifying the function fitting in this program is very simple. The documentation
 *  for the constants below allow for easy adaptation of the file from which to extract data,
 *  whether to generate data, whether to print debug statements, the tolerance and lambda for 
 *  Gradient Descent, and others. First, modify these constants to your liking in order
 *  to ensure a good fit for the data. Second, navigate to the method with signature
 *       public static double f(double x, double[] parameters)
 *  and alter the return value to equal the return value of the function f(x) you desire
 *  to fit. For example, if you would like to fit the function x^2 to a set of data,
 *  modify f(x) to return 
 *      params[0]*x*x + params[1]*x + params[2]
 *  Next, specifically modify the constant PARAM_LENGTH to equal the number of parameters
 *  used by f(x) - in this case, there are 3. The parameters are generated randomly
 *  from 0 to 100, although you may change this generation if you so wish by altering
 *  the for loop in the main method which begins with 
 *      for (int i = 0; i < params.length; i++) 
 *  And you're done! Compile and execute the program as described above.
 * 
 * @author Manan
 * Date of Creation: 9-24-2014
 */

public class Regression
{
    /*
     * Constants used generally in Regression.java include:
     *
     * MAXN: the number of points to write to the file located at
     *      FILEDIR. This parameter is not necessary if generating
     *      a function to write to a file (genereatePoints()) is NOT
     *      used in the program.
     *
     * FILE: a boolean that determines whether a file is required
     *      in the program. If FILE is set to false, writing to a file is
     *      not used.
     *
     * FILEDIR: a String that represents where the file is located. This file
     *      is either used to generate points to or read from in order to obtain
     *      a set of (x, y) data.
     *
     * GENERATE: a boolean that determines whether points are to be generated
     *      to a file. If GENERATE is set to false, generatePoints() will not run
     *      and a set of data points will have to be provided.
     */
    public static int MAXN = 100;
    public static boolean FILE = true;
    final static String FILEDIR = "c:/users/manan/desktop/points.txt";
    public static boolean GENERATE = true;

    /*
     * Constants defined below are used for the Gradient Descent method
     * (specifically, to determine when to stop and with which DELTA to take the three
     * point derivative). The constants include:
     *
     *  DELTA: The change in x value for the three points used to take the
     *    three point derivative. The points used consist of
     *    (x-DELTA, f(x-DELTA)), (x, f(x)), and (x+DELTA, f(x+DELTA))
     *
     *  TOLERANCE: How small the cost function/error derivatives can be before
     *    the program stops. If the absolute value of the aforementioned values
     *    is smaller than TOLERANCE, the parameters are printed and gradientDescent()
     *    exits.
     *
     *  MAX_ITER: The maximum number of iterations run before gradientDescent() stops
     *    by default - even if no convergence is reached.
     *
     *  LAMBDA: The learning factor used in determining how to alter the paramters.
     *    Mathematically, this constant is λ in ∆P_j = -λ(∂E/∂P_j).
     *
     *  DEBUG: If this constant is set to TRUE, every iteration will print information regarding
     *    the status of the program (which dramatically increases the method's runtime).
     * 
     *  PARAM_LENGTH: how many parameters are used (this constant is used to initialize
     *      the params array with random values).
     */
    public static double DELTA = 1e-3;
    public static double TOLERANCE = 1e-6;
    public static double MAX_ITER = 1e8;
    public static double LAMBDA = 1e-5;
    public static boolean DEBUG = false;
    public static int PARAM_LENGTH = 1;

    /*
     * data[] is an array of Points that stores the data (x, y) values - either
     * read from a file or generated from a function
     */
    public static Point[] data;

    /**
     * This method defines the function f(x) that is used to fit either
     *  a set of points or the function generatedFn(). In the gradient descent
     *  implementation, the parameters of f(x) are altered in order to minimize the
     *  cost of this function with respect to genereatedFn().
     *
     *  NOTE: Gaussian Generic function: f(x) = A*e^(-Bx^2).
     *
     * @param x the function input
     * @param parameters the coefficients used in the function
     * @return the value y = f(x)
     */
    public static double f(double x, double[] parameters)
    {
        //sample testing function (x^2 + C)
        return x*x + parameters[0]*x;

        // return parameters[0]*Math.exp(-parameters[1]*x*x);
        // return parameters[0]*Math.exp(-parameters[1]*x*x) + parameters[2];
    }

    /**
     * This method defines the function generatedFn(x) that is used to generate
     * (used in generatePoints() to write points to a file if necessary). If
     *  a set of data is pre-provided, this function is not used in favor
     *  of the provided data.
     *
     *  This method is also used to regress in linear and quadratic regression,
     *  and as the function to fit using gradient descent.
     *
     * @param x the function input
     * @return the value y = generatedFn(x)
     */
    public static double generatedFn(double x) 
    {
        return x*x;
    }

    /**
     * The main method below runs & tests the linear and quadratic
     *  fit with a given set of points as well as the Gradient Descent method. 
     *  Based on the booleans defined above (MAXN, FILE, FILEDIR, and GENERATE), 
     *  points are read from a text file or generated automatically from f(x) and 
     *  subsequently fitted with both a linear and quadratic fit. The resulting equations
     *      y = mx + b for a linear fit
     *      y = ax^2 + bx + c for a quadratic fit
     *  are printed to the console for validification. The gradient descent approach
     *  uses randomly generated coefficients to attempt to fit the function f(x)
     *  to generatedFn(x) or a set of data points depending on the defined constants
     *  at the top of the code. 
     *  
     * @param args the command line arguments (none required)
     * @throws IOException the Java default exception thrown when file IO fails
     */
    public static void main(String[] args) throws IOException
    {
        //ensure that we generate an even number of points.
        if(MAXN % 2 != 0)
        {
            MAXN++;
        }

        //if a file exists
        if(FILE)
        {
            //read from that file
            read(FILEDIR);
        }
        else
        {
            /*
             * The following code generates points from generatedFn(x) and stores them
             * in the Point[] array without writing to a file. It is run
             * when FILE is set as false (no file is provided).
             */
            data = new Point[MAXN];
            int c = 0;
            for (int i = -(MAXN/2); i < (MAXN/2); i++)
            {
                double x = i;
                double y = generatedFn(x);
                data[c] = new Point(x, y);
                c++;
            } // for (int i = -(MAXN/2); i < (MAXN/2); i++)
        } // else

        //Perform linear and quadratic fits on a set of data points.
        linearFit(data);
        quadraticFit(data);

        //Randomly generate parameters
        double[] params = new double[PARAM_LENGTH]; 
        for (int i = 0; i < params.length; i++) 
        {
            params[i] = Math.random()*100;
        }
        gradientDescent(params);
        return;
    } // public static void main(String[] args) throws IOException

    /**
     * linearFit(Point[] xy) is a method that takes in one parameter -
     *  an array of (x, y) points - and fits the points to a line, printing
     *  out the resulting fit in the form of (y = mx + b).
     *
     * @param xy the array of Points to perform a linear fit
     */
    public static void linearFit(Point[] xy)
    {
        /*
         * variables used/naming convention:
         *  xavg = average of x values
         *  yavg = average of y values
         *  x_xavg = sum over i of (x - xavg) squared
         *  x_yavg = sum over i of (y - yavg)I*(x - xavg)
         *  m = slope of the fitted line
         *  b = y intercept of fitted line
         *
         * formula:
         *  m = n*sum over i of (xi - xavg)(yi - yavg)/ (sum over i of (xi - xavg) squared)).
         *  b = m*xavg
         */

        double sumx = 0.0, sumy = 0.0;

        for (int i = 0; i < xy.length; i++)
        {
            sumx += xy[i].x;
            sumy += xy[i].y;
        }

        double xavg = sumx / xy.length, yavg = sumy / xy.length;
        double x_xavg = 0.0, x_yavg = 0.0;

        for (int i = 0; i < xy.length; i++)
        {
            x_xavg += (xy[i].x - xavg) * (xy[i].x - xavg);
            x_yavg += (xy[i].x - xavg) * (xy[i].y - yavg);
        }

        //calculate slope and y intercept
        double m = x_yavg / x_xavg;
        double b = yavg - m * xavg;

        // print results
        System.out.println("[linearFit] y = " + m + "x " + (b > 0 ? "+ " : "") + b);
        return;
    } // public static void linearFit(Point[] xy)

    /**
     * quadraticFit(Point[] xy) is a method that takes in one parameter -
     *  an array of (x, y) points - and fits the points to a quadratic function,
     *  printing out the result in the form of (y = ax^2 + bx + c).
     *
     * NOTE: Variable names in this method are from http://mathforum.org/library/drmath/view/72047.html
     *  This specific website provided a useful derivation for the quadratic fit
     *  formula and allowed me to deeply understand the method after missing class
     *  the day this method was reviewed. Although I was able to derive the closed
     *  form solution using Mathematica, I found the consistent variable names to be
     *  convenient and easy to copy-and-paste from the source. The description
     *  on the naming convention for the variables used in this segment of code
     *  is described at the Mathforum website in detail.
     *
     * @param xy the array of Points required to perform a quadratic fit
     */
    public static void quadraticFit(Point[] xy)
    {

        /*
         * note that variable names are defined as according to the aforementioned
         * naming convention in the referenced website.
         */
        double S00 = 0, S10 = 0, S20 = 0, S30 = 0, S40 = 0;
        double S01 = 0, S11 = 0, S21 = 0;

        for (int i = 0; i < xy.length; i++)
        {
            S00 += 1;
            S10 += xy[i].x;
            S20 += Math.pow(xy[i].x, 2);
            S30 += Math.pow(xy[i].x, 3);
            S40 += Math.pow(xy[i].x, 4);
            S01 += xy[i].y;
            S11 += xy[i].x*xy[i].y;
            S21 += Math.pow(xy[i].x, 2) * xy[i].y;
        } // for (int i = 0; i < xy.length; i++)

        double a = (S01*S10*S30 - S11*S00*S30 - S01*Math.pow(S20,2)
                    + S11*S10*S20 + S21*S00*S20 - S21*Math.pow(S10,2))
                    /(S00*S20*S40 - Math.pow(S10, 2)*S40 - S00*Math.pow(S30, 2)
                    + 2*S10*S20*S30 - Math.pow(S20,3));

        double b = (S11*S00*S40 - S01*S10*S40 + S01*S20*S30
                    - S21*S00*S30 - S11*Math.pow(S20, 2) + S21*S10*S20)
                    /(S00*S20*S40 - Math.pow(S10, 2)*S40 - S00*Math.pow(S30, 2)
                    + 2*S10*S20*S30 - Math.pow(S20, 3));

        double c = (S01*S20*S40 - S11*S10*S40 - S01*Math.pow(S30, 2)
                    + S11*S20*S30 + S21*S10*S30 - S21*Math.pow(S20, 2))
                    /(S00*S20*S40 - Math.pow(S10, 2)*S40 - S00*Math.pow(S30, 2)
                    + 2*S10*S20*S30 - Math.pow(S20,3));

        System.out.println("[quadraticFit] y = " + a + "x^2 " + (b > 0 ? " + " : "") + b + "x" + (c > 0 ? " + " : "") + c);
        return;
    } //public static void quadraticFit(Point[] xy)

    /**
     * gradientDescent(double[] params) is a method that performs the gradient descent
     *  algorithm using an input array of coefficients. This method prints out the
     *  updated coefficient values that are generated as a result of minimizing the error
     *  function of f(x) with respect to either the predefined point array or the points
     *  generated by function generatedFn(x).
     *
     *  Gradient descent, as described in the top-level comment, involves minimizing
     *  the cost of the parameters by taking the partial derivative with respect
     *  to each parameter, multiplying that cost by negative one (in order to
     *  "go down the hill") and finally subtracting each coefficient by a scaling
     *  factor λ * its partial derivative. This process is repeated either until MAX_ITER
     *  is reached or the function f(x) has converged; that is, the cost function has resulted
     *  in a value close enough to zero (defined by δ)
     *
     *  The method follows three primary loops: the first loops over the number of
     *  iterations defined by the constant MAX_ITER, the second loops over
     *  each parameter in order to obtain its partial derivative (Σ((f(x_i) - y)) over i) times
     *  (∂E / ∂P_j). The calculation of the partial sum and the corresponding partial
     *  derivative is conducted by a third for loop, resulting in an algorithmic
     *  runtime of O(N^3). After filling the errorDerivatives[] array, the coefficients
     *  are subtracted by the scaling factor described above and another iteration is
     *  conducted unless convergence has been determined.
     *
     * @param params the coefficients of the function f(x).
     */
    public static void gradientDescent(double[] params)
    {
        double[] errorDerivatives = new double[params.length];

        //LOOP 1: iterate to the maximum # of iterations
        for (int iter = 0; iter < MAX_ITER; iter++)
        {
            //fill the errorDerivatives array with all zeroes to prepare for the next iteration
            Arrays.fill(errorDerivatives, 0);

            //LOOP 2: iterate over each coefficient in the parameters array
            for (int i = 0; i < params.length; i++)
            {
                //LOOP 3: loop over each data point, calculate ∂E / ∂P_j
                for (int j = 0; j < data.length; j++)
                {
                    /*
                     * The partial sum, expressed by (f(x_i) - y); the total partial sum
                     * is expressed by the summation Σ((f(x_i) - y)) over i.
                     */
                    double partialSum = (f(data[j].x, params) - (double)data[j].y);

                    //threePointDerivative: partial wrt params[i] (the current coefficient)
                    params[i] -= DELTA;
                    Point one = new Point(params[i], f(data[j].x, params));

                    params[i] += DELTA;
                    Point two = new Point(params[i], f(data[j].x, params));

                    params[i] += DELTA;
                    Point three = new Point(params[i], f(data[j].x, params));

                    params[i] -= DELTA;

                    double partialDerivative = ThreePointSum.threePointDerivative
                       (
                           //first point (minus DELTA)
                           one,
                           //second point no change
                           two,
                           //third point (plus DELTA)
                           three,
                           //with respect to params[i]
                           params[i]
                       );

                    //multiply the derivative by the errorDerivatives sum to get the total value
                    errorDerivatives[i] += ((double)1/data.length)*partialSum*partialDerivative;

                } // for (int j = 0; j < data.length; j++)
            } // for (int i = 0; i < params.length; i++)

            //DEBUG: print results (temporarily)
            if(DEBUG)
            {
                System.out.println("errorDerivatives is " + Arrays.toString(errorDerivatives));
                System.out.println("params are" + Arrays.toString(params));
                System.out.println("params cost is " + cost(params));
                System.out.println("learning factor is " + LAMBDA);
                System.out.println();
            } // if(DEBUG)

            //determine whether we're done
            for (int w = 0; w < params.length; w++)
            {
                params[w] -= (double)LAMBDA*errorDerivatives[w];
            } // for (int w = 0; w < params.length; w++)

            double paramCost = cost(params);

            //if the |cost| is less than the TOLERANCE, we have converged
            if(Math.abs(paramCost) < TOLERANCE)
            {
                System.out.println("[converged] Iteration: [" + iter + "], Error: [" + paramCost + "]");
                System.out.println("The parameters are: " + Arrays.toString(params));
                return; //converged, so we're done
            } // if(Math.abs(paramCost) < TOLERANCE)
            
        } // for (int iter = 0; iter < MAX_ITER; iter++)
        
        System.out.println("[warn] The program did not converge. The parameters will be printed below.");
        System.out.println("The parameters are: " + Arrays.toString(params));
        
        return;
    } // public static void gradientDescent(double[] params)

    /**
     * cost(double[] params) is a method that returns the cost of the parameter
     *  array (with respect to the data points specified). This method makes use
     *  of the cost function defined as
     *      Ɵ = (1/N)(Σ(f(x) - y_i)^2)
     * @param params the parameter array to calculate the cost of
     * @return the cost (by means of the cost function described above) of params.
     */
    public static double cost(double[] params)
    {
        double sum = 0;
        for (int i = 0; i < data.length; i++)
        {
            sum += ((f(data[i].x, params) - data[i].y)*(f(data[i].x, params) - data[i].y));
        }
        return sum / MAXN; //divided by number of points
    } // public static double cost(double[] params)

    /**
     * generatePoints() is a helper method that generates points from the predefined
     *  function generatedFn(x) - defined at the top of the program - to the file at FILEDIR. This
     *  method opens a file designated at the final static String parameter FILEDIR
     *  (or generates this file if it does not exist) and writes points to it based on
     *  the constant MAXN (from -MAXN/2 to MAXN/2). The program then closes the file and exits;
     *  if an error is thrown, the stack trace is printed and the System exits to prevent
     *  further propagation.
     *
     * @throws IOException the Java default exception thrown when file IO fails.
     * @author Manan
     */
    public static void generatePoints() throws IOException
    {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILEDIR))))
        {
            for (int i = (int) 0; i < MAXN; i++)
            {
                double x = i;
                double y = generatedFn(x);
                out.println(x + " " + y);
            }
        } // try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILEDIR))))
        
        catch(FileNotFoundException e)
        {
            System.out.println("generatePoints() failed! Program exiting due to lack of the file located at " + FILEDIR);
            throw e;
        }
        
        finally 
        {
            return;
        }
    } // public static void generatePoints() throws IOException

    /**
     * read(final String filename) is a helper method that reads a file defined at filename and
     *  stores the data values (assuming space delineation) in the double data[] array.
     *
     * @param filename the location of the file to read input from.
     * @throws FileNotFoundException Java default exception thrown if a file is not found
     * @throws IOException Java default exception thrown if there is an Input/Output error
     */
    public static void read(final String filename) throws FileNotFoundException, IOException
    {
        BufferedReader bf = new BufferedReader(new FileReader(filename));
        int count = 0;

        //get the number of points
        while(bf.readLine() != null)
        {
            count++;
        }

        //reset BufferedReader
        bf = new BufferedReader(new FileReader(filename));
        data = new Point[count];

        for (int i = 0; i < count; i++)
        {
            //specialized delimiter may be necessary for more complicated sets (i.e. commas)
            StringTokenizer st = new StringTokenizer(bf.readLine());
            String x = st.nextToken();
            String y = st.nextToken();

            //create the point and place it in the data[] array
            Point p = new Point(Double.parseDouble(x), Double.parseDouble(y));
            data[i] = p;
        } // for (int i = 0; i < count; i++)
        
        return;
    } // public static void read(final String filename) throws FileNotFoundException, IOException
} // public class Regression

/**
 * The Point class is used to efficiently store two double variables
 *  (the x and y coordinates of any given point) together to provide
 *  a better and more usable interface for the derivative function.
 *
 * Methods provided are:
 *
 *  public Point(double tx, double ty) - constructs a point with x and y coordinates
 *   as parameters
 *  public String toString() - overrides Java's default toString (which prints the hex
 *   address of the memory location of the object) and instead prints the x and y
 *   coordinates of the Point.
 *  public boolean equals(Object obj) - overrides Java's default equals (which check
 *   if the references of both objects are equal) and instead checks the x and y
 *   coordinates of each Point to determine equality.
 *
 * @author Manan
 */
class Point
{
    public double x, y;

    public Point(double tx, double ty)
    {
        x = tx;
        y = ty;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Point other = (Point) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
        {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
        {
            return false;
        }
        return true;
    } // public boolean equals(Object obj)
} // class Point