package bin.graphics.property;

import java.awt.Font;

import bin.util.Maths;

public class GraphicFont extends Property
{
    Font font;
    int size;

    public GraphicFont(int size)
    {
        this.size = size;
        this.font = new Font("sansserif", Font.BOLD, size);
    }

    public Font font()
    { return font; }

    @Override
    public Property interpolate(Property end, float progression)
    {
        if(end instanceof GraphicFont f)
        { return new GraphicFont((int)Maths.lerp(progression, size, f.size)); }
        
        throw new IllegalArgumentException("Expecting GraphicFont: " + end);
    }

    @Override
    public String toString()
    { return "GraphicFont [font=" + font + ", size=" + size + "]"; }

    @Override
    protected void setCurrentValues(Property p)
    {
        if(p instanceof GraphicFont gf)
        {
            font = gf.font;
            size = gf.size;
        }
    }   
}
