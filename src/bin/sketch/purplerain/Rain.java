package bin.sketch.purplerain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bin.util.Arrays;

public class Rain
{
    public static final Color COLOR_BACKGROUND = null;
    public static final Color COLOR_RAIN = null;

    private ArrayList<Droplet> drops;
    private int height;
    
    public Rain(int width, int height, int n)
    {
        drops = Arrays.of(n, () -> Droplet.random(width, height, 10, 100, .5f)); 
        this.height = height;
    }

    public void update()
    {
        for (Droplet droplet : drops)
        { droplet.update(height); }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        for (Droplet droplet : drops)
        { droplet.paint(g2, COLOR_RAIN); }
    }
}
