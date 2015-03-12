
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manan
 */
public class DerivativeTest {
    final static boolean DEBUG = false;
    final static double STEP = 0.1;
    final static double MAX = 1000;
    
    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("C:/Users/Manan/Desktop/out.txt")); 
        PrintWriter out2 = new PrintWriter(new FileWriter("C:/Users/Manan/Desktop/out2.txt")); 
        
        Point[] p = new Point[(int) (MAX / STEP)];
        Point[] act = new Point[(int) (MAX / STEP)];
        
        int ct = 0;
        for (double i = 0; i < MAX; i += STEP) 
        {
            p[ct] = new Point(i, f(i));
            act[ct] = new Point(i, df(i));
            ++ct;
        }
        Point[] der = new Point[(int) (MAX / STEP)];

        // Fill Point[] der with the derivative function 
        der = ThreePointSum.derivative(p, der);

        // Print both arrays to compare values
        
        for(Point pt: der) {
            if(pt != null) out.println(pt.x + " " + pt.y);
        }
        for(Point pt: act) {
            if(pt != null) out2.println(pt.x + " " + pt.y);
        }
        System.out.println(Arrays.toString(der));
        System.out.println(Arrays.toString(act));
    }
    /*
     * Functions used to test the derivative methods
     *  listed above.
     */
    private static double f(double x) 
    {
        return Math.cos(x);
    }

    private static double df(double x) 
    {
        return -1 * Math.sin(x);
    }
}
