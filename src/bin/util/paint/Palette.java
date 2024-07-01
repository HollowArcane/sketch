package bin.util.paint;

import java.awt.Color;

import bin.util.Arrays;

public class Palette
{
    Color[] colors;
    int colorChoice = 0;

    public Palette(Color... colors)
    { this.colors = colors; }

    public static Palette ofHues(int size, float saturation, float brightness)
    {
        Color[] colors = new Color[size];
        for(int i = 0; i < size; i++)
        { colors[i] = Color.getHSBColor((float)i/size, saturation, brightness); }

        return new Palette(colors);
    }

    public Color[] getColors()
    { return colors; }

    public Color next()
    { return colors[++colorChoice % colors.length]; }

    public Color current()
    { return colors[colorChoice]; }

    public Color random()
    { return Arrays.random(colors); }
}
