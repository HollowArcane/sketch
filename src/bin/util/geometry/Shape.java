package bin.util.geometry;

public abstract sealed class Shape implements Cloneable permits Circle, Polygon
{
    public static record Rectangle(double x, double y, double base, double height) implements Cloneable
    {
        public double area()
        { return base * height; }
    }
    
    public Shape translate(double x, double y)
    { return translate(new Vector2(x, y)); }
    
    @Override
    public abstract Shape clone();
    public abstract Shape translate(Vector2 v);
    public abstract Shape rotate(double angle);
    public abstract boolean intersects(Shape s);
    public abstract boolean contains(Vector2 p);
    public abstract Vector2 center();
    public abstract double area();
    public abstract Rectangle boundingBox();
}
