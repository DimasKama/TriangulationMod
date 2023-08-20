package dimaskama.triangulationmod.client;

public class Line {
    public final double k;
    public final double b;
    public Line(double x1, double z1, double x2, double z2) {
        k = (z1 - z2) / (x1 - x2);
        b = z2 - k * x2;
    }
}
