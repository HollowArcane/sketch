package bin.sketch.packing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import bin.util.geometry.Circle;
import bin.util.geometry.FiniteShape;
import bin.util.geometry.Polygon;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;
import bin.util.paint.TextStroke;

public class ShapeItem2D
{
    private FiniteShape shape;
    private String name;
    private boolean fit;

    private Color color;

    public ShapeItem2D(FiniteShape shape, String name)
    {
        this.shape = shape;
        this.name = name;
        this.fit = false;
        color = Color.getHSBColor((float)Math.random(), .8f, .9f);
    }

    public FiniteShape getShape()
    { return shape; }

    public void setFit(boolean fit)
    { this.fit = fit; }

    public void draw(int scale, Graphics g)
    {
        if(!fit)
        { return; }

        g.setColor(color);
        g.setFont(new Font("sansserif", Font.BOLD, 24));

        Vector2 center = shape.getCenter();
        Point txtPosition = TextStroke.center(name, g.getFontMetrics(), scale*(int)center.x, scale*(int)center.y);

        if(shape instanceof Circle c)
        {
            PaintUtilities.fillCircle(g, scale*(int)c.center().x, scale*(int)c.center().y, scale*(int)c.radius);
            g.setColor(Color.WHITE);
            g.drawString(name, txtPosition.x, txtPosition.y);
        }
        else if(shape instanceof Polygon p)
        {
            Path2D.Float path = p.path();
            path.transform(new AffineTransform(new float[] { scale, 0, 0, scale, 0, 0 }));

            ((Graphics2D)g).fill(path);
            g.setColor(Color.WHITE);
            g.drawString(name, txtPosition.x, txtPosition.y);
        }
    }

    @Override
    public String toString()
    { return "ShapeItem [shape=" + shape + ", name=" + name + ", fit=" + fit + "]"; }
}
