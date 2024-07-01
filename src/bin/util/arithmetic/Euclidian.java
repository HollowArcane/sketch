package bin.util.arithmetic;

public class Euclidian
{
    public static int gcd(int a, int b)
    {
        a = Math.abs(a);
        b = Math.abs(b);

        if(a == 0 && b == 0)
        { return 1; }

        if(a == 0)
        { return b; }
        
        if(b == 0)
        { return a; }

        if(a >= b)
        { return gcd(b, a % b); }

        return gcd(b, a);
    }

    public static int lcm(int a, int b)
    { return Math.abs(a * b)/gcd(a, b); }
}
