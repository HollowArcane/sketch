package bin.util.geometry;

public abstract sealed class FiniteShape extends Shape permits Polygon, Circle
{
    public static FiniteShape scale(FiniteShape shape, Vector2 v)
    { return ((FiniteShape)shape.clone()).scale(v); }

    public abstract FiniteShape scale(Vector2 v);

    public abstract boolean contains(Vector2 p);
    public abstract Vector2 getCenter();
    public abstract double getArea();
    public abstract Rectangle getBoundingBox();
    public abstract boolean intersects(FiniteShape s);
}
