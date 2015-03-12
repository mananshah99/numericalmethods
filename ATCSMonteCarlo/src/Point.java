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
public class Point 
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