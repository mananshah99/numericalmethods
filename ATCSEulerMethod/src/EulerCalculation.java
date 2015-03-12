
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
 * @author Manan
 */
public class EulerCalculation 
{
    //constant for g
    final static double g = 9.81;
    //constant for drag velocity: alpha
    final static double ALPHA = 2.5;
    //constant for drag velocity: gamma
    final static double GAMMA = 6.5e-3;
    //general constant: mass
    final static double M = 1; //kg
    //drag force constant: rho (mass over volume)
    static double RHO = 1.225;
    //drag force constant: C
    final static double C = 0.46; // for a sphere
    //drag fore constnat: A (area of the sphere)
    final static double A = 1;
    //radius of the earth
    final static double RADIUS = 6371;
    //change in time to evaluate
    final static double DELTA = 1e-2;
    //sea level temperature
    final static double iTemp = 288.16;
    
    //initial velocity muzzle velocity = 120
    final static double v0 = 30;
    //initial position
    final static double p0 = 0;
    //how long is the projectile flying?
    final static double dt = 15;
    
    //write to CSV files
    static PrintWriter optimizedCSV,
                       unoptimizedCSV;
    
    public static void main(String[] args) throws IOException 
    {
        optimizedCSV = new PrintWriter(new BufferedWriter(new FileWriter("c:/users/manan/desktop/optimized.csv")));
        unoptimizedCSV = new PrintWriter(new BufferedWriter(new FileWriter("c:/users/manan/desktop/unoptimized.csv")));
       
        //result from optimized
        double[] res = euler(v0, p0, DELTA, dt);
        //run unoptimized model
        eulerBasic(v0, p0, DELTA, dt);
        
        //physics v and d
        double idealVelocity = (v0 + -g*dt);
        double idealPosition = (p0 + v0*dt + (.5*-g*dt*dt));
        
        //compare with the optimized 
        System.out.println("ideal velocity :: " + (p0 + -g*dt));
        System.out.println("ideal position :: " + (0 + p0*dt + (.5*-g*dt*dt)));
        System.out.println();
        System.out.println("realistic velocity :: " + res[0]);
        System.out.println("realistic velocity :: " + res[1]);
        System.out.println();       
        System.out.println(("realistic v % error = " + ((res[0] - idealVelocity)/idealVelocity)*100) + "%");
        System.out.println(("realistic d % error = " + ((res[1] - idealPosition)/idealPosition)*100) + "%");
    }
    
    /**
     * optimized
     * @param v0
     * @param p0
     * @param delta
     * @param maxTime
     * @return 
     */
    public static double[] euler(double v0, double p0, double delta, double maxTime) {
        double vcurr = 0, pcurr = 0;
        double vprev = v0, pprev = p0;
        
        double rhoTemp = RHO;
        for(double i = 0; i < maxTime; i += delta) 
        {
            //drag force includes: -mg (+/-) (.5*C*A*rho*v*v) = -ma
            //vcurr = vprev + -g*(delta);
            rhoTemp = getRho("adiabatic", new double[] {RHO, pprev});
            
            //vf  = vi    +  |------------------------  a  ---------------------| * t
            vcurr = vprev + (-M*g - (Math.signum(vprev)*.5*A*C*rhoTemp*vprev*vprev))*delta;
            pcurr = pprev + (vcurr)*(delta);
            vprev = vcurr;
            pprev = pcurr;
            optimizedCSV.println(pprev);
        }
        /*System.out.println(
                "velocity after " + maxTime + " seconds :: " + vcurr + "\n" + 
                "position after " + maxTime + " seconds :: " + pcurr);*/
        return new double[] {vcurr, pcurr};
    }
    
    /**
     * unoptimized
     * @param v0
     * @param p0
     * @param delta
     * @param maxTime
     * @return 
     */
    public static double[] eulerBasic(double v0, double p0, double delta, double maxTime) {
        double vcurr = 0, pcurr = 0;
        double vprev = v0, pprev = p0;
        for(double i = 0; i < maxTime; i += delta) 
        {
            //drag force includes: -mg - (.5*C*A*rho*v*v) = -ma
            vcurr = vprev + -g*(delta);
            pcurr = pprev + (vcurr)*(delta);
            vprev = vcurr;
            pprev = pcurr;
            unoptimizedCSV.println(pprev);
        }
       /* System.out.println(
                "velocity after " + maxTime + " seconds :: " + vcurr + "\n" + 
                "position after " + maxTime + " seconds :: " + pcurr);*/
        return new double[] {vcurr, pcurr};
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
            case "adiabatic":
                //System.out.println("adiabatic model chosen");
                //p = p_0*e^(-y/y_0)
                return parameters[0]*Math.exp(-(parameters[1]/RADIUS+1));
            case "isothermal":
                //System.out.println("isothermal model chosen");
                return parameters[0]*Math.pow((1-GAMMA*parameters[0]/iTemp), ALPHA);
            default:
                //System.out.println("default (p0) model chosen");
                return parameters[0];
        }
    }
}
