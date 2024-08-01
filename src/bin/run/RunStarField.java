package bin.run;

import java.awt.Color;

import bin.sketch.starfield.StarField;
import bin.util.Maths;
import bin.view.processing.Canvas;

public class RunStarField
{
    private Canvas canvas;
    private int width, height, n;

    private StarField field;

    void main()
    {
        width = 1000;
        height = 1000;
        n = 1000;
        
        canvas = new Canvas(width, height);
        canvas.setBackground(Color.BLACK);
        canvas.mouse().move(mouse -> field.setSpeed((int)Maths.map(mouse.x(), 0, width, 1, 10)));
        field = new StarField(n, width, height);

        canvas.draw(field::paint);
        canvas.display("Starfield");
    }
}
