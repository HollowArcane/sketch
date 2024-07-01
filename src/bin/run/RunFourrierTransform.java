package bin.run;

import java.awt.geom.Path2D;
import java.util.ArrayList;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;

import bin.sketch.fourrier.FourrierTransform;
import bin.util.himath.HiComplex;
import bin.view.event.Mouse.MouseHandler;
import bin.view.processing.Canvas;
import bin.view.processing.Canvas.CanvasHandler;

public class RunFourrierTransform
{
    private static Canvas canvas;
    private static ArrayList<HiComplex> points;

    private static FourrierTransform transform;

    private static boolean drawFourrier;
    private static boolean loadFourrier;

    private static float ti, tf, dt;
    private static int detail;

    private static MouseHandler press = mouse ->
    {
        if(mouse.details().getClickCount() >= 2)
        { drawFourrier = !drawFourrier; }
    };

    private static MouseHandler drag = mouse ->
    {
        points.add(new HiComplex(mouse.x(), mouse.y()));
        loadFourrier = true;
    };

    public static MouseHandler wheel = mouse ->
    {
        double rotation = ((MouseWheelEvent)mouse.details()).getPreciseWheelRotation();
        if(rotation > 0)
        { detail++; }
        else if(rotation < 0)
        { detail--; }

        transform = new FourrierTransform(detail);
        loadFourrier = true;
    };

    private static CanvasHandler draw = g ->
    {
        if(points.size() == 0)
        { return; }

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(!drawFourrier)
        { ((Graphics2D)g).draw(tracePath(points)); }
        else
        {
            if(loadFourrier)
            {
                transform.read(points.toArray(HiComplex[]::new));
                loadFourrier = false;
            }
            transform.draw(g, tf);
            
            ((Graphics2D)g).setStroke(new BasicStroke(3));
            ((Graphics2D)g).draw(transform.getPath(ti, tf, (float)points.size()/1000000));

            tf += .001f;
            if(tf > 1)
            { ti += dt; }
        }
        
        g.drawString(detail + "", 100, 100);
    };

    private static void init()
    {
        canvas = new Canvas(1280, 720);
        points = new ArrayList<>();
        
        drawFourrier = false;
        loadFourrier = true;
        
        ti = 0;
        tf = 0;
        dt = .001f;
        
        detail = 50;
        transform = new FourrierTransform(detail);
    }

    private static Path2D.Float tracePath(ArrayList<HiComplex> points)
    {
        Path2D.Float path = new Path2D.Float();

        path.moveTo(points.get(0).a, points.get(0).b);
        for(int i = 1; i < points.size(); i++)
        { path.lineTo(points.get(i).a, points.get(i).b); }

        return path;
    }


    public static void main(String[] args)
    {
        init();

        canvas.mouse().wheel(wheel);
        canvas.mouse().press(press);
        canvas.mouse().drag(drag);
        canvas.draw(draw);
        canvas.display("Fourrier Transform");
    }
}
