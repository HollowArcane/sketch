package bin.run;

import java.awt.Color;
import java.awt.Graphics2D;

import bin.util.geometry.Circle;
import bin.util.geometry.Polygon;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;
import bin.view.processing.Canvas;
import bin.view.processing.Canvas.CanvasHandler;

public class RunCirclePolygonCollision
{
    private Circle c;
    private Polygon p;

    private Canvas canvas;
    public void main(String[] args)
    {
        c = new Circle(new Vector2(0, 0), 40);
        p = new Polygon(
            new Vector2(100, 100),
            new Vector2(400, 100),
            new Vector2(300, 400),
            new Vector2(200, 400)
        );

        canvas = new Canvas(1280, 720);
        
        canvas.mouse().move(mouse -> c.center().set(mouse.x(), mouse.y()));
        
        canvas.draw(g -> {
            if(c.intersects(p))
            { g.setColor(Color.getHSBColor(0f, .7f, 1f)); }
            else
            { g.setColor(Color.getHSBColor(.6f, .7f, 1f)); }
            
            ((Graphics2D)g).fill(p.path());

            g.setColor(Color.BLACK);
            PaintUtilities.fillCircle(g, (int)c.center().x, (int)c.center().y, (int)c.radius);
        });

        canvas.display("Circle Polygon Collision");
    }    
}
