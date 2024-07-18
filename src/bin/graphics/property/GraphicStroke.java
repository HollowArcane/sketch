package bin.graphics.property;

import java.awt.BasicStroke;
import java.util.Arrays;

import bin.util.Maths;

public class GraphicStroke extends Property
{
    float width;
    float dashPhase;

    float[] dash;

    public GraphicStroke(float width, float[] dash, float dashPhase)
    {
        this.width = width;
        this.dash = dash;
        this.dashPhase = dashPhase;
    }

    public GraphicStroke(float width)
    { this(width, new float[] { 1f }, 0f); }

    public GraphicStroke(float width, float[] dash)
    { this(width, dash, 0f); }

    public GraphicStroke(float width, float dashPhase)
    { this(width, new float[] { 1f }, dashPhase); }

    public BasicStroke stroke()
    { return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, dash, dashPhase); }

    @Override
    public Property interpolate(Property end, float progression)
    {
        if(end instanceof GraphicStroke gs)
        { return new GraphicStroke(
            (float)Maths.lerp(progression, this.width, gs.width),
            interpolateDash(progression, dash, gs.dash),
            (float)Maths.lerp(progression, this.dashPhase, gs.dashPhase)
        ); }
        
        throw new IllegalArgumentException("Expecting GraphicsStoke: " + end);
    }

    private float[] interpolateDash(float progression, float[] d1, float[] d2)
    {
        float[] d = new float[Math.max(d1.length, d2.length)];

        for (int i = 0; i < d.length; i++)
        {
            d[i] = (float)Maths.lerp(progression,
                d1.length > i ? d1[i]: 0,
                d2.length > i ? d2[i]: 0
            );
        }
        return d;
    }

    @Override
    public String toString()
    { return "GraphicStroke [width=" + width + ", dashPhase=" + dashPhase + ", dash=" + Arrays.toString(dash) + "]"; }

    @Override
    protected void setCurrentValues(Property p)
    {
        if(p instanceof GraphicStroke gs)
        {
            width = gs.width;
            dashPhase = gs.dashPhase;
            dash = gs.dash;
        }
    }
}
