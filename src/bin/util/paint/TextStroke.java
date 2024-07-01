package bin.util.paint;

import java.awt.FontMetrics;
import java.awt.Point;

public class TextStroke
{
    public static Point center(String text, FontMetrics metrics, int cx, int cy)
    {
        return new Point(
            cx - metrics.stringWidth(text)/2,
            cy + metrics.getHeight()/2 - metrics.getDescent()
        );
    }
}
