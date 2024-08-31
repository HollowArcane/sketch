package bin.util.geometry;

public sealed class Quadrilateral extends Polygon permits Parallelogram
{
    public Quadrilateral(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4)
    { super(p1, p2, p3, p4); }

    @Override
    public Quadrilateral clone()
    { return new Quadrilateral(point(0), point(1), point(2), point(3)); }
}
