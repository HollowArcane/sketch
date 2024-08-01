package bin.sketch.snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bin.util.Arrays;

public class Snake
{
    public static enum Direction {
        UP, LEFT, RIGHT, DOWN;

        public static boolean opposed(Direction d1, Direction d2)
        {
            return d1 == UP && d2 == DOWN
                || d1 == DOWN && d2 == UP
                || d1 == LEFT && d2 == RIGHT
                || d1 == RIGHT && d2 == LEFT;
        }
    }

    private Direction direction;
    private ArrayList<Body> tail;
    private int cellSize;

    public Snake(int x, int y, int cellSize)
    {
        direction = Direction.DOWN;
        this.cellSize = cellSize;
        buildTail(x, y, 3);
    }

    private void buildTail(int x, int y, int initialSize)
    {
        tail = new ArrayList<>();
        for(int i = 0; i < initialSize; i++)
        {
            tail.add(new Body(x, y));
            
            x = switch(direction) {
                case UP -> x;
                case DOWN -> x;
                case LEFT -> x+1;
                case RIGHT -> x-1;
            };
            y = switch(direction) {
                case UP -> y+1;
                case DOWN -> y-1;
                case LEFT -> y;
                case RIGHT -> y;
            };
        }
    }

    public void setDirection(Direction newDirection)
    {
        if(!Direction.opposed(direction, newDirection))
        { this.direction = newDirection; }
    }

    public void update(int rows, int cols)
    {
        Body first = tail.getFirst();
        Body last = tail.removeLast();

        switch (direction)
        {
            case UP -> last.moveTo(first.x % rows, (first.y + cols - 1) % cols);
            case LEFT -> last.moveTo((first.x + rows - 1) % rows, first.y % cols);
            case DOWN -> last.moveTo(first.x % rows, (first.y + 1) % cols);
            case RIGHT -> last.moveTo((first.x + 1) % rows, first.y % cols);
        }
        tail.add(0, last);
    }

    public boolean dies()
    {
        Body head = tail.getFirst();
        return Arrays.some(tail, b -> b != head && b.x == head.x && b.y == head.y);
    }

    public boolean eats(Food f)
    {
        Body head = tail.getFirst();
        if(f.x() != head.x || f.y() != head.y)
        { return false; }

        Body last = tail.getLast();
        tail.add(new Body(last.x, last.y));
        return true;
    }

    public void paint(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        for(Body b: tail)
        { b.paint(g, cellSize); }
    }

    private static class Body
    {
        int x, y;
        public Body(int x, int y)
        { moveTo(x, y); }

        public void moveTo(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public void paint(Graphics2D g, int cellSize)
        { g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize); }
    }
}
