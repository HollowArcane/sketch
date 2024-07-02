package bin.run;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import bin.sketch.packing.Box1D;
import bin.sketch.packing.EntryPanelSimple;
import bin.sketch.packing.ShapeBox2D;
import bin.util.Arrays;
import bin.util.geometry.Circle;
import bin.util.geometry.Parallelogram;
import bin.util.geometry.Shape;
import bin.util.geometry.Vector2;
import bin.util.geometry.Triangle;
import bin.view.Window;
import bin.view.processing.Canvas;
import bin.view.processing.Canvas.CanvasHandler;

public class Run2DPackingSimple
{
    // canvas to draw the sketch on 
    private static Canvas canvas;

    // box containing the shapes to fit in
    private static ShapeBox2D box;

    // width of the canvas
    private static double width;
    // height of the canvas
    private static double height;

    // insertion panel
    private static JPanel entryPanel;

    // draw function;
    private static CanvasHandler draw = g -> box.draw(g);

    private static WindowListener window;

    public static void main(String[] args)
    {   
        init();
        display();
    }
    
    private static void display()
    {
        Window frame = canvas.display("2D Packing");
        frame.addWindowListener(window);
        entryPanel = new EntryPanelSimple(width, height, frame, canvas, box);

        canvas.mouse().click(e -> {
            frame.setContentPane(entryPanel);
            frame.revalidate();
            frame.pack();
        });
    }

    private static void init()
    {
        width = 1280;
        height = 720;
        
        canvas = new Canvas((int)width, (int)height);
        canvas.setBackground(Color.BLACK);
        canvas.draw(draw);


        // Exercice 2 : =================================================
        /* size = 150
         * 2.3.2,1: (100, 125, 25, 50)
         * 2.3.2,2: (25, 50, 100, 125)
         * 2.3.2,3: (125, 50, 25, 100)
         */

        Box1D b = new Box1D( 150, 100, 125, 25, 50);
        b.fitBruteForce();
        System.out.println(b);
        b = new Box1D(150, 25, 50, 100, 125);
        b.fitBruteForce();
        System.out.println(b);
        b = new Box1D(150, 125, 50, 25, 100);
        b.fitBruteForce();
        System.out.println(b);


        // Exercice 3 : =================================================
        /* 3.4.3,1 
         * (100, 400)
         * (1200, 300)
         * (100, 200)
         * 
        /* 3.4.3,2
         * (250, 400)
         * (500, 300)
         * (750, 299)
         * (1000, 298)
         */
        /* 3.4.3,3
         * (250, 400)
         * (500, 300)
         * (750, 299)
         * (1000, 298)
         */
        
        int n = 100;
        Random random = new Random();

        ArrayList<Shape> shapes = Arrays.fill(new ArrayList<Shape>(), n/3, i -> Parallelogram.rectangle(
            Vector2.zero(),
            random.nextDouble(50, 200),
            random.nextDouble(30, 60)
        ));

        Arrays.fill(shapes, n/3, i -> new Circle(
            Vector2.zero(),
            random.nextDouble(30, 60)
        ));

        Arrays.fill(shapes, n/3, i -> Triangle.isocelis(
            Vector2.zero(),
            random.nextDouble(50, 200),
            random.nextDouble(30, 60)
        ));

        box = new ShapeBox2D(1, shapes.toArray(Shape[]::new));
        // box = new ShapeBox2D(1, Arrays.fill(new Shape[n], i -> Parallelogram.rectangle(
        //     Vector2.zero(),
        //     random.nextDouble(50, 200),
        //     random.nextDouble(30, 60)
        // )));

        // box = new ShapeBox2D(4,
        //     Parallelogram.rectangle(Vector2.zero(), 25, 25),
        //     Parallelogram.rectangle(Vector2.zero(), 24, 24),
        //     // Parallelogram.rectangle(Vector2.zero(), 19, 23),
        //     Parallelogram.rectangle(Vector2.zero(), 29, 22)
        // );

        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.execute(() -> box.fitBFDH(width, height, 100));

        exec.shutdown();

        window = new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                exec.shutdownNow(); 
                System.out.println("WINDOW CLOSED");
            }
        };
    }
}
