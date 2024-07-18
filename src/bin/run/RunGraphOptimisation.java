package bin.run;

import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bin.sketch.flow.Flow;
import bin.view.processing.Canvas;

public class RunGraphOptimisation
{
    private static int width = 1280;
    private static int height = 720;

    static void main()
        throws IOException
    {
        Flow flow = Flow.read("graph", width, height, 0);
        Canvas canvas = new Canvas(width, height);

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println(flow.maximum()));
        executor.shutdown();

        canvas.mouse().press(mouse -> mouse.record(flow.getPoint(mouse.x(), mouse.y())));
        canvas.mouse().release(mouse -> mouse.unrecord());
        canvas.mouse().drag(mouse -> {
            if(mouse.record() instanceof Point p)
            {
                p.x += mouse.dx();
                p.y += mouse.dy();
                flow.moveLines(p);
            }
        });

        canvas.display("Flot maximale");
        canvas.draw(flow::paint);
    }
}
