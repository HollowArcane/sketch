package bin.util.geometry;

public final class Triangle extends Polygon
{
    public Triangle(Vector2 p1, Vector2 p2, Vector2 p3)
    { super(p1, p2, p3); }
    
    public static Triangle equilateral(Vector2 center, double base)
    { return isocelis(center, base, base * Math.sqrt(3)/2); }
    
    public static Triangle isocelis(Vector2 center, double base, double height)
    { return new Triangle(center.copy().add(base/2, height/3), center.copy().add(-base/2, height/3), center.copy().add(0, -2*height/3)); }
    
    @Override
    public Polygon addPoints(Vector2... points)
    { throw new UnsupportedOperationException("Cannot add new points to a defined triangle"); }
}
