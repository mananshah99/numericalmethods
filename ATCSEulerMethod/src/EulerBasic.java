/**
 *
 * @author Manan
 */
public class EulerBasic {
    final static double DELTA = .001;
    /**
     * 
     * @param f the function
     * @param y0 the initial y point
     * @param a the starting time
     * @param b the ending time
     * @param h the number of iterations to run
     */
    public static double solve(Function f, double x0, int initialTime, int finalTime, double h) 
    {
        //beginning time
        double ti = initialTime;
        
        //initial x position
        double x = x0;
        
        //iterate through the time
        while (ti < finalTime) 
        {
            System.out.println (ti + " " + x);
            ti += h; //increment the time 
            //compute for x
            x += h * f.compute(ti, x);
        }
        return x;
  }
 
  public static void main (String[] args) {
    Function x = new X();
    solve(x, 3, 0, 100, DELTA);
    for(int i=0; i<10; i++) {
        System.out.println(i + " " + 3*i + (.5*9.81*i*i));
    }
  }
}

interface Function {
  public double compute(double time, double y);
}
 
class X implements Function {
  @Override
  public double compute (double time, double y) {
    return (-1/2 * 9.81 * time*time) + (y * time);
  }
}
