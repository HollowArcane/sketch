package bin.sketch.flow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import bin.util.algebra.Fraction;
import bin.util.algebra.Matrix;
import bin.util.algorithm.Fulkerson;
import bin.util.paint.PaintUtilities;
import bin.util.paint.TextStroke;

public class Flow
{
    private static final int NODE_SIZE = 15;
    
    private int sleep;
    private Matrix graph;
    private ArrayList<Point> points;

    public Flow(Matrix graph, int width, int height, int sleep)
    {
        this.graph = graph;
        this.sleep = sleep;

        Random random = new Random();

        points = new ArrayList<>();
        for(int i = 0; i < graph.getRowCount(); i++)
        { points.add(new Point(random.nextInt(width), random.nextInt(height))); }
    }

    public static Flow read(String filename, int width, int height, int sleep)
        throws IllegalArgumentException, IOException
    { return new Flow(Matrix.parse(Files.readString(Path.of(filename))), width, height, sleep); }

    public Fraction maximum()
    { return new Fulkerson(graph).maxflow(sleep); }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("sansserif", Font.BOLD, 28));

        paintJoints(g2);
        paintNodes(g2);
    }

    private void paintNodes(Graphics2D g2)
    {
        for(int i = 0; i < graph.getRowCount(); i++)
        {
            g2.setColor(Color.WHITE);
            PaintUtilities.fillCircle(g2, points.get(i).x, points.get(i).y, NODE_SIZE);

            g2.setColor(Color.BLACK);
            PaintUtilities.drawCircle(g2, points.get(i).x, points.get(i).y, NODE_SIZE);

            String nodeName = Integer.toString(i);
            Point txtPoint = TextStroke.center(nodeName, g2.getFontMetrics(), points.get(i).x, points.get(i).y);
            g2.drawString(nodeName, txtPoint.x, txtPoint.y);
        }
    }

    private void paintJoints(Graphics2D g2)
    {
        g2.setColor(Color.BLACK);
        for(int i = 0; i < graph.getRowCount(); i++)
        {
            for(int j = 0; j < graph.getColumnCount(); j++)
            {
                if(graph.get(i, j).compareTo(Fraction.zero()) > 0)
                { g2.drawLine(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y); }
            }
        }
    }
}
