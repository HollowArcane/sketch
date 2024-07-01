package bin.util.geometry;

import java.awt.geom.Path2D;

public class Parabola
{
    private Vector2 focus;
    private Line directrix;

    public Parabola(Vector2 focus, Line directrix)
    {
        this.focus = focus;
        this.directrix = directrix;
    }

    public Parabola translate(Vector2 v)
    {
        focus.add(v);
        directrix.translate(v);
        return this;
    }

    public Vector2 get(float t)
    {
        Vector2 p = directrix.get(t);

        double a1 = focus.x - p.x, b1 = focus.y - p.y, c1 = -a1*(p.x + focus.x)/2 - b1*(p.y + focus.y)/2;
        double a2 = -directrix.direction.x, b2 = -directrix.direction.y, c2 = -a2*p.x - b2*p.y;

        return Line.intersect(a1, b1, c1, a2, b2, c2);
    }

    public Path2D path(float tmin, float tmax, float step)
    {
        Vector2 next = get(tmin);

        Path2D.Float path = new Path2D.Float();
        path.moveTo(next.x, next.y);

        for(float t = tmin + step; t <= tmax; t += step)
        {
            next = get(t);
            path.lineTo(next.x, next.y);
        }

        return path;
    }
}
