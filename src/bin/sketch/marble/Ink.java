package bin.sketch.marble;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import bin.util.Maths;
import bin.util.geometry.Vector2;

public class Ink
{
    private static int size = 400;
    ArrayList<Vector2> vertices;

    Vector2 center;
    double radius;
    private Color color;

    public Ink(double x, double y, double radius, Color color)
    {
        center = new Vector2(x, y);
        this.radius = radius;

        this.color = color;
        vertices = new ArrayList<>(size);
        for(int i = 0; i < size; i++)
        {
            double angle = Maths.map(i, 0, size, 0, 2*Math.PI);
            vertices.add(Vector2.polar(angle, radius).add(x, y));
        }   
    }

    public void paint(Graphics g)
    {
        g.setColor(color);

        Path2D.Float path = tracePath();
        ((Graphics2D)g).fill(path);
    }

    private Path2D.Float tracePath()
    {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(vertices.get(0).x, vertices.get(0).y);

        for (int i = 1; i < size; i++)
        { path.lineTo(vertices.get(i).x, vertices.get(i).y); }

        return path;
    }
}
