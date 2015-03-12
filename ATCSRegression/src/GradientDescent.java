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
 * Gradient Descent is a class that contains multiple utility methods
 *  and one primary method to run the Gradient Descent algorithm on a set of
 *  either generated or predetermined Points. The class includes the following
 *  methods:
 *
 *
 * pick any random initial values for a, b, and c
 *
 * USE A LEARNING FACTOR THAT STARTS BIG AND GOES SMALL
 *  or vice versa
 *
 * @author Manan
 */
public class GradientDescent
{
    /*
     * data[] is an array of Points that stores the data (x, y) values - either
     * read from a file or generated from a function
     */
    public static Point[] data;

    /*
     * filename is a constant that is used to determine the filepath from which
     * to read points. If points are being generated, filename is not used.
     */
    final static String filename = "C:/Users/Manan/Desktop/functionpoints.txt";

    /*
     * Constants defined below are used for the Gradient Descent method
     * (specifically, to determine when to stop and with which delta to take the three
     * point derivative). The constants include:
     *
     *  delta: The change in x value for the three points used to take the
     *    three point derivative. The points used consist of
     *    (x-delta, f(x-delta)), (x, f(x)), and (x+delta, f(x+delta))
     *
     *  tolerance: How small the cost function/error derivatives can be before
     *    the program stops. If the absolute value of the aforementioned values
     *    is smaller than tolerance, the parameters are printed and gradientDescent()
     *    exits.
     *
     *  maxIterations: The maximum number of iterations run before gradientDescent() stops
     *    by default - even if no convergence is reached.
     *
     *  learningFactor: The learning factor used in determining how to alter the paramters.
     *    Mathematically, this constant is λ in ∆P_j = -λ(∂E/∂P_j).
     *
     *  DEBUG: If this constant is set to TRUE, every iteration will print information regarding
     *    the status of the program (which dramatically increases the method's runtime).
     */
    static double delta = 1e-3;
    static double tolerance = 1e-6;
    static double maxIterations = 1e8;
    static double learningFactor = 1e-5;
    static boolean DEBUG = false;
    static boolean GENERATE = false;
    
    /**
     * The main method performs four (4) main steps to run the Gradient Descent
     *  program. It first 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
        generatePoints();
        double[] params = new double[] {2}; //TODO: Make this random!
        read(filename);
        gradientDescent(params);
    }

    /**
     * Generic function: f(x) = A*e^(-Bx^2) to fit.
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
     *
     * @param x
     * @return
     */
    static double toGenerate(double x) 
    {
        return x*x;
    }

    /**
     *
     * @param params
     */
    public static void gradientDescent(double[] params)
    {
        double[] errorDerivatives = new double[params.length];
        //LOOP 1: iterate to the maximum # of iterations
        for(int iter = 0; iter < maxIterations; iter++)
        {
            //fill the errorDerivatives array with all zeroes to prepare for the next iteration
            Arrays.fill(errorDerivatives, 0);
            
            //LOOP 2: iterate over each coefficient in the parameters array
            for(int i = 0; i < params.length; i++)
            {
                //dE / dP_j
                //LOOP 3: loop over each data point, calculate dE / dP_j
                for(int j = 0; j < data.length; j++)
                {
                    /*
                     * The partial sum, expressed by (f(x_i) - y); the total partial sum 
                     * is expressed by the summation Σ((f(x_i) - y)) over i.
                     */
                    double partialSum = (f(data[j].x, params) - (double)data[j].y);
                    
                    //threePointDerivative: partial wrt params[i] (the current coefficient)
                    params[i] -= delta;
                    Point one = new Point(params[i], f(data[j].x, params));

                    params[i] += delta;
                    Point two = new Point(params[i], f(data[j].x, params));

                    params[i] += delta;
                    Point three = new Point(params[i], f(data[j].x, params));

                    params[i] -= delta;

                    //TODO: this might be the problem. is the logic here correct?
                    double partialDerivative = ThreePointSum.threePointDerivative
                       (
                           //first point (minus delta)
                           one,
                           //second point no change
                           two,
                           //third point (plus delta)
                           three,
                           //with respect to params[i]
                           params[i]
                       );

                    //multiply the derivative by the errorDerivatives sum to get the total value
                    errorDerivatives[i] += ((double)1/data.length)*partialSum*partialDerivative;

                } // for(int j = 0; j < data.length; j++)
            }

            //DEBUG: print results (temporarily)
            if(DEBUG)
            {
                System.out.println("errorDerivatives is " + Arrays.toString(errorDerivatives));
                System.out.println("params are" + Arrays.toString(params));
                System.out.println("params cost is " + cost(params));
                System.out.println("learning factor is " + learningFactor);
                System.out.println();
            }

            //determine whether we're done
            for(int w = 0; w < params.length; w++)
            {
                params[w] -= (double)learningFactor*errorDerivatives[w];
            }

            double paramCost = cost(params);

            //if the |cost| is less than the tolerance, we have converged
            if(Math.abs(paramCost) < tolerance)
            {
                System.out.println("[converged] Iteration: [" + iter + "], Error: [" + paramCost + "]");
                System.out.println("The parameters are: " + Arrays.toString(params));
                return; //converged, so we're done
            }
        }
    }

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
        for(int i = 0; i < data.length; i++)
        {
            sum += ((f(data[i].x, params) - data[i].y)*(f(data[i].x, params) - data[i].y));
        }
        return sum / MAXN; //divided by number of points
    }

    /**
     * read(final String filename) is a method that
     * @param filename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
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

        for(int i = 0; i < count; i++)
        {
            //specialized delimiter may be necessary for more complicated sets (i.e. commas)
            StringTokenizer st = new StringTokenizer(bf.readLine());
            String x = st.nextToken();
            String y = st.nextToken();

            //create the point and place it in the data[] array
            Point p = new Point(Double.parseDouble(x), Double.parseDouble(y));
            data[i] = p;
        }
    }

    /**
     * Generates points based on a given function
     * @throws IOException
     */
    final static double MAXN = 100;
    public static void generatePoints() throws IOException 
    {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) 
        {
            for(int i = (int) 0; i < MAXN; i++) 
            {
                double x = i;
                double y = toGenerate(x);
                out.println(x + " " + y);
            }
        }
        catch(FileNotFoundException e) 
        {
            System.out.println("generatePoints() failed! Program exiting due to lack of the file defined at " + filename);
            throw e;
        }
    }
}
