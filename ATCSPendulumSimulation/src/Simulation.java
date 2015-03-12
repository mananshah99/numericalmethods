
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Manan
 */
public class Simulation {
    
    public static double OMEGA = 1;
    public static double DELTA = 0.001;
    public static double Q = 0;
    public static double F = 0.4; // 0.4; //0;// 0.4;
    public static double FORCING_CONSTANT = 0.02;
    
    public static double forcingFunction(double t) {
        return F*Math.sin(FORCING_CONSTANT*t);
    }
    /**
     * 
     * @param v0
     * @param p0
     * @param delta
     * @param maxTime
     * @return
     * @throws FileNotFoundException 
     */
    public static double[] simulate(double v0, double p0, double delta, double maxTime) throws FileNotFoundException {
        PrintWriter csv = new PrintWriter("c:/users/manan/desktop/data.csv");
        
        //tcurr = theta
        //vcurr = omega
        
        double vcurr = 0, tcurr = 0;
        double vprev = v0, tprev = p0;
        boolean DEBUG = false;
        double maxTCurr = Double.MIN_VALUE;
        for(double i = 0; i < maxTime; i += delta) 
        {
            //v_c = v_i   + omega squared * sin(tcurr) * detlta  - q*omega squared (damping) * delta + Forcing function (broken atm)
            vcurr = vprev + -OMEGA*OMEGA*Math.sin(tcurr)*(delta) - Q*vprev*Math.abs(vprev)*(delta) + forcingFunction(i)*delta;//*(delta);//Math.sin((2/3)*i);
            tcurr = tprev + (vcurr)*(delta); //using vprev here (w_(i)) doesn't work
            
            csv.println(vcurr + ", " + tcurr);
            //csv.println(tcurr); //prints theta, change this to print other things
            //System.out.println(tcurr);
            if(DEBUG) {
                if(tcurr > maxTCurr) {
                    System.out.println("[diverging] " + tcurr + " > " + maxTCurr);
                    maxTCurr = tcurr;
                }
            }
            vprev = vcurr;
            tprev = tcurr;
        }
        return new double[] {vcurr, tcurr};
    }
    public static void main(String[] args) throws FileNotFoundException {
        simulate(1, 0, DELTA, 1000);
    }
}
