package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;

import bin.util.geometry.Parallelogram;
import bin.util.geometry.Vector2;

public class Bonus
{
    public static enum Type
    { X2, SIZE }

    private Vector2 vecolity;

    private Parallelogram shape;
    private Color color;
    private Type type;

    public Bonus(int x, int y, int width, int height, Color color, Type type)
    {
        vecolity = new Vector2(0, 3);
        shape = Parallelogram.rectangle(new Vector2(x + width/2, y + height/2), width, height);
        this.color = color;
        this.type = type;
    }

    public Type getType()
    { return type; }

    public int x()
    { return (int)(shape.getCenter().x - shape.getBase() / 2); }

    public int y()
    { return (int)(shape.getCenter().y - shape.getHeight() / 2); }

    public int width()
    { return (int)shape.getBase(); }

    public int height()
    { return (int)shape.getHeight(); }

    public Parallelogram getShape()
    { return shape; }

    public void update(Platform platform)
    {
        shape.translate(vecolity);

        if(shape.intersects(platform.getShape()))
        { platform.granted(this); }
    }

    public void paint(Graphics2D g)
    {
        g.setColor(color);
        g.fillOval(x(), y(), width(), height());
    }
}
