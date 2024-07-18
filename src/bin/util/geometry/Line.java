package bin.util.geometry;

public class Line
{
    public Vector2 origin;
    public Vector2 direction;
    
    public Line(Vector2 origin, Vector2 direction)
    {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public static Line through(Vector2 p1, Vector2 p2)
    { return new Line(p1, p2.copy().sub(p1)); }

    public Vector2 get(float t)
    { return origin.copy().add(direction.copy().scale(t)); }

    public double originX()
    { return origin.x; }

    public double originY()
    { return origin.y; }
    public Vector2 intersect(Line l)
    {
        double a1 =   direction.y, b1 = -  direction.x, c1 = -a1*  origin.x - b1*  origin.y;
        double a2 = l.direction.y, b2 = -l.direction.x, c2 = -a2*l.origin.x - b2*l.origin.y;
        
        return intersect(a1, b1, c1, a2, b2, c2);
    }

    public static Vector2 intersect(double a1, double b1, double c1, double a2, double b2, double c2)
    {
        double delta = a1*b2 - a2*b1;
        if(delta == 0)
        { return null; }

        return new Vector2((b1*c2 - b2*c1)/delta, (a2*c1 - a1*c2)/delta);
    }

    public double distance(Vector2 p)
    { return Vector2.sub(p, origin).cross(direction); }

    public Line translate(Vector2 v)
    {
        origin.add(v);
        return this;
    }

    public Vector2 project(Vector2 p)
    { return p.set(intersect(new Line(p, direction.copy().normal()))); }
    
    public static Line translate(Line l, Vector2 v)
    { return new Line(l.origin.copy(), l.direction.copy()).translate(v); }

    public static Vector2 project(Line l, Vector2 p)
    { return l.intersect(new Line(p, Vector2.normal(l.direction))); }

    @Override
    public String toString()
    { return "Line [origin=" + origin + ", direction=" + direction + "]"; }
}
