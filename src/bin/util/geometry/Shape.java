package bin.util.geometry;

public abstract sealed class Shape implements Cloneable permits FiniteShape, InfiniteShape
{
    public static record Rectangle(double x, double y, double base, double height) implements Cloneable
    {
        public double area()
        { return base * height; }
    }
    
    public static Shape translate(Shape s, Vector2 v)
    { return s.clone().translate(v); }
    
    public static Shape rotate(Shape s, double angle)
    { return s.clone().rotate(angle); }

    public Shape translate(double x, double y)
    { return translate(new Vector2(x, y)); }
    
    @Override
    public abstract Shape clone();
    public abstract Shape translate(Vector2 v);
    public abstract Shape rotate(double angle);
}
