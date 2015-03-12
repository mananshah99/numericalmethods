/**
 * Testing module for ThreePointDerivative
 * @author Manan
 */

public class FitNPoints {
    final static boolean DEBUG = true;
    
    public static void main(String[] args) {
        double t_fcresult = 0.;
        double t_actresult = 0.;
        
        for(double i = 0; i < 1000; i+=0.001) {
            double fi = f(i);
            double fii = f(i+1);
            double fiii = f(i+2);
            
            Point g = new Point(i, fi);
            Point h = new Point(i+1, fii);
            Point j = new Point(i+2, fiii);
            
            double fitCurveResult = threePointDerivative(g, h, j, (i == 0) ? i : (i == 1000-0.001) ? 1000-0.001 : h.x);
            double actualResult = df(i);
            //System.out.println(fitCurveResult + " " + actualResult);
            double error = (fitCurveResult - actualResult)/(actualResult);
            
            if(DEBUG) {
                System.out.println("Fitcurve is \t " + fitCurveResult);
                System.out.println("  Actual is \t " + actualResult);
                System.out.println("    Error is \t " + error + "%");
            }
            
            if(!Double.isInfinite(fitCurveResult) && !Double.isNaN(fitCurveResult))
                t_fcresult += fitCurveResult;
            if(!Double.isInfinite(actualResult) && !Double.isNaN(actualResult))
                t_actresult += actualResult;
            
        }
        System.out.println("=== Total error percent is " + (t_fcresult/t_actresult) + "% ===");
    }
    
    public static Point[] derivative(Point[] pxy, Point[] dpxy) {
        //first point
        Point p1, p2, p3; 
        double der;
        p1 = pxy[0];
        p2 = pxy[1];
        p3 = pxy[2];
        
        der = threePointDerivative(p1, p2, p3, p1.x);
        
        for(int i=1; i<(Math.min(pxy.length, dpxy.length)) - 2; i++) {
            p1 = pxy[i];
            p2 = pxy[++i];
            p3 = pxy[i+2];
            
        }   
        Point fi = pxy[0];
        Point fii = pxy[1];
        Point fiii = pxy[2];
        
        return dpxy;
    }
    
    /**
     *
     * @param a first point
     * @param b second point
     * @param c third point
     * @param x x coordinate of the point you want to find the derivative at
     * @return the derivative at point x based on a, b, and c
     */
    public static double threePointDerivative(Point a, Point b, Point c, double x) {
        
        double A = ((c.x * (b.y - a.y)) 
                   + (b.x * (c.y - a.y))
                   + (a.x * (c.y - b.y))) 
                   / ((a.x - b.x)*(a.x - c.x)*(b.x - c.x));
        
        double H = (((c.x * c.x) * (a.y - b.y)) 
                   + ((a.x * a.x) * (b.y - c.y))
                   + ((b.x * b.x) * (c.y - a.y))) 
                   / (2*(c.x*(a.y - b.y) + a.x*(b.y - c.y) + b.x*(c.y - a.y)));
        
        return (2*A) * (x - H); //returns dy/dx evaluted at point x
    }
    
    /*
     * For testing
     */
    private static double f(double x) {
        return Math.exp(-1 * Math.pow(x, 2));
    }
    
    private static double df(double x) {
        return -2 * x * f(x);
    }
}
