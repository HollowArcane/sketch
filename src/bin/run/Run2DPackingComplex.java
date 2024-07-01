package bin.run;

import java.util.Random;

import javax.swing.JPanel;

import bin.sketch.packing.EntryPanelComplex;
import bin.sketch.packing.ShapeBox2D;
import bin.view.Window;
import bin.view.processing.Canvas;

public class Run2DPackingComplex
{
    // canvas to draw the sketch on 
    private static Canvas canvas;

    // number of shapes to fit
    private static int n;

    // box containing the shapes to fit in
    private static ShapeBox2D box;

    // width of the canvas
    private static int width;
    // height of the canvas
    private static int height;

    // insertion panel
    private static JPanel entryPanel;

    public static void main(String[] args)
    {
        init();
        display();
    }
    
    private static void display()
    {
        Window frame = canvas.display("2D Packing");
        entryPanel = new EntryPanelComplex(frame, canvas, box);

        canvas.mouse().click(e -> {
            frame.setContentPane(entryPanel);
            frame.revalidate();
        });
    }

    private static void init()
    {
        width = 1280;
        height = 720;
        
        canvas = new Canvas(width, height);

        n = 3;
        box = new ShapeBox2D(1);
    }
}
