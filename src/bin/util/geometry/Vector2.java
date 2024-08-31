package bin.util.geometry;

import java.awt.Point;

import bin.util.Maths;

public class Vector2
{
    public double x;
    public double y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Point p)
    {
        this.x = p.x;
        this.y = p.y;
    }
    
    public static Vector2 ihat()
    { return new Vector2(1, 0); }

    public static Vector2 jhat()
    { return new Vector2(0, 1); }

    public static Vector2 unit(double angle)
    { return new Vector2(Math.cos(angle), Math.sin(angle)); }

    public static Vector2 polar(double angle, double radius)
    { return new Vector2(radius * Math.cos(angle), radius * Math.sin(angle)); }

    public static Vector2 zero()
    { return new Vector2(0, 0); }

    public static Vector2 random()
    { return random(1); }

    public static Vector2 random(double scaleX)
    { return random(scaleX, scaleX); }

    public static Vector2 random(double scaleX, double scaleY)
    {
        Vector2 v = new Vector2(Math.random(), Math.random());
        v.x *= scaleX;
        v.y *= scaleY;
        return v;
    }

    public static Vector2 lerp(double progression, Vector2 v1, Vector2 v2)
    { return new Vector2(Maths.lerp(progression, v1.x, v2.x), Maths.lerp(progression, v1.y, v2.y)); }

    public static Vector2 add(Vector2 v1, Vector2 v2)
    { return new Vector2(v1.x + v2.x, v1.y + v2.y); }

    public static Vector2 sub(Vector2 v1, Vector2 v2)
    { return new Vector2(v1.x - v2.x, v1.y - v2.y); }

    public static Vector2 scale(Vector2 v1, float scalar)
    { return new Vector2(v1.x * scalar, v1.y * scalar); }

    public static Vector2 scale(Vector2 v1, Vector2 scalar)
    { return new Vector2(v1.x * scalar.x, v1.y * scalar.y); }

    public static Vector2 normal(Vector2 v)
    { return new Vector2(v.y, -v.x); }

    public static Vector2 normalize(Vector2 v)
    {
        double mag = v.mag();
        return new Vector2(v.x/mag, v.y/mag);
    }

    public static Vector2 rotate(Vector2 v, double angle)
    {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2(v.x*cos - v.y*sin, v.x*sin + v.y*cos);
    }

    public static Vector2 rotate(Vector2 v, double angle, Vector2 center)
    {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2((v.x - center.x)*cos - (v.y - center.y)*sin + center.x, (v.x - center.x)*sin + (v.y - center.y)*cos + center.y);
    }

    public Vector2 copy()
    { return new Vector2(x, y); }

    public Vector2 set(double x, double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 v)
    { return set(v.x, v.y); }

    public Vector2 add(Vector2 v)
    { return add(v.x, v.y); }

    public Vector2 add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 lerp(double progression, Vector2 target)
    {
        set(
            Maths.lerp(progression, x, target.x),
            Maths.lerp(progression, y, target.y)
        );
        return this;
    }

    public Vector2 sub(Vector2 v)
    {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vector2 scale(double scalar)
    {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2 scale(Vector2 scalar)
    {
        x *= scalar.x;
        y *= scalar.y;
        return this;
    }

    public Vector2 normal()
    { return set(-y, x); }

    public Vector2 rotate(double angle)
    {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return set(x*cos - y*sin, x*sin + y*cos);
    }

    public Vector2 rotate(double angle, Vector2 center)
    {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return set((x - center.x)*cos - (y - center.y)*sin + center.x, (x - center.x)*sin + (y - center.y)*cos + center.y);
    }

    public boolean isZero()
    { return Double.compare(x, 0) == 0 && Double.compare(y, 0) == 0; }

    public Vector2 normalize()
    {
        if(isZero())
        { return set(1, 0); }
        return scale(1/mag());
    }

    public double distance(Vector2 v)
    { return Math.sqrt(distance2(v)); }

    public double distance2(Vector2 v)
    {
        double dx = v.x - x;
        double dy = v.y - y;
        return dx*dx + dy*dy;
    } 

    public boolean between(Vector2 v1, Vector2 v2)
    { return Maths.between(x, v1.x, v2.x) && Maths.between(y, v1.y, v2.y); }

    public Point toPoint()
    { return new Point((int)Math.round(x), (int)Math.round(y)); }


    public double mag2()
    { return x*x + y*y; }

    public double mag()
    { return Math.sqrt(mag2()); }

    public double dot(Vector2 v)
    { return x * v.x + y * v.y; }

    public double cross(Vector2 v)
    { return x * v.y - y * v.x; }

    public double angle()
    {
        if(isZero())
        { return 0; }
        return  (y > 0 ? Math.acos(x / mag()): 2*Math.PI - Math.acos(x / mag()));
    }

    @Override
    public String toString()
    { return "Vector2 [x=" + x + ", y=" + y + "]"; }
}