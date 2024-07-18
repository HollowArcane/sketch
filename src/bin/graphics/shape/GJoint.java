package bin.graphics.shape;

import java.awt.Graphics2D;
import java.awt.Point;

import bin.graphics.property.GraphicFloat;
import bin.util.geometry.Vector2;

public class GJoint extends GLine
{
    GCircle c1;
    GCircle c2;

    public GJoint(GCircle c1, GCircle c2)
    {
        Point[] points = getJoinPoints(c1, c2);
        super(points[0], points[1]);

        this.c1 = c1;
        this.c2 = c2;
    }

    public void setJoinPoints()
    {
        Point[] points = getJoinPoints(c1, c2);
        p1.setLocation(points[0]);
        p2.setLocation(points[1]);
    }

    public static Point[] getJoinPoints(GCircle c1, GCircle c2)
    {
        Vector2 v1 = new Vector2(c1.center()), v2 = new Vector2(c2.center());
        Vector2 dv = Vector2.sub(v2, v1).normalize();

        return new Point[] {
            v1.add(Vector2.scale(dv, c1.getProperty(GCircle.PROPERTY_RADIUS).get(GraphicFloat.class).get())).toPoint(),
            v2.sub(Vector2.scale(dv, c2.getProperty(GCircle.PROPERTY_RADIUS).get(GraphicFloat.class).get())).toPoint()
        };
    }

    @Override
    public void paint(Graphics2D g)
    {
        setJoinPoints();
        super.paint(g);
    }
}
