
/**
 * ThreePointSum is a class that calculates the derivative of an array of Points. 
 * 
 * ThreePointSum contains the following methods:
 *  Point[] derivative(Point[] pxy, Point[] dpxy) with parameters
 *      Point[] pxy: an array of points to find the derivative of
 *      Point[] dpxy: an array (initially empty) to store the derivatives of 
 *          the points in pxy
 *  which returns an array of the derivatives of the points in Point[] pxy.
 * 
 *  double threePointDerivative(Point a, Point b, Point c, double x) with parameters
 *      Point a: the first point in the three-point parabolic fit
 *      Point b: the second point in the three-point parabolic fit
 *      Point c: the third point in the three-point parabolic fit
 *      double x: the x-coordinate of the point at which to find the derivative
 *  which returns the derivative at point x based on the curve approximated by 
 *  a, b, and c.
 *
 * Author: Manan Shah
 * Date of Creation: 9/08/2014
 */
public class ThreePointSum
{

    /**
     * derivative(Point[] pxy, Point[] dpxy) calculates the derivative of a
     * point array pxy and stores the result in the array dpxy.
     *
     * The algorithm initially calculates the derivative at the first point of
     * pxy and stores it in dpxy[0]. It then runs a for loop from the second
     * point of pxy to the n-1th point of pxy and uses the
     * threePointDerivative function to calculate the derivative at every point.
     * 
     * Finally, it calculates the derivative at the last point and then returns
     * the array dpxy.
     *
     * @author Manan Version 982014
     */
    public static Point[] derivative(Point[] pxy, Point[] dpxy) 
    {
        //first point
        Point p1, p2, p3;
        double der;

        //get three points initially
        p1 = pxy[0];
        p2 = pxy[1];
        p3 = pxy[2];

        //calculate the derivative at point p1
        der = threePointDerivative(p1, p2, p3, p1.x);

        //store it in the first element of the array
        dpxy[0] = new Point(p1.x, der);

        for (int i = 0; i < (Math.min(pxy.length, dpxy.length)) - 3; i++) 
        {
            //get points
            p1 = pxy[i];
            p2 = pxy[i + 1];
            p3 = pxy[i + 2];

            //calculate the derivative at these three points
            der = threePointDerivative(p1, p2, p3, p2.x);

            //save it at dpxy[i+1]
            dpxy[i + 1] = new Point(p2.x, der);
        }
        
        //do the derivative of the last point separately (edge case)
        dpxy[dpxy.length-1] = new Point(pxy[pxy.length-1].x, threePointDerivative(pxy[pxy.length-3],pxy[pxy.length-2] ,pxy[pxy.length-1], pxy[pxy.length-1].x));
        
        //return the Point[] array containing the derivatives of each point
        return dpxy;
    }

    /**
     * Calculates the three point derivative of a function given three points and 
     *  the x coordinate of the point where the derivative should be taken.
     *
     * @param a first point
     * @param b second point
     * @param c third point
     * @param x x coordinate of the point you want to find the derivative at
     * @return the derivative at point x based on a, b, and c
     */
    public static double threePointDerivative(Point a, Point b, Point c, double x) 
    {
        double denominator = (a.x - b.x) * (a.x - c.x) * (b.x - c.x);
        double A = (c.x * (b.y - a.y) + b.x * (a.y - c.y) + a.x * (c.y - b.y)) / denominator;
        double B = (c.x * c.x * (a.y - b.y) + b.x * b.x * (c.y - a.y) + a.x * a.x * (b.y - c.y)) / denominator;
        return (2 * A * x) + B;
    }

}
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
}