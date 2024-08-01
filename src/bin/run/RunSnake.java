package bin.run;

import java.awt.Color;
import java.awt.event.KeyEvent;

import bin.sketch.snake.Field;
import bin.sketch.snake.Snake;
import bin.sketch.snake.Snake.Direction;
import bin.view.processing.Canvas;

public class RunSnake
{
    static int width;
    static int height;
    static Canvas canvas;

    static Field field;

    public static void main(String[] args)
    {
        width = 1280;
        height = 720;

        canvas = new Canvas(width, height);
        canvas.setBackground(Color.BLACK);
        
        field = new Field(width, height, 20); 
        canvas.keys().press(keys -> {
            Snake snake = field.getSnake();
            if(keys.key(KeyEvent.VK_LEFT))
            { snake.setDirection(Direction.LEFT); }

            if(keys.key(KeyEvent.VK_RIGHT))
            { snake.setDirection(Direction.RIGHT); }

            if(keys.key(KeyEvent.VK_UP))
            { snake.setDirection(Direction.UP); }
            
            if(keys.key(KeyEvent.VK_DOWN))
            { snake.setDirection(Direction.DOWN); }
        });

        canvas.draw(g -> {
            try
            { Thread.sleep(100); }
            catch (Exception e) {}

            field.update();
            field.paint(g);
        });  
        canvas.display("Snake");
    }    
}
