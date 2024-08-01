package bin.util.geometry;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import bin.util.Maths;

public sealed class Polygon extends Shape permits Quadrilateral, Triangle
{
    private List<Vector2> points;
    private Vector2 center;

    public Polygon(int npoints)
    { this.points = new ArrayList<>(npoints); }

    public Polygon(Vector2... points)
    {
        this.points = bin.util.Arrays.of(points);
        center = computeCenter();
    }

    public Polygon(ArrayList<Vector2> points)
    {
        this.points = points;
        center = computeCenter();
    }

    public static Polygon regular(int n, Vector2 center, double radius)
    { return regular(n, center, radius, 0); }

    public static Polygon regular(int n, Vector2 center, double radius, double initialAngle)
    {
        ArrayList<Vector2> points = new ArrayList<>();
        
        for(int i = 0; i < n; i++)
        { points.add(Vector2.polar(i * 2f * Math.PI /n + initialAngle, radius).add(center)); }

        return new Polygon(points);
    }

    private Vector2 computeCenter()
    { return new Vector2(bin.util.Arrays.avg(points, p -> p.x), bin.util.Arrays.avg(points, p -> p.y)); }

    public Vector2 center()
    { return center; }

    public Path2D.Float tracePath()
    {
        Path2D.Float path = new Path2D.Float();
        if(points.size() == 0)
        { return path; }

        path.moveTo((int)points.get(0).x, (int)points.get(0).y);
        for(int i = 1; i < points.size(); i++)
        { path.lineTo((int)points.get(i).x, (int)points.get(i).y); }

        return path;
    }

    public Vector2 point(int index)
    { return points.get(index); }

    public Vector2[] getPoints()
    { return points.toArray(Vector2[]::new); }

    public Polygon addPoints(Vector2... points)
    {
        for(int i = 0; i < points.length; i++)
        { this.points.add(points[i]); }
        center = computeCenter();

        return this;
    }

    @Override
    public double area()
    { return Math.abs(bin.util.Arrays.sum(points, (_, p) -> points.get(0).cross(p), (sum, value) -> sum + value) / 2); }

    @Override
    public Shape rotate(double angle)
    {
        Vector2 center = center();

        for(Vector2 point : points)
        { point.rotate(angle, center); }

        return this;
    }

    @Override
    public Shape clone()
    { return new Polygon(bin.util.Arrays.map(points, (_, p) -> p.copy())); }

    @Override
    public Shape translate(Vector2 v)
    {
        for (Vector2 point : points)
        { point.add(v); }
        center = computeCenter();
        return this;
    }

    @Override
    public boolean intersects(Shape s)
    {
        return switch (s) {
            case Polygon p -> intersects(p);
            case Circle c -> c.intersects(this);
            default -> throw new UnsupportedOperationException("Unknown shape given");
        };
    }

    private boolean intersects(Polygon p)
    { return bin.util.Arrays.some(points, point -> p.contains(point)); }

    @Override
    public boolean contains(Vector2 p)
    {
        int count = 0;
        Line vecticalUp = new Line(p, new Vector2(0, 1));
        for(int i = 0; i < points.size(); i++)
        {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get((i + 1) % points.size());
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
    public Rectangle boundingBox()
    {
        Vector2 top = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p2.y, p1.y));
        Vector2 left = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p2.x, p1.x));
        Vector2 bottom = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p1.y, p2.y));
        Vector2 right = bin.util.Arrays.max(points, (p1, p2) -> Double.compare(p1.x, p2.x));
        return new Rectangle(left.x, top.y, right.x - left.x, bottom.y - top.y);
    }
}
