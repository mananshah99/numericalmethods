
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Manan
 */
public class Body {
    public double M;
    public double penRadius = 0.02;
    public Vector P, // Momentum vector
                  R, // Position vector
                  V; // Velocity vector
    public Color c = Color.BLACK;
    public static double G = 6.67429e-11; //m^3/(kg/s^2) +- 0.00067 x 10^-11
    public Body(double mass, Vector momentum, Vector position) {
        M = mass; 
        P = momentum;
        R = position;
        V = P.times(1 / M); //p = mv, v = p / m
        c = randomColor();
    }
    
    public Color randomColor() {
        final float hue = 0;
        final float saturation = 0.9f;                  //1.0 for brilliant, 0.0 for dull
        
        //luminance based on mass ratio to maximum mass
        final float luminance = (float)M / (float)getMaxMass();  //1.0 for brighter, 0.0 for black
        Color color = Color.getHSBColor(hue, saturation, luminance);
        return color;
    }
    
    public double getMaxMass() {
        double MAX = Double.MIN_VALUE;
        for(Body b : Simulation.bodies) {
            MAX = Math.max(b.M, MAX);
        }
        return MAX;
    }
    public void rerandomizeColor() {
        c = randomColor();
    }
    
    public void move(Vector f, double dt) {
        Vector acc = f.times(1/M);
        V = V.plus(acc.times(dt));
        R = R.plus(V.times(dt));
    }
    
    public Vector force(Body b) {
        Vector delta = b.R.minus(this.R);
        double dist = delta.magnitude();
        double F = (G * this.M * b.M) / (dist * dist);
        return delta.direction().times(F);
    } 
    public void draw() {
        Display.setPenColor(c);
        Display.setPenRadius(penRadius);
        
        Display.point(R.cartesian(0), R.cartesian(1));
        
        Display.setPenRadius(0.02);
        Display.setPenColor(Color.BLACK);
    }
}
