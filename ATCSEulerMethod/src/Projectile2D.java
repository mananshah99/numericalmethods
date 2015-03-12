
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * v_(i+1) = v_i + a(delta t)
 * x_(i+1) = x_i + v_(i+1)(delta t)
 * 
 * Error change is linear!
 * 
 * 1 m/s: throwing a pen
 * 50 m/s: about as fast as a baseball (fast pitch)
 * mach 1
 * mach 10
 * escape velocity? 7 mi/s
 * 
 * finds the initial velocity required to give 40 cm diameter iron sphere
 *   a horizontal range of 40 km if there is
 *     (a) no atmosphere
 *     (b) a constant density atmosphere
 *     (c) a variable density atmosphere
 * 
 * no air
 * constant density
 * adiabatic
 * isothermal
 * for a cannon at 45 degrees and 21 cm diameter, mass = 1kg
 * 
 * @author Manan
 */
public class Projectile2D 
{
    //angle in radians.
    static double throwAngle = 0.785398163; //45 degrees
    
    //constant for g
    final static double g = 9.81;
    //constant for rho: alpha
    final static double ALPHA = 2.5;
    //constant for rho: gamma
    final static double GAMMA = 6.5e-3;
    //general constant: mass
    final static double M = 1; //kg
    //drag force constant: rho (mass over volume)
    static double RHO = 1.225;
    //drag force constant: C
    final static double C = 0.46; // for a sphere
    //drag fore constnat: A (area of the sphere)
    final static double A = Math.PI * 0.21 * 0.21; //1 was the first value
    //radius of the earth
    final static double RADIUS = 6371;
    //change in time to evaluate
    final static double dt = 1e-2;
    //sea level temperature
    final static double iTemp = 288.16;
    
    //initial velocity muzzle velocity = 120
    static double v0 = 1; //start at zero
    //initial position
    static double p0 = 0;
    
    public static void main(String[] args) throws IOException 
    {
        ArrayList<Double> adiabatic_temp = projectile2D(v0, throwAngle, p0, dt, "adiabatic", true, "adiabatic");
        Double[] adiabatic = adiabatic_temp.toArray(new Double[adiabatic_temp.size()]);
        
        ArrayList<Double> isothermal_temp = projectile2D(v0, throwAngle, p0, dt, "isothermal", true, "isothermal");
        Double[] isothermal = isothermal_temp.toArray(new Double[isothermal_temp.size()]);
        
        ArrayList<Double> constant_temp = projectile2D(v0, throwAngle, p0, dt, "neither", true, "constant");
        Double[] constant = constant_temp.toArray(new Double[constant_temp.size()]);
        
        ArrayList<Double> ideal_temp = projectile2D(v0, throwAngle, p0, dt, "neither", false, "ideal");
        Double[] ideal = ideal_temp.toArray(new Double[ideal_temp.size()]);
        
        v0 = 120;
        while(true) {
            //v0+=.1; //change the velocity (you can do this with the angle too) 
            //p0 += 10;
            adiabatic_temp = projectile2D(v0, throwAngle, p0, dt, "adiabatic", true, "adiabatic");
            adiabatic = adiabatic_temp.toArray(new Double[adiabatic_temp.size()]);
        
            constant_temp = projectile2D(v0, throwAngle, p0, dt, "neither", true, "constant");
            constant = constant_temp.toArray(new Double[constant_temp.size()]);
            
            ideal_temp = projectile2D(v0, throwAngle, p0, dt, "neither", false, "ideal");
            ideal = ideal_temp.toArray(new Double[ideal_temp.size()]);
            
            isothermal_temp = projectile2D(v0, throwAngle, p0, dt, "isothermal", true, "isothermal");
            isothermal = isothermal_temp.toArray(new Double[isothermal_temp.size()]);
            
            double error = error(adiabatic, constant);
            
            System.out.println(p0 + " " + error + "%");
            /*if(error > 25) {
                System.out.println(v0 + " " + error + "%");
                return;
            }*/ 
            return;
        }
        
        // System.out.println(error(isothermal, ideal));
        //compare with the optimized 
        /*System.out.println("unoptimized velocity :: " + unoptimized[0]);
        System.out.println("unoptimized position :: " + unoptimized[1]);
        System.out.println();
        System.out.println("realistic velocity :: " + optimized[0]);
        System.out.println("realistic position :: " + optimized[1]);*/
        
        /*System.out.println();       
        System.out.println(("realistic v % error = " + ((res[0] - idealVelocity)/idealVelocity)*100) + "%");
        System.out.println(("realistic d % error = " + ((res[1] - idealPosition)/idealPosition)*100) + "%");*/
    }
    
    /**
     * Mean Squared Error; see
     *  http://en.wikipedia.org/wiki/Root-mean-square_deviation
     *  or
     *  https://www.physicsforums.com/threads/calculating-match-between-two-data-sets.617026/
     * 
     * Dividing by the max minus the min gives you a normalized error. 
     * @param a data set 1  
     * @param b data set 2
     * @return 
     */
    public static double error(Double[] a, Double[] b) {
        double ms = 0;
        if(a.length != b.length) {
            //System.err.println("[error calculation] ERR: The array's lengths did not match.");
        }
        double len = Math.min(a.length, b.length);
        
        for (int i = 0; i < len; i++)
            ms += (a[i]-b[i])*(a[i]-b[i]);
        ms /= len;
        
        double max = Math.max(Collections.max(Arrays.asList(a)), Collections.max(Arrays.asList((b))));
        double min = Math.min(Collections.min(Arrays.asList(a)), Collections.min(Arrays.asList((b))));
        return (Math.sqrt(ms)/(max-min));
    }
    
    /**
     * Euler method for projectile motion is used to simulate 2D motion.
     * @param v0 the initial velocity
     * @param angle the initial angle thrown
     * @param p0 the initial y position
     * @param delta the change in time to evaluate at
     * @param maxTime the maximum amount of time (t = 0 to t = maxTime)
     * @param mode for rho, either adiabatic, isothermal, or neither
     * @param drag whether drag is used or not
     * @param optimized whether to print to optimized csv or not
     * @return the y velocity and y position respectively
     */
    public static ArrayList<Double> projectile2D (   
                                            double v0,          //initial velocity
                                            double angle,       //initial angle thrown
                                            double p0,          //initial position (y)
                                            double delta,       //change in time (intervals)
                                            String mode,        //adiabatic, isothermal, or regular
                                            boolean drag,       //yes or no drag
                                            String outfile      //filename to print out
                                        ) throws IOException    //default exception
    {
        //contains 
        ArrayList<Double> ret = new ArrayList<>();
        
        PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter("c:/users/manan/desktop/" + outfile + ".csv")));

        //vy changes through time
        double vcurr_y = 0;
        double vprev_y = v0*Math.sin(angle);
        //vx does not change
        double v_x = v0*Math.cos(angle); //stays constant
        
        //x and y posiitons
        double pprev_y = p0, pcurr_y = 0;
        double pprev_x = 0, pcurr_x = 0;
        
        double rhoTemp = RHO;
        while(pcurr_y >= 0) //until it hits the ground 
        {
            //drag force includes: -mg (+/-) (.5*C*A*rho*v*v) = -ma
            rhoTemp = getRho(mode, new double[] {RHO, pprev_y});
            
            //change the y velocity
            //vf  = vi    +  |------------------------  a  ---------------------| * t
            if(drag) 
                vcurr_y = vprev_y + ((-M*g - (Math.signum(vprev_y)*.5*A*C*rhoTemp*vprev_y*vprev_y))/M)*delta;
            else 
                vcurr_y = vprev_y + -M*g*(delta);
            
            //the x velocity stays constant
            pcurr_x = pprev_x + (v_x)*(delta);
            //get the y position
            pcurr_y = pprev_y + (vcurr_y)*(delta);
            
            //prev/current y velocity
            vprev_y = vcurr_y;
            
            //previous position
            pprev_x = pcurr_x;
            pprev_y = pcurr_y;
            
            //add the previous y position
            ret.add(pprev_y);
            
            p.println(pcurr_x + ", " + pcurr_y);
        }
       
        /*System.out.println(
                "velocity after " + maxTime + " seconds :: " + vcurr + "\n" + 
                "position after " + maxTime + " seconds :: " + pcurr);*/
        p.close();
        return ret;
    }
    
    /**
     * 
     * @param mode the mode used for the getRho function
     *  "adiabatic" for adiabatic: p0, y
     *  "isothermal" for isothermal: p0
     *  any other mode is defaulted to returning the original 
     * @return 
     */
    public static double getRho(final String mode, double[] parameters) 
    {
        switch (mode.toLowerCase()) {
            case "isothermal":
                //p = p_0*e^(-y/y_0)
                return parameters[0]*Math.exp(-((RADIUS + parameters[1])/RADIUS));
            case "adiabatic":
                return parameters[0]*Math.pow((1-GAMMA*parameters[0]/iTemp), ALPHA);
            default:
                /*System.err.println("ERR: got " + mode + " which wasn't isothermal or adiabatic."
                        + " Defaulted to constant rho. ");*/
                return parameters[0];
        }
    }
}
