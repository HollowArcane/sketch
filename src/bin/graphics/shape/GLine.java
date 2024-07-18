package bin.graphics.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicStroke;
import bin.graphics.property.GraphicFloat;
import bin.graphics.property.Property;
import bin.util.Maths;
import bin.util.geometry.Vector2;

public class GLine extends GPath
{
    public static final int PROPERTY_STROKE = 0;
    public static final int PROPERTY_COLOR = 1;
    public static final int PROPERTY_TRACE = 2;

    public Point p2;
    public Point p1;

    public GLine(Point p1, Point p2)
    { this(p1, p2, Color.BLACK, 3, new float[] { 1f }); }

    public GLine(Point p1, Point p2, Color color, float width, float[] dash)
    {
        super(color, width, dash);
        this.p1 = p1;
        this.p2 = p2;
    }

    public Property getProperty(int index)
    {
        return switch (index)
        {
            case 0 -> stroke;
            case 1 -> color;
            case 2 -> trace;
            default -> throw new IndexOutOfBoundsException(index);
        };
    }

    public void paint(Graphics2D g)
    {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setStroke(stroke.get(GraphicStroke.class).stroke());
        g2.setColor(color.get(GraphicColor.class).color());

        float progression = trace.get(GraphicFloat.class).get();
        
        int x = (int)Maths.lerp(progression, p1.x, p2.x);
        int y = (int)Maths.lerp(progression, p1.y, p2.y);

        g2.drawLine(p1.x, p1.y, x, y);
    }

    @Override
    public Vector2 p(float t)
    { return new Vector2(p1).lerp(t, new Vector2(p1).lerp(trace.get(GraphicFloat.class).get(), new Vector2(p2))); }

    @Override
    public Vector2 v(float t)
    { return new Vector2(p2).sub(new Vector2(p1)).normalize(); }
}
