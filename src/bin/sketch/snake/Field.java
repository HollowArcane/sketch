package bin.sketch.snake;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Field
{
    int rows, cols, cellSize;

    Snake player;
    Food food;

    public Field(int width, int height, int cellSize)
    {
        rows = width / cellSize;
        cols = height / cellSize;
        this.cellSize = cellSize;
        player = new Snake(rows / 2, cols / 2, cellSize);
        food = Food.random(rows, cols, cellSize);
    }

    public Snake getSnake()
    {return player; }

    public void update()
    {
        player.update(rows, cols);

        if(player.eats(food))
        { food = Food.random(rows, cols, cellSize); } 

        if(player.dies())
        { player = new Snake(rows / 2, cols / 2, cellSize); }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        player.paint(g2);
        food.paint(g2);
    }
}
