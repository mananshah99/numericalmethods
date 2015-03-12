
import java.util.ArrayList;

/**
 * Lesson: you want to take the derivative at (t2 - t1) / 2, not 
 * the beginning or end points. The error is the smallest when you
 * take the derivative at the middle. 
 * 
 * However, a simple trapezoidal basis loses a data point somewhere along
 * the line (ask for more explanation)
 *  ^ Go to Wikipedia and look up numerical derivatives 
 * 
 * There is a requirement here that the data all be evenly spaced. ^^
 * 
 * However, there is another technique that you can use. If you attempt to fit
 * a parabola, you can
 *  - fit a cubic/quartic/etc (nth order to n+1 points)
 *  - pick off the value of the derivative at every point based on the parabola
 *      or polynomial
 * 
 * @author Manan
 */
public class DifferentMethods {
    public static void main(String[] args) {
        ArrayList<Double> fx = new ArrayList<>();   //regular function
        
        ArrayList<Double> dfx = new ArrayList<>();  // Xn
        ArrayList<Double> dfx_plusone = new ArrayList<>(); // Xn+1
        ArrayList<Double> dfx_avg = new ArrayList<>(); // (Xn + Xn+1) / 2
        
        ArrayList<Double> dydx = new ArrayList<>(); //delta y / delta x
        
        for(double i = 0; i < 28; i+=0.5) {
            double fi = f(i);
            double dfi = df(i);
            double dfi_plusone = df(i+1);
            double dfi_average = df((2*i + 1)/2);
            
            fx.add(fi);
            dfx.add(dfi);
            dfx_plusone.add(dfi_plusone);
            dfx_avg.add(dfi_average);
            
            if(i > 0) {
                dydx.add(f(i) - f(i-1));
            }
            else dydx.add((double)0);
        }
        
        System.out.println("f(x)                        => " + fx);
        System.out.println("f'(x) @ Xn                  => " + dfx);
        System.out.println("f'(x) @ Xn+1                => " + dfx_plusone);
        System.out.println("f'(x) @ (Xn + X(n+1))/2     => " + dfx_avg);        
        System.out.println("dy/dx                       => " + dydx);
        
        //processing error
        
        dydx.set(0, (double)0);
        
        System.out.print("\n\n");
        for(int i=0; i<dydx.size(); i++) {
            double correct  = dydx.get(i);
            double xn       = dfx.get(i);
            double xn_p1    = dfx_plusone.get(i);
            double xn_avg   = dfx_avg.get(i);
            System.out.println(" Error values for " + i + " : "
                    + "\t X(n)    = " + (1-((correct-xn)/(correct)))
                    + "\t X(n+1)  = " + (1-((correct - xn_p1)/(correct)))
                    + "\t Average = " + (1-((correct - xn_avg)/(correct))));
        }
        
    }
    private static double f(double x) {
        return Math.pow(Math.E, -1 * Math.pow(x, 2));
    }
    private static double df(double x) {
        return -2 * x * f(x);
    }
}