package bin.util;

public class Maths
{
    public static double lerp(double amount, double min, double max)
    { return (max - min)*amount + min; }

    public static double map(double value, double imin, double imax, double omin, double omax)
    { return lerp((value - imin) / (imax - imin), omin, omax); }

    public static double constrain(double value, double v1, double v2)
    { return Math.max(Math.min(Math.max(v1, v2), value), Math.min(v1, v2)); }

    public static boolean between(double value, double v1, double v2)
    { return value >= Math.min(v1, v2) && value <= Math.max(v1, v2); }
}