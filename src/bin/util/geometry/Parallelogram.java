package bin.util.geometry;

public final class Parallelogram extends Quadrilateral
{
    public Parallelogram(Vector2 center, Vector2 v1, Vector2 v2)
    { super(
        new Vector2(center.x - v1.x/2 - v2.x/2, center.y - v1.y/2 - v2.y/2),
        new Vector2(center.x + v1.x/2 - v2.x/2, center.y + v1.y/2 - v2.y/2),
        new Vector2(center.x + v1.x/2 + v2.x/2, center.y + v1.y/2 + v2.y/2),
        new Vector2(center.x - v1.x/2 + v2.x/2, center.y - v1.y/2 + v2.y/2)
    );}

    public static Parallelogram rectangle(Vector2 center, double base, double height)
    { return new Parallelogram(center, new Vector2(base, 0), new Vector2(0, height)); }

    public static Parallelogram square(Vector2 center, double side)
    { return rectangle(center, side, side); }

    public double getArea()
    { return getBase() * getHeight(); }
    
    public double getBase()
    { return points.get(1).distance(points.get(0)); }

    public double getHeight()
    { return Math.abs(points.get(3).copy().sub(points.get(0)).cross(points.get(1).copy().sub(points.get(0)).normalize())); }
}
