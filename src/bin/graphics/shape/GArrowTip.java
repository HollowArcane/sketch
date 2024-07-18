package bin.graphics.shape;

import java.awt.Graphics2D;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicStroke;
import bin.util.geometry.Vector2;

public class GArrowTip extends GPin
{
    float length;
    float angle;

    public GArrowTip(float length, float angle)
    {
        this.length = length;
        this.angle = angle;
    }

    public GArrowTip(float length)
    { this(length, (float)Math.PI/6); }

    public GArrowTip()
    { this(10f); }

    public void paint(Graphics2D g)
    {
        Vector2 center = center();
        Vector2 direction = direction();
        GPath path = path();

        if(center != null && direction != null && path != null)
        {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(center.x, center.y);
            g2.rotate(direction.angle());

            g2.setColor(path.color.get(GraphicColor.class).color());
            g2.setStroke(path.stroke.get(GraphicStroke.class).stroke());

            Vector2 v = Vector2.polar(angle, length);
            g2.drawLine(0, 0, -(int)v.x,  (int)v.y);
            g2.drawLine(0, 0, -(int)v.x, -(int)v.y);
        }
    }
}
