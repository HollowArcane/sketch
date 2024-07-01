package bin.util.geometry;

import bin.util.Arrays;

public final class Circle extends Shape
{
    public double radius;
    public Vector2 center;

    public Circle(Vector2 center, double radius)
    {
        this.center = center;
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

    public Circle translate(Vector2 v)
    {
        center.add(v);
        return this;
    }

    public Vector2 center()
    { return center; }

    public double centerX()
    { return center.x; }

    public double centerY()
    { return center.y; }

    @Override
    public boolean intersects(Shape s)
    {
        return switch (s) {
            case Polygon p -> intersects(p);
            case Circle c -> intersects(c);
            default -> throw new UnsupportedOperationException("Unknown shape given");
        };
    }

    @Override
    public Shape rotate(double angle)
    { return this; }

    @Override
    public double area()
    { return Math.PI * radius * radius; }

    @Override
    public Shape clone()
    { return new Circle(center.copy(), radius); }

    private boolean intersects(Circle c)
    { return center.copy().sub(c.center).mag2() < Math.pow(radius + c.radius, 2); }

    private boolean intersects(Polygon p)
    { return Arrays.some(p.getPoints(), (i, point) -> contains(point)); }

    @Override
    public boolean contains(Vector2 point)
    { return point.distance2(center) < radius * radius; }

    @Override
    public String toString()
    { return "Circle [radius=" + radius + ", center=" + center + "]"; }

    @Override
    public Rectangle boundingBox()
    { return new Rectangle(center.x - radius, center.y - radius, 2*radius, 2*radius); }
}
