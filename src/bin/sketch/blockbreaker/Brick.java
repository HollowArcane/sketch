package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;

import bin.util.geometry.Parallelogram;
import bin.util.geometry.Vector2;

public class Brick
{
    private Parallelogram shape;
    private Color color;

    public Brick(int x, int y, int width, int height, Color color)
    {
        shape = Parallelogram.rectangle(new Vector2(x + width/2, y + height/2), width, height);
        this.color = color;
    }

    public int x()
    { return (int)(shape.center().x - shape.getBase() / 2); }

    public int y()
    { return (int)(shape.center().y - shape.getHeight() / 2); }

    public int width()
    { return (int)shape.getBase(); }

    public int height()
    { return (int)shape.getHeight(); }

    public Parallelogram getShape()
    { return shape; }

    public void paint(Graphics2D g)
    {
        g.setColor(color);
        g.fillRect(x(), y(), width(), height());
    }
}
