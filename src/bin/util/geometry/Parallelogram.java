package bin.util.geometry;

public final class Parallelogram extends Quadrilateral
{
    private double base, height;
    public Parallelogram(Vector2 center, Vector2 v1, Vector2 v2)
    {
        super(
            new Vector2(center.x - v1.x/2 - v2.x/2, center.y - v1.y/2 - v2.y/2),
            new Vector2(center.x + v1.x/2 - v2.x/2, center.y + v1.y/2 - v2.y/2),
            new Vector2(center.x + v1.x/2 + v2.x/2, center.y + v1.y/2 + v2.y/2),
            new Vector2(center.x - v1.x/2 + v2.x/2, center.y - v1.y/2 + v2.y/2)
        );
        base = point(1).distance(point(0));
        height = Math.abs(point(3).copy().sub(point(0)).cross(point(1).copy().sub(point(0)).normalize()));
    }

    @Override
    public Polygon addPoints(Vector2... points)
    { throw new UnsupportedOperationException("Cannot add new points to a defined triangle"); }

    public static Parallelogram rectangle(Vector2 center, double base, double height)
    { return new Parallelogram(center, new Vector2(base, 0), new Vector2(0, height)); }

    public static Parallelogram square(Vector2 center, double side)
    { return rectangle(center, side, side); }

    public double getArea()
    { return getBase() * getHeight(); }
    
    public double getBase()
    { return base; }

    public double getHeight()
    { return height; }
}
