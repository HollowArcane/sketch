package bin.sketch.snake;

import java.awt.Color;
import java.awt.Graphics2D;

public record Food(int x, int y, int cellSize)
{
    public void paint(Graphics2D g)
    {
        g.setColor(Color.RED);
        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public static Food random(int rows, int cols, int cellSize)
    { return new Food((int)(Math.random() * rows), (int)(Math.random() * cols), cellSize); }
}