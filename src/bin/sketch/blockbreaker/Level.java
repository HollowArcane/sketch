package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Level
{
    public static final int PLATFORM_WIDTH = 200;
    public static final int PLATFORM_HEIGHT = 10;

    public static final int BRICK_WIDTH = 60;
    public static final int BRICK_HEIGHT = 30;

    public static final int BRICK_ROW_COUNT = 10;
    public static final int BRICK_COLUMN_COUNT = 15;
    public static final int BRICK_GAP = 5;

    public static final int BALL_SIZE = 5;

    private Ball ball;
    private Platform platform;

    private ArrayList<Brick> bricks;

    private int width, height;

    public Level(int width, int height)
    {
        this.width = width;
        this.height = height;
        bricks = new ArrayList<>();

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

        ball = new Ball(width / 2, height / 2, BALL_SIZE);
        platform = new Platform((width - PLATFORM_WIDTH)/2, height - 10*PLATFORM_HEIGHT, PLATFORM_WIDTH, PLATFORM_HEIGHT);
    }

    public Platform getPlatform()
    { return platform; }

    public void update()
    { ball.update(platform, bricks, width, height); }

    public void paint(Graphics2D g)
    {
        ball.paint(g);
        platform.paint(g);

        for (Brick brick : bricks)
        { brick.paint(g); }
    }
}
