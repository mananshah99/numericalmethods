import java.util.Arrays;

/**
 *
 * @author Manan
 */
public class TrapezoidalSum {
    static final double INTERVAL = .1;
    static final double LENGTH = 10000;
    public static void main(String[] args) {
        double sum = 0;
        Point[] points = new Point[(int)(LENGTH/INTERVAL)];
        
        int ct = 0;
        for(double i = 0; i < LENGTH; i+=INTERVAL) {
           /*
            double x = INTERVAL;
            double y = f(i);
            sum += (x*y);
           */
           points[ct] = new Point(i, f(i));
           ++ct;
        }
        System.out.println(Arrays.toString(points));
        System.out.println(integrate(points));
        System.out.println(df(LENGTH));
    }
    public static double integrate(Point[] points) {
        /*
         * for each point, get the x and y coordinates
         * then, the area will be (under a trapezoid) 
         *  (INTERVAL)* f(x) + .5(f(x+1)-f(x))(INTEVAL)
         */
        double tot = 0.;
        for(int i=0; i<points.length-1; i++) {
            Point curr = points[i];
            Point next = points[++i];
            double interval = (next.x - curr.x);
            System.out.println("tot is " + tot + " interval is " + interval + " dy is " + (next.y - curr.y));
            tot += (interval*(curr.y) + (.5*(next.y-curr.y)*interval));
        }
       return tot; 
    }
    
    /*
     * f(x) = x^2
     */
    public static double f(double x) {
        return x*x;
    }
    
    /*
     * f(x)dx = (x^3)/3
     */
    public static double df(double x) {
        return (x*x*x)/3;
    }
}

class Point {
    public double x, y;
    
    public Point (double tx, double ty) {
        x = tx;
        y = ty;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
}