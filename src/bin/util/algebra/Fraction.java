package bin.util.algebra;

import bin.util.arithmetic.Euclidian;

public class Fraction implements Comparable<Fraction>
{
    private int numerator;
    private int denominator;

    public Fraction(int numerator)
    {
        this.numerator = numerator;
        this.denominator = 1;
    }

    public Fraction(int numerator, int denominator)
    {
        if(denominator == 0)
        { throw new Error("Cannot divide by 0"); }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction zero()
    { return new Fraction(0); }

    public static Fraction one()
    { return new Fraction(1); }

    public int numerator()
    { return numerator; }

    public int denominator()
    { return denominator; }

    @Override
    public int compareTo(Fraction o)
    {
        if(denominator == o.denominator)
        { return Integer.compare(numerator, o.numerator); }

        if(numerator == o.numerator)
        { return -Integer.compare(denominator, o.denominator); }

        // get the lcm of both denominators
        int lcm = Euclidian.lcm(o.denominator, denominator);
        
        // multiply each fraction by ppcm/denominator
        Fraction f1 = clone().multiply(new Fraction(lcm/denominator, lcm/denominator));
        Fraction f2 = o.clone().multiply(new Fraction(lcm/o.denominator, lcm/o.denominator));

        return f1.compareTo(f2);
    }

    public static Fraction add(Fraction f1, Fraction f2)
    { return f1.clone().add(f2); }

    public static Fraction substract(Fraction f1, Fraction f2)
    { return f1.clone().substract(f2); }

    public static Fraction simplify(Fraction f)
    { return f.clone().simplify(); }

    public static Fraction multiply(Fraction f1, Fraction f2)
    { return f1.clone().multiply(f2); }

    public static Fraction multiply(Fraction f, int n)
    { return f.clone().multiply(n); }

    public Fraction add(Fraction f)
    {
        // get the lcm of both denominators
        int lcm = Euclidian.lcm(f.denominator, denominator);
        
        // multiply each fraction by ppcm/denominator
        multiply(new Fraction(lcm/denominator, lcm/denominator));
        Fraction copy = Fraction.multiply(f, new Fraction(lcm/f.denominator, lcm/f.denominator));

        // add numerators
        numerator += copy.numerator;
        return this;
    }

    public Fraction substract(Fraction f)
    { return add(new Fraction(-f.numerator, f.denominator));  }


    public Fraction clone()
    { return new Fraction(numerator, denominator); }

    public Fraction inverse()
    { return new Fraction(denominator, numerator); }

    public Fraction simplify()
    {
        int gcd = Euclidian.gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;

        if(denominator < 0)
        { multiply(new Fraction(-1, -1)); }

        return this;
    }

    public Fraction multiply(int n)
    {
        numerator *= n;
        return this;
    }
    
    public Fraction multiply(Fraction f)
    {
        numerator *= f.numerator;
        denominator *= f.denominator;
        return this;
    }

    @Override
    public String toString()
    {
        if(numerator == 0)
        { return "0"; }
        if(denominator == 1)
        { return Integer.toString(numerator); }
        return numerator + "/" + denominator;
    }

    public String toString(int numSize, int denSize)
    {
        if(numerator == 0)
        { return formatInteger(0, numSize, denSize); }

        if(denominator == 1)
        { return formatInteger(numerator, numSize, denSize); }

        return formatFraction(this, numSize, denSize);
    }

    private String formatInteger(int integer, int numSize, int denSize)
    {
        String num = Integer.toString(integer);
        StringBuilder builder = new StringBuilder()
            .repeat(" ", numSize - num.length())
            .append(num)
            .repeat(" ", denSize + 1);

        return builder.toString();
    }

    private String formatFraction(Fraction f, int numSize, int denSize)
    {
        String num = Integer.toString(f.numerator);
        String den = Integer.toString(f.denominator);

        StringBuilder builder = new StringBuilder()
            .repeat(" ", numSize - num.length())
            .append(num)
            .append("/")
            .append(den)
            .repeat(" ", denSize - den.length());

        return builder.toString();
    }

    public static Fraction parse(String fraction)
        throws IllegalArgumentException
    {
        String[] parts = fraction.split("/");

        return switch (parts.length)
        {
            case 1 -> new Fraction(Integer.parseInt(parts[0].trim()));
            case 2 -> new Fraction(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
            default -> throw new IllegalArgumentException(fraction);
        };
    }
}
