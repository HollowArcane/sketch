package bin.util.geometry;

public sealed class Quadrilateral extends Polygon permits Parallelogram
{
    public Quadrilateral(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4)
    { super(p1, p2, p3, p4); }
    
    @Override
    public Polygon addPoints(Vector2... points)
    { throw new UnsupportedOperationException("Cannot add new points to a defined triangle"); }
}
