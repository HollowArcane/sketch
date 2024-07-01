package bin.util.paint;

import java.awt.Graphics;

public class PaintUtilities
{
    public static void drawPoint(Graphics g, int x, int y)
    { g.fillOval(x - 2, y - 2, 4, 4); }

    public static void drawCircle(Graphics g, int cx, int cy, int radius)
    { g.drawOval(cx - radius, cy - radius, 2*radius, 2*radius); }

    public static void fillCircle(Graphics g, int cx, int cy, int radius)
    { g.fillOval(cx - radius, cy - radius, 2*radius, 2*radius); }
}
