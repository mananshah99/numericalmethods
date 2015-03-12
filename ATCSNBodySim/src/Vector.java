

/**
 * @author Manan
 */
public class Vector { 
    public final int N;
    private double[] data;

    // create the zero vector of length N
    public Vector(int N) {
        this.N = N;
        this.data = new double[N];
    }

    // create a vector from an array
    public Vector(double[] data) {
        N = data.length;
        this.data = new double[N];
        System.arraycopy(data, 0, this.data, 0, N);
    }

    public double dot(Vector that) {
        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    public Vector plus(Vector that) {
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    public Vector minus(Vector that) {
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }
    
    public Vector times(double factor) {
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++)
            c.data[i] = factor * data[i];
        return c;
    }

    public Vector direction() {
        return this.times(1.0 / this.magnitude());
    }

    @Override
    public String toString() {
        String s = "<";
        for (int i = 0; i < N; i++) {
            s += data[i];
            if (i < N-1) s+= ", "; 
        }
        return s + ">";
    }

    public double cartesian(int i) {
        return data[i];
    }
}
