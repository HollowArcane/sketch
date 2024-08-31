package bin.sketch.voronoi;

import java.util.ArrayList;
import java.util.HashMap;

import bin.util.Arrays;
import bin.util.geometry.Line;
import bin.util.geometry.Parabola;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;
import bin.view.processing.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;

public class VoronoiSweep
{
    // cursor pointing to the next expected site
    private int cursor, direction;
    // cloud of sites
    private ArrayList<Vector2> cloud;
    // associated array of (site, parabolas)
    private HashMap<Vector2, Parabola> parabolas;

    // swipe utils
    private Line sweepLine;
    private Vector2 swipeSpeed;

    // size of the bounding box
    private int width, height;

    public VoronoiSweep(int width, int height)
    {
        this.width = width;
        this.height = height;

        cursor = 0;
        direction = 1;

        // generate a cloud of points
        cloud = Arrays.of(20, () -> Vector2.random(width, height));
        // sort points by x value
        cloud.sort((p1, p2) -> Double.compare(p1.x, p2.x));

        // hashmap to store parabolas associated to the seeds
        parabolas = new HashMap<>();

        // Sweep
        sweepLine = new Line(new Vector2(0, 0), new Vector2(0, 1));
        swipeSpeed = new Vector2(2, 0);
    }

    public void draw(Canvas canvas, Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        // paint
        paint(g2);

        // update
        if(canvas.keys().key(KeyEvent.VK_LEFT))
        { updateLeft(); }

        if(canvas.keys().key(KeyEvent.VK_RIGHT))
        { updateRight(); }
    }

    private void updateRight()
    {
        if(direction == -1)
        {
            cursor++;
            direction = 1;
        }
        sweepLine.translate(swipeSpeed.copy());
        if(cursor < cloud.size())
        {
            // check if sweepLine passes through next seed
            Vector2 seed = cloud.get(cursor);
            if(seed.x < sweepLine.origin().x)
            {
                // create parabola associated to that seed
                parabolas.put(seed, new Parabola(seed, sweepLine));
                // point next seed
                cursor++;
            }
        }
    }

    private void updateLeft()
    {
        if(direction == 1)
        {
            cursor--;
            direction = -1;
        }
        sweepLine.translate(swipeSpeed.copy().scale(-1));
        if(cursor >= 0)
        {
            // check if sweepLine passes through next seed
            Vector2 seed = cloud.get(cursor);
            if(seed.x > sweepLine.origin().x)
            {
                // create parabola associated to that seed
                parabolas.remove(seed);
                // point next seed
                cursor--;
            }
        }
    }

    private void paint(Graphics2D g)
    {
        g.setColor(Color.BLUE);
        for (Vector2 point : cloud)
        { PaintUtilities.drawPoint(g, (int)point.x, (int)point.y); }

        g.setColor(new Color(0, 150, 50));
        g.drawLine((int)sweepLine.origin().x, 0, (int)sweepLine.origin().x, height);
        
        g.setColor(Color.RED);
        g.draw(traceBeach());
    }

    private Path2D.Float traceBeach()
    {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(sweepLine.origin().x, 0);

        for(int t = 0; t < height; t++)
        {
            Vector2 p0 = Vector2.zero();
            for(Parabola parabola: parabolas.values())
            {
                Vector2 p1 = parabola.get(t);
                if(p1.x > p0.x)
                { p0 = p1; }
            }
            path.lineTo(p0.x, p0.y);
        }
        return path;
    }
}
