package bin.util.geometry;

import java.awt.geom.Path2D;

import bin.util.Arrays;
import bin.util.Maths;

public sealed class Polygon extends FiniteShape permits Quadrilateral, Triangle
{
    private Vector2[] points;
    private Vector2 center;

    public Polygon(Vector2... points)
    {
        this.points = Arrays.map(points, (i, p) -> p.copy());
        center = computeCenter();
    }

    public static Polygon regular(int n, Vector2 center, double radius)
    { return regular(n, center, radius, 0); }

    public static Polygon regular(int n, Vector2 center, double radius, double initialAngle)
    {
        Vector2[] points = new Vector2[n];
        
        for(int i = 0; i < n; i++)
        { points[i] = Vector2.polar(i * 2f * Math.PI /n + initialAngle, radius).add(center); }

        Polygon p = new Polygon();
        p.points = points;
        return p;
    }

    private Vector2 computeCenter()
    { return new Vector2(bin.util.Arrays.avg(points, (i, p) -> p.x), bin.util.Arrays.avg(points, (i, p) -> p.y)); }

    @Override
    public Vector2 getCenter()
    { return center; }

    public Path2D.Float path()
    {
        Path2D.Float path = new Path2D.Float();
        if(points.length == 0)
        { return path; }

        path.moveTo((int)points[0].x, (int)points[0].y);
        for(int i = 1; i < points.length; i++)
        { path.lineTo((int)points[i].x, (int)points[i].y); }

        return path;
    }

    public Vector2 point(int index)
    { return points[index]; }

    public Vector2 getPoint(int index)
    { return points[index].copy(); }

    public Vector2[] getPoints()
    { return Arrays.map(points, (i, p) -> p.copy()); }

    @Override
    public double getArea()
    { return Math.abs(bin.util.Arrays.sum(points, (_, p) -> points[0].cross(p), (sum, value) -> sum + value) / 2); }

    @Override
    public Polygon rotate(double angle)
    {
        for(Vector2 point : points)
        { point.rotate(angle, center); }

        return this;
    }

    @Override
    public Polygon clone()
    {
        Polygon shape = new Polygon();
        shape.points = Arrays.map(points, (i, p) -> p.copy());
        return shape;
    }

    @Override
    public Polygon translate(Vector2 v)
    {
        for (Vector2 point : points)
        { point.add(v); }
        center = computeCenter();
        return this;
    }

    @Override
    public Polygon scale(Vector2 v)
    {
        Polygon shape = new Polygon();
        shape.points = Arrays.map(points, (i, p) -> Vector2.scale(p, v));
        return shape;
    }

    @Override
    public boolean intersects(FiniteShape s)
    {
        return switch (s) {
            case Polygon p -> intersects(p);
            case Circle c -> c.intersects(this);
            default -> throw new UnsupportedOperationException("Unknown shape given");
        };
    }

    private boolean intersects(Polygon p)
    { return bin.util.Arrays.some(points, (i, point) -> p.contains(point)); }

    @Override
    public boolean contains(Vector2 p)
    {
        int count = 0;
        Line vecticalUp = new Line(p, new Vector2(0, 1));
        for(int i = 0; i < points.length; i++)
        {
            Vector2 p1 = points[i];
            Vector2 p2 = points[(i + 1) % points.length];
            Vector2 inter = Line.through(p1, p2).intersect(vecticalUp);
            if(inter != null && inter.y >= p.y)
            {
                if(Maths.between(p.x, p1.x, p2.x))
                { count++; }
            }
        }

        return count % 2 == 1;
    }

    @Override
    public String toString()
    { return "Polygon [points=" + points + "]"; }

    @Override
    public Rectangle getBoundingBox()
    {
        Vector2 top = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p2.y, p1.y));
        Vector2 left = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p2.x, p1.x));
        Vector2 bottom = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p1.y, p2.y));
        Vector2 right = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p1.x, p2.x));
        return new Rectangle(left.x, top.y, right.x - left.x, bottom.y - top.y);
    }
}
