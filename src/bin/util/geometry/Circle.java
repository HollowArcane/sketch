package bin.util.geometry;

import java.util.Objects;

import javax.management.RuntimeErrorException;

public final class Circle extends FiniteShape
{
    public double radius;
    private Vector2 center;

    public Circle(Vector2 center, double radius)
    {
        this.center = center != null ? center.copy(): Vector2.zero();
        this.radius = radius;
    }

    public static Circle through(Vector2 p1, Vector2 p2, Vector2 p3)
    {
        Vector2 v32 = p3.copy().sub(p2);
        Vector2 v21 = p2.copy().sub(p1);

        double cross = v21.cross(v32);
        if(Double.compare(cross, 0) == 0)
        { return null; }

        double mag2 = p2.mag2();
        double m32 = p3.mag2() - mag2, m21 = mag2 - p1.mag2();

        Vector2 center = new Vector2(
            (v32.y * m21 - v21.y * m32)/(2*cross),
            (v32.x * m21 - v21.x * m32)/(2*-cross)
        );
        return new Circle(center, p3.copy().sub(center).mag());
    }

    public Vector2 center()
    { return center; }

    @Override
    public Vector2 getCenter()
    { return center.copy(); }

    @Override
    public boolean intersects(FiniteShape s)
    {
        return switch (s) {
            case Polygon p -> intersects(p);
            case Circle c -> intersects(c);
            default -> throw new UnsupportedOperationException("Unknown shape given");
        };
    }

    @Override
    public Circle translate(Vector2 v)
    {
        center.add(v);
        return this;
    }

    @Override
    public FiniteShape scale(Vector2 v)
    {
        if(v.x != v.y)
        { throw new RuntimeException("Cannot scale a circle with different scalar values"); };
        return new Circle(v, radius * v.x);
    }

    @Override
    public Circle rotate(double angle)
    { return this; }

    @Override
    public double getArea()
    { return Math.PI * radius * radius; }

    @Override
    public Circle clone()
    { return new Circle(center, radius); }

    @Override
    public boolean contains(Vector2 point)
    { return point.distance2(center) < radius * radius; }

    private boolean intersects(Circle c)
    { return center.copy().sub(c.center).mag2() < Math.pow(radius + c.radius, 2); }

    public boolean intersectsSegment(Vector2 p1, Vector2 p2)
    {
        if(contains(p1) || contains(p2))
        { return true; }

        Vector2 projection = Line.project(Line.through(p1, p2), center);
        if(!projection.between(p1, p2))
        { return false; }

        return projection.distance2(center) < radius * radius;
    }

    private boolean intersects(Polygon p)
    {
        Vector2[] points = p.getPoints();
        for(int i0 = 0; i0 < points.length; i0++)
        {
            int i1 = (i0 + 1)%points.length;
            if(intersectsSegment(points[i0], points[i1]))
            { return true; }
        }
        return p.contains(center);
    }

    @Override
    public String toString()
    { return "Circle [radius=" + radius + ", center=" + center + "]"; }

    @Override
    public Rectangle getBoundingBox()
    { return new Rectangle(center.x - radius, center.y - radius, 2*radius, 2*radius); }
}
