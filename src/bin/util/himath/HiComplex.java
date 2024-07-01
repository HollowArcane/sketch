package bin.util.himath;

public class HiComplex
{
    public final double a;
    public final double b;

    public HiComplex(final double a, final double b)
    {
        this.a = a;
        this.b = b;
    }

    public static HiComplex zero()
    { return new HiComplex(0, 0); }

    public static HiComplex polar(double modulus, double argument)
    { return new HiComplex(modulus*Math.cos(argument), modulus*Math.sin(argument)); }

    public double modulus()
    { return Math.sqrt(a*a + b*b); }

    public boolean isZero()
    { return Double.compare(a, 0) == 0 && Double.compare(b, 0) == 0; }

    public double argument()
    {
        if(isZero())
        { return 0; }
        return Math.acos(a / modulus()) + (b < 0 ? 0: Math.PI);
    }

    @Override
    public String toString()
    { return "%s + %s i".formatted(a, b); }

    public static HiComplex add(HiComplex a, HiComplex b)
    { return new HiComplex(a.a + b.a, a.b + b.b); }

    public static HiComplex add(HiComplex a0, HiComplex... an)
    {
        for(int i = 0; i < an.length; i++)
        { a0 = add(a0, an[i]); }
        return a0;
    }

    public static HiComplex multiply(HiComplex a, HiComplex b)
    { return new HiComplex(a.a*b.a - a.b*b.b, a.a*b.b + a.b*b.a); }

    public static HiComplex multiply(HiComplex a0, HiComplex... an)
    {
        for(int i = 0; i < an.length; i++)
        { a0 = multiply(a0, an[i]); }
        return a0;
    }
}
