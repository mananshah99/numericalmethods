/**
 * RadioactiveDecay is a class that contains the following methods. 
 * 
 *  public static double f(double x, double t): the derivative of the radioactive
 *      decay function, f(x, t) is used in the Runge Kutta simluation.
 * 
 *  public static double rk4(double value, double time, double dx): determines the 
 *      next value of a function given its current value, the elapsed time, and the
 *      time interval by using the Runge Kutta method. rk4 is the primary method used
 *      to determine the next value (N_(i+1)) given N_i.
 * 
 *  public static double rungeKutta(double N_i, double dx, double maxTime): runs the    
 *      rk4 method over a loop from t = 0 to t = maxTime with a step interval dx in 
 *      order to determine how much of the radioactive substance is left after an
 *      elapsed time maxTime. This method exists to encapsulate the inner methods
 *      such as rk4 and f and only expose the end user with a straightforward, easy
 *      to use method for simulating radioactive decay.
 *  
 *  It is important to note that the Runge Kutta simulation is much more expensive to 
 *  compute than the Euler method, which works well in most cases. The implementation 
 *  in this program is the same one that MATLAB and many other prominent statistical software
 *  use, and is cited on Wikipedia as "The" Runge Kutta method. Thus, in most cases, the 
 *  Euler method will prove efficient and also noticeably faster; the Runge Kutta method
 *  should only be used if the additional precision is of paramount importance. 
 * 
 * @author Manan Shah
 * Date of Creation: 11-06-2014
 */
public class RadioactiveDecay 
{
    
    /**
     * Constants:
     *  N_o is the initial amount of radioactive material
     *  tau is the decay constant for the material specified (20 in this case)
     *  dx is the change in time used for the Runge Kutta method (the smaller dx is, the 
     *      more precise the model will be)
     *  maxTime determines how long the simulation will run
     */
    static double N_o = 100.; //start with 100 g
    static double tau = 20.;
    static double dx = 0.001;
    static double maxTime = 14.;
    
    /**
     * double f(double x, double t) is an equation that resembles the derivative
     *  of the radioactive decay function used in the Runge Kutta method. Euler's method
     *  of prediction would calculate 
     *      N_(i+1) = (-N_o/tau)*e^(-t/tau)*(delta t) + N_i
     *  In contrast, the Runge-Kutta method uses the derivative in four calculations expressed
     *  in the rk4 method below to determine N_(i+1).
     * @param x the current amount of radioactive material
     * @param t the current time (time elapsed)
     * @return the value of the derivative of the radioactive decay function evaluated at time t
     */
    public static double f(double x, double t) 
    {
        return (-N_o/tau)*Math.exp(-t/tau);
    } // public static double f(double x, double t)
    
    /**
     * rk4(double value, double time, double dx) is a method that computes the four
     *  constants needed for the Runge Kutta method. k1, k2, k3, and k4 are increments
     *  based on the slopes of different points along the size of the interval (given
     *  by dx). When averaging the intervals, greater weight is given to the increments 
     *  near the midpoint.
     * 
     * rk4 returns the next value of a function given the current value it is 
     *  being evaluated at, the elapsed time, and the step interval. Specifically, as
     *  mentioned above, the method calculates k1, k2, k3, and k4 using the function above
     *  and returns the current value added to their weighted average. 
     * 
     * @param value the current value of the model at the time of the rk4 evaluation
     * @param time the elapsed time
     * @param dx the step size of evaluation
     * @return the next value of the model as determined by the classical Runge Kutta method
     */
    public static double rk4(double value, double time, double dx) 
    {
        double	k1 = f(value, time),
		k2 = f(value + k1 / 2, time + dx / 2),
		k3 = f(value + k2 / 2, time + dx / 2),
		k4 = f(value + dx*k3, time + dx);
        
	return value + ((k1 + 2 * k2 + 2 * k3 + k4) * (dx/6));
    } // public static double rk4(double value, double time, double dx) 
    
    /**
     * rungeKutta(double N_i, double dx, double maxTime) is the primary program
     *  which uses the Runge Kutta method to calculate the remaining amount of 
     *  radioactive material that is left after the elapsed time maxTime. The
     *  method encapsulates many of the unnecessary method calls from the user and 
     *  only requires three parameters: The initial amount of material, the 
     *  time increment size, and the time of decay. For any specified material, 
     *  the constant tau may need to be altered to provide an accurate estimation.
     * 
     * @param N_i the initial amount of radioactive material
     * @param dx the time increment size
     * @param maxTime the time of decay
     * @return the amount of the substance remaining after maxTime has passed using
     *  the Runge Kutta method.
     */
    public static double rungeKutta(double N_i, double dx, double maxTime) 
    {
        double time = 0.;
        while(time < maxTime)
        {
            N_i = rk4(N_i, time, dx);
            time += dx;
        }
        
        return N_i;
    } // public static double rungeKutta(double N_i, double dx, double maxTime)
    
    public static void main(String[] args) 
    {
        double yhat = rungeKutta(N_o, dx, maxTime);
        double y =  N_o * Math.exp(-maxTime/tau);
        System.out.println("error: " + (y - yhat)/(yhat));
    } // public static void main(String[] args) 
} // public class RadioactiveDecay 
