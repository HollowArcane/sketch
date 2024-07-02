package bin.graphics.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicStroke;
import bin.graphics.property.GraphicTrace;
import bin.graphics.property.Property;
import bin.util.Maths;

public class GLine
{
    public static final int PROPERTY_STROKE = 0;
    public static final int PROPERTY_COLOR = 1;
    public static final int PROPERTY_TRACE = 2;

    private GraphicStroke stroke;
    private GraphicColor color;
    private GraphicTrace trace;

    private Point p2;
    private Point p1;

    public GLine(Point p1, Point p2)
    { this(p1, p2, Color.BLACK, 1); }

    public GLine(Point p1, Point p2, Color color, float width)
    {
        this.p1 = p1;
        this.p2 = p2;

        this.color = new GraphicColor(color);
        this.stroke = new GraphicStroke(width);
        this.trace = new GraphicTrace(1f);
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
        g.setStroke(stroke.get(GraphicStroke.class).stroke());
        g.setColor(color.get(GraphicColor.class).color());

        float progression = trace.get(GraphicTrace.class).get();
        
        int x = (int)Maths.lerp(progression, p1.x, p2.x);
        int y = (int)Maths.lerp(progression, p1.y, p2.y);

        g.drawLine(p1.x, p1.y, x, y);
    }
}
