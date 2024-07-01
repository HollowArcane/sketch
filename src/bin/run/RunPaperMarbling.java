package bin.run;

import java.awt.Color;


import bin.sketch.marble.PaperMarbling;
import bin.view.processing.Canvas;

public class RunPaperMarbling
{
    private static int width = 1920;
    private static int height = 1080;
    private static Canvas canvas = new Canvas(width, height);

    public static void main(String[] args)
    {
        PaperMarbling paper = new PaperMarbling();
        canvas.setBackground(Color.BLACK);

        canvas.mouse().release(e -> paper.changeColor());
        canvas.mouse().press(e -> paper.addInk(e.x(), e.y(), 50));
        
        canvas.mouse().drag(e -> {
            paper.addInk(e.x(), e.y(), 50);
            // paper.tineLine(new Line(new Vector2(e.x(), e.y()), new Vector2(e.dx(), e.dy())), e.speed()/10, 8);
        });

        canvas.draw((g) -> {
            // paper.changeHue();
            // paper.addRandomInk(width, height);
            // paper.addFloralInk(i++, width, height);
            paper.draw(canvas, g);
        });

        canvas.display("Paper Marbling");
    }
}
