package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bin.util.Arrays;

public class Level
{
    public static final int PLATFORM_WIDTH = 200;
    public static final int PLATFORM_HEIGHT = 10;

    public static final int BRICK_WIDTH = 60;
    public static final int BRICK_HEIGHT = 30;

    public static final int BRICK_ROW_COUNT = 10;
    public static final int BRICK_COLUMN_COUNT = 15;
    public static final int BRICK_GAP = 5;

    public static final int BONUS_WIDTH = 60;
    public static final int BONUS_HEIGHT =30;

    public static final int BALL_SIZE = 5;

    private ArrayList<Ball> balls;
    private Platform platform;

    private ArrayList<Brick> bricks;
    private ArrayList<Bonus> boni;

    private int width, height;

    public Level(int width, int height)
    {
        this.width = width;
        this.height = height;
        bricks = new ArrayList<>();
        boni = new ArrayList<>();

        for(int i = 0; i < BRICK_ROW_COUNT; i++)
        {
            for(int j = 0; j < BRICK_COLUMN_COUNT; j++)
            {
                bricks.add(new Brick(
                    (width + BRICK_GAP - BRICK_COLUMN_COUNT * (BRICK_WIDTH + BRICK_GAP)) / 2  + j * (BRICK_WIDTH + BRICK_GAP),
                    20 + i * (BRICK_HEIGHT + BRICK_GAP),
                    BRICK_WIDTH, BRICK_HEIGHT, Color.BLUE
                ));
            }
        }

        balls = new ArrayList<>();
        addBall(new Ball(width / 2, height / 2, BALL_SIZE));
        
        platform = new Platform((width - PLATFORM_WIDTH)/2, height - 10*PLATFORM_HEIGHT, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        platform.setLevel(this);
    }

    public void addBall(Ball ball)
    {
        balls.add(ball);
        ball.setLevel(this);
    }

    public ArrayList<Ball> getBalls()
    { return balls; }

    public void removeBonus(Bonus b)
    { boni.remove(b); }

    public void addBonus(Bonus b)
    { boni.add(b); }

    public Platform getPlatform()
    { return platform; }

    public void update()
    {
        for(Ball b: balls)
        { b.update(platform, bricks, width, height); }

        for(int i = boni.size() - 1; i >= 0; i--)
        { boni.get(i).update(platform); }

        for(int i = 0; i < boni.size(); i++)
        {
            if(boni.get(i).y() > height)
            { boni.remove(i--); }
        }

        for(int i = 0; i < balls.size(); i++)
        {
            if(balls.get(i).y() > height)
            { balls.remove(i--); }
        }
    }

    public void paint(Graphics2D g)
    {
        for(Ball b: balls)
        { b.paint(g); }

        platform.paint(g);

        for (Brick brick : bricks)
        { brick.paint(g); }

        for (Bonus bonus : boni)
        { bonus.paint(g); }
    }
}
