package bin.sketch.fourrier;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import bin.util.Arrays;
import bin.util.himath.HiComplex;
import bin.util.himath.HiReal;
import bin.util.paint.PaintUtilities;

public class FourrierTransform
{
    HiComplex[] Cn;

    public FourrierTransform(int n)
    { Cn = Arrays.fill(new HiComplex[2*n + 1], () -> HiComplex.zero()); }

    public void read(HiComplex[] function)
    {
        HiComplex dt = new HiReal((double)1/function.length);
        for(int j = 0; j < Cn.length; j++)
        {
            Cn[j] = HiComplex.zero();
            for(int t = 0; t < function.length; t++)
            {
                int n = j - Cn.length/2;
                Cn[j] = HiComplex.add(
                    Cn[j],
                    HiComplex.multiply(
                        function[t],
                        HiComplex.polar(1, n*2*Math.PI * (float)t/function.length),
                        dt
                    )
                );
            }
        }
    }

    public Path2D.Float getPath(float ti, float tf, float dt)
    {
        Path2D.Float path = new Path2D.Float();

        float t = ti;
        HiComplex next = get(t);
        path.moveTo(next.a, next.b);

        for(t = ti + dt; t <= tf; t += dt)
        {
            next = get(t);
            path.lineTo(next.a, next.b);
        }

        return path;
    }

    public void draw(Graphics g, float t)
    {
        HiComplex value = Cn[Cn.length/2];
        for(int j = 1; j <= Cn.length/2; j++)
        {
            value = drawNextValue(g, t, value, Cn.length/2 - j, j);
            value = drawNextValue(g, t, value, Cn.length/2 + j, -j);
        }
    }

    private HiComplex drawNextValue(Graphics g, float t, HiComplex value, int j, int n)
    {
        HiComplex crtValue = HiComplex.multiply(Cn[j], HiComplex.polar(1, n*2*Math.PI * t));
        HiComplex nextValue = HiComplex.add(value, crtValue);

        drawPart(g, value, crtValue, nextValue);
        return nextValue;
    }

    private void drawPart(Graphics g, HiComplex value, HiComplex crtValue, HiComplex nextValue)
    {
        double modulus = crtValue.modulus();
        ((Graphics2D)g).setStroke(new BasicStroke((float)modulus/50));
        
        g.setColor(new Color(200, 200, 200));
        PaintUtilities.drawCircle(g, (int)value.a, (int)value.b, (int)modulus);
        g.setColor(new Color(100, 100, 100));
        g.drawLine((int)value.a, (int)value.b, (int)nextValue.a, (int)nextValue.b);
    }

    public HiComplex get(float t)
    {
        HiComplex value = new HiComplex(0, 0);
        for(int j = 0; j < Cn.length; j++)
        {
            int n = j - Cn.length/2;
            value = HiComplex.add(value, HiComplex.multiply(Cn[j], HiComplex.polar(1, -n*2*Math.PI * t)));
        }
        return value;
    }
}
