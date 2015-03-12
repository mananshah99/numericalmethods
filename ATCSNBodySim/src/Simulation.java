import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**

Test set 1: random objects
4
10
200000 0 0 0 0 2 0
100000 0 0 0 0 -2 0
100000 0 0 0 4 -3 0
200000 0 0 0 0 5 0

Test set 2: circular orbits
2
10
10000000 0 91339.271400641 0 -2 0 0
10000000 0 -91339.271400641 0 2 0 0

Test set 3: two 100 kg objects
2
10
100 0 0 0 -5 0 0
100 0 0 0 5 0 0

Test set 4: Random objects
7
20
200000 0 0 0 0 2 0
100000 0 0 0 0 -2 0
100000 0 0 0 4 -3 0
200000 0 0 0 0 5 0
100000 0 2 0 0 4 0
50000 0 0 0 0 -7 0
750000 7 0 0 0 16 0

Test set 5: Earth and Moon
2
3641061400
73476730900000000000000 -15688920000000000000000000 75270736000000000000000000 -6868332300000000000000000 364106143.282 83931594.7613 2880607.4259
5972000000000000000000000 0 0 0 0 0 0

Test set 6: Moon, earth, and sun
3
1382545984970
73476730900000000000000 -2101366240000000000000000000 832296771000000000000000000 -6804635110000000000000000 51995087719.40993 138254598497.0757 -1819253.752579734
5972000000000000000000000 -169523890000000000000000000000 61531089500000000000000000000 5177312270000000000000000 51630981576.12840 138170666902.3144 -4699861.178475483
1989000000000000000000000000000 0 0 0 0 0 0

Test set 7: Moon, earth, sun, and Jupiter
4
532869123583
73476730900000000000000 -2101366240000000000000000000 832296771000000000000000000 -6804635110000000000000000 51995087719.40993 138254598497.0757 -1819253.752579734
5972000000000000000000000 -169523890000000000000000000000 61531089500000000000000000000 5177312270000000000000000 51630981576.12840 138170666902.3144 -4699861.178475483
1898000000000000000000000000 -18708452000000000000000000000000 -15478474000000000000000000000000 482811396000000000000000000000 -532869123583.0276 589244708769.0078 9476606588.420691
1989000000000000000000000000000 0 0 0 0 0 0

Test set 8: Use randomObjects to verify random system

Test set 9: ........?
**/

public class Simulation 
{
    public static int N; //number of data values
    final static String INPUTFILE = "c:/users/manan/documents/netbeansprojects/atcsnbodysim/data/nbody.in";
    public static ArrayList<Body> bodies;
    public static double MINDIST = 1e4;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, Exception {
        Display.setCanvasSize(1340, 680);
        boolean status = true; //read(INPUTFILE);
        if(status == false) {
            System.err.println(
            "File reading error. Check the parameters specified below. \n" + 
            "# Input format is as follows:\n" +
            "#\n" +
            "# Line 1: N, the number of bodies there are (default N = 2)\n" +
            "# Line 2: R, the radius of the screen\n" +
            "# Lines 3..N+2: \n" +
            "# 	m_i	p_i	r_i\n" +
            "#\n" +
            "# Where,\n" +
            "#	m_i: the mass of each object\n" +
            "#	p_i: the momentum vector of each object (p_i / m_i = v_i)\n" +
            "#	r_i: the position vector of each object (i, j, k coordinates)"
             );
            System.exit(1);
        }
        randomSystem(((3.08e16)/4), 200); //radius of half a parsec 
        //randomSystem((1.89210568e16), 2000); //20 lightyears
        //randomSystem(1000000, 2000);
        double dt = 1e9; // delta t
        double currtime = 0;
        while (true) {
            Display.clear();
            euler(dt);
            currtime += dt;
            draw();
            Display.show(0);
            //System.out.println(currtime);
        }
    }
    
    /**
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws Exception 
     */
    public static boolean read(String file) throws FileNotFoundException, Exception {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            try {
                String s;
                while((s = br.readLine()) != null) {
                    if(s.charAt(0) == '#') continue; //comment
                    if(s.length() < 10 && bodies == null) { 
                        // number of objects
                        N = Integer.parseInt(s);
                        bodies = new ArrayList<>(N);
                        break; // process below
                    }
                }
                s = br.readLine();
                double radius = Double.parseDouble(s);
                Display.setXscale(-radius, +radius);
                Display.setYscale(-radius, +radius);

                for (int i = 0; i < N; i++) {
                    String[] data = br.readLine().split(" ");
                    //System.out.println(Arrays.toString(data));
                    double mass = Double.parseDouble(data[0]);
                    double[] p = new double[] {
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3])
                    };
                    Vector momentum = new Vector(p);

                    double[] r = new double[] {
                        Double.parseDouble(data[4]),
                        Double.parseDouble(data[5]),
                        Double.parseDouble(data[6])
                    };
                    Vector position = new Vector(r);
                    bodies.add(new Body(mass, momentum, position));
                }
            }
            catch (IOException | NumberFormatException e) {
                throw e;
                //return false;
            }
        }
        catch (Exception e) {
            throw e;
            //return false;
        }
        return true;
    }
    
    /**
     * 
     * @param dt 
     */
    public static void euler(double dt) {
       double dist;
        for(int i = 0; i < bodies.size(); i++) {
            for(int j = 0; j < bodies.size(); j++) {
                try {
                    if(i != j) {
                        Vector delta = bodies.get(i).R.minus(bodies.get(j).R);
                        dist = delta.magnitude();
                        if(dist < MINDIST) { // mindist for collision
                            if(bodies.get(i).M > bodies.get(j).M) {
                                bodies.get(i).M += bodies.get(j).M;
                                bodies.get(i).penRadius += bodies.get(j).penRadius;
                                bodies.get(i).P.plus(bodies.get(j).P);
                                bodies.get(i).P.times(1/(bodies.get(i).M));
                                bodies.remove(j);
                            }
                            else {
                                bodies.get(j).M += bodies.get(i).M;
                                bodies.get(j).penRadius += bodies.get(j).penRadius;
                                bodies.get(j).P.plus(bodies.get(i).P);
                                bodies.get(j).P.times(1/(bodies.get(j).M));
                                bodies.remove(i); 
                            }
                        }
                    }
                }
                catch(Exception e) {}
            }
        }
        Vector[] f = new Vector[N];
        for (int i = 0; i < bodies.size(); i++) {
            f[i] = new Vector(new double[3]);
        }
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = 0; j < bodies.size(); j++) {
                if (i != j) {
                    f[i] = f[i].plus(bodies.get(i).force(bodies.get(j)));
                }
            }
        }
        //System.out.println(Arrays.toString(f));
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).rerandomizeColor();
            bodies.get(i).move(f[i], dt);
        }
    }
    
    /**
     * 
     * @param radius
     * @param nObjects 
     */
    public static void randomSystem(double radius, double nObjects) {
        Display.setXscale(-radius, +radius);
        Display.setYscale(-radius, +radius);
        N = (int)nObjects;
        bodies = new ArrayList<>((int)nObjects);
        
        //change min and max masses
        double minMass = 328e21, //mercury's mass
               maxMass = 200*1e30; //mass of the sun
        
        double minVelocity = -1e5,
               maxVelocity = 1e5;
        
        double minPosition = -radius,
               maxPosition = +radius;
        
        for(int i=0; i < nObjects; i++) {
            double mass = (Math.random() * maxMass) + minMass;
            //fill in momentum
            double[] p = new double[3];
            for(int t = 0; t < 3; t++) {
                p[t] = mass * ((Math.random() * maxVelocity * 2) + (minVelocity));
            }
            Vector momentum = new Vector(p);
            
            double[] r = new double[3];
            for(int t = 0; t < 3; t++) {
                r[t] = (Math.random() * maxPosition*2) + minPosition;
            }
            Vector position = new Vector(r);
            bodies.add(new Body(mass, momentum, position));
        }
    }
    
    /**
     * 
     */
    public static void draw() {
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw();
        }
    }
}
