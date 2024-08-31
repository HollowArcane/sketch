package bin.sketch.marble;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import bin.util.geometry.Line;
import bin.util.geometry.Vector2;
import bin.util.paint.Palette;
import bin.view.processing.Canvas;

public class PaperMarbling
{
    public ArrayList<Ink> inks;
    private Palette palette;

    public PaperMarbling()
    {
        inks = new ArrayList<>();
        palette = Palette.ofHues(10, 1f, 1f);
    }

    public void addInk(double x, double y, double radius)
    {
        Ink newInk = new Ink(x, y, radius, palette.current());

        for (Ink ink : inks)
        { marble(newInk, ink); }

        inks.add(newInk);
    }

    public void tineLine(Line line, double z, double c)
    {
        for (Ink ink : inks)
        { tine(ink, line, z, c); }
    }

    public void changeColor()
    { palette.next(); }

    private void marble(Ink newInk, Ink ink)
    {
        for(Vector2 vertex: ink.vertices)
        {
            // formula: C + (P - C)*sqrt(1 + r**2 / |P - C|**2)
            Vector2 C = newInk.center;
            Vector2 PsubC = vertex.copy().sub(C);
            double r = newInk.radius;
            
            vertex.set(PsubC.scale(Math.sqrt(1 + (r*r)/PsubC.mag2())).add(C));
        }
    }

    public void tine(Ink ink, Line line, double z, double c)
    {
        // vertical tineline: (x, y + z*u**(x - x**c)) where u = (1/2)**(1/c)
        // any tineline: P + z * u**d where d = |(P - B)*N| where N is the normal to l
        double u = Math.pow(.5, 1/c);        

        for(Vector2 vertex: ink.vertices)
        { vertex.add(line.direction().copy().scale(z * Math.pow(u, Math.abs(vertex.copy().sub(line.origin()).dot(line.direction().copy().normal()))))); }
    }

    public void draw(Canvas canvas, Graphics g)
    {
        for (Ink ink : inks)
        { ink.paint(g); }
    }

    public void addRandomInk(int width, int height)
    {
        changeColor();
        Random random = new Random();
        addInk(random.nextInt(width), random.nextInt(height), random.nextInt(10, 100));
    }

    public void addFloralInk(int i, int width, int height)
    {
        double angle = i*97*Math.PI/180;
        addInk(width/2 + 7*i*Math.cos(angle), height/2 + 7*i*Math.sin(angle), 10*Math.sqrt(i));
    }
}