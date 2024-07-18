package bin.graphics.shape;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicFont;
import bin.graphics.property.Property;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;
import bin.util.paint.TextStroke;

public class GText extends GPin
{
    public static enum RotationBehaviour { ZERO, HALF, FREE }

    public static final int PROPERTY_COLOR = 0;
    public static final int PROPERTY_FONT = 1;

    private GraphicFont font;
    private GraphicColor color;
    public String value;

    private RotationBehaviour rotation;
    public Point position;

    public GText(String value)
    { this(null, value); }

    public GText(Point position, String value)
    {
        this.position = position;
        this.value = value;
        this.rotation = RotationBehaviour.ZERO;
        font = new GraphicFont(24);
        color = new GraphicColor(Color.BLACK);
    }

    public void setValue(String value)
    { this.value = value; }

    public void setPosition(Point position)
    { this.position = position; }

    public Property getProperty(int index)
    {
        return switch (index)
        {
            case 0 -> color;
            case 1 -> font;
            default -> throw new ArrayIndexOutOfBoundsException(index);
        };
    }

    public void setRotationBehaviour(RotationBehaviour rotationBehaviour)
    { this.rotation = rotationBehaviour; }

    private double getAngle(double angle)
    {
        return switch (rotation)
        {
            case ZERO -> 0;
            case HALF -> (angle + Math.PI/2) % Math.PI - Math.PI/2;
            case FREE -> angle;
        };
    }

    public void paint(Graphics2D g)
    {
        Vector2 center = Objects.requireNonNullElse(center(), position != null ? new Vector2(position): Vector2.zero());
        Vector2 direction = Objects.requireNonNullElse(direction(), Vector2.ihat());
        double angle = direction.angle();

        Graphics2D g2 = (Graphics2D)g.create();
        g2.setFont(font.get(GraphicFont.class).font());
        FontMetrics fontMetrics = g2.getFontMetrics();

        g2.translate(center.x, center.y);
        g2.rotate(angle);

        g2.rotate(-angle);
        g2.rotate(getAngle(angle));

        int width = 3*fontMetrics.stringWidth(value)/2;
        int height = fontMetrics.getHeight();
        g2.setColor(Color.WHITE);
        g2.fillRect(-width/2, -height/2, width, height);

        Point p = TextStroke.center(value, fontMetrics, 0, 0);
        g2.setColor(color.get(GraphicColor.class).color());
        g2.drawString(value, p.x, p.y);
    }
}
