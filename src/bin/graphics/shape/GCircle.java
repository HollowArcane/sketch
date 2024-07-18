package bin.graphics.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicStroke;
import bin.graphics.property.GraphicFloat;
import bin.graphics.property.Property;
import bin.util.geometry.Vector2;

public class GCircle extends GPath
{
    public static final int PROPERTY_STROKE = 0;
    public static final int PROPERTY_COLOR = 1;
    public static final int PROPERTY_TRACE = 2;
    public static final int PROPERTY_RADIUS = 3;

    public Point p;

    private GraphicFloat radius;

    public GCircle(Point p, float radius)
    { this(p, radius, Color.BLACK, 3); }

    public GCircle(Point p, float radius, Color color, float width)
    {
        super(color, width);
        this.p = p;
        this.radius = new GraphicFloat(radius);
    }

    public Point center()
    { return p; }

    public Property getProperty(int index)
    {
        return switch (index)
        {
            case 0 -> stroke;
            case 1 -> color;
            case 2 -> trace;
            case 3 -> radius;
            default -> throw new IndexOutOfBoundsException(index);
        };
    }

    public void paint(Graphics2D g)
    {
        g.setStroke(stroke.get(GraphicStroke.class).stroke());
        g.setColor(color.get(GraphicColor.class).color());

        float progression = trace.get(GraphicFloat.class).get();
        float radius = this.radius.get(GraphicFloat.class).get();

        Path2D.Float path = new Path2D.Float();
        path.moveTo(p.x + radius, p.y);

        Vector2 center = new Vector2(p);
        for(int i = 1; i < progression * 2 * radius; i++)
        {
            Vector2 v = Vector2.polar(i*Math.PI/radius, radius).add(center);
            path.lineTo(v.x, v.y);
        }
        if(progression == 1)
        { path.lineTo(p.x + radius, p.y); }

        g.draw(path);
    }

    @Override
    public Vector2 p(float t)
    {
        float progression = trace.get(GraphicFloat.class).get();
        float radius = this.radius.get(GraphicFloat.class).get();

        return Vector2.polar(2*Math.PI*progression*t, radius).add(new Vector2(p));
    }

    @Override
    public Vector2 v(float t)
    {
        float progression = trace.get(GraphicFloat.class).get();
        float radius = this.radius.get(GraphicFloat.class).get();

        return Vector2.polar(t + Math.PI/2, radius).scale(2*Math.PI*progression);
    }
}
