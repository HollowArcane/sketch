package bin.run;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bin.graphics.animation.Animation;
import bin.graphics.property.GraphicTrace;
import bin.graphics.property.Property;
import bin.graphics.shape.GLine;
import bin.sketch.flow.Flow;
import bin.view.processing.Canvas;

public class RunGraphOptimisation
{
    private static int width = 1280;
    private static int height = 720;

    static void main()
        throws IOException
    {
        Flow flow = Flow.read("graph", width, height, 100);
        
        GLine line = new GLine(new Point(width/3, height/3), new Point(2*width/3, 2*height/3));
        line.getProperty(GLine.PROPERTY_TRACE).
             from(new GraphicTrace(0))
            .with(Animation.BEHAVIOUR_CUBIC)
            .in(2000)
            .create();

        Canvas canvas = new Canvas(width, height);
        canvas.draw(g -> line.paint((Graphics2D)g));
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println(flow.maximum()));

        executor.shutdown();

        canvas.display("Flot maximale");
    }
}
