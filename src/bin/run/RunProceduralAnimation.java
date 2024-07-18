package bin.run;

import java.awt.Point;
import java.awt.event.KeyEvent;

import bin.sketch.wire.Body;
import bin.sketch.wire.Joint;
import bin.sketch.wire.Wire;
import bin.util.Arrays;
import bin.util.Maths;
import bin.view.processing.Canvas;

public class RunProceduralAnimation
{
    static Body snake;

    public static void main(String[] args)
    {
        int n = 50;
        int maxSize = 25, minSize = 10;
        Integer[] sizes = Arrays.fill(new Integer[n], i -> (int)Maths.map(i, 0, n, maxSize, minSize));
        sizes[0] = maxSize;
        sizes[1] = maxSize + 5;
        sizes[2] = maxSize - 3;
        // Integer[] sizes = { 50, 50 };
        snake = Body.read(new Point(300, 300), sizes);

        Canvas c = new Canvas(1280, 720);
        c.mouse().drag(mouse -> snake.move(mouse.dx(), mouse.dy()));
        c.keys().press(keys -> {
            if(keys.key(KeyEvent.VK_A))
            { snake.tension -= .1f; }
            
            if(keys.key(KeyEvent.VK_D))
            { snake.tension += .1f; }
        });
        
        c.draw(snake::paint);
        c.display("Procedural Animation");
    }
}
