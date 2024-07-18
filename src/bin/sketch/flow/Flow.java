package bin.sketch.flow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.StringTemplate.STR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import bin.graphics.animation.Animation;
import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicFloat;
import bin.graphics.property.GraphicStroke;
import bin.graphics.shape.GArrowTip;
import bin.graphics.shape.GCircle;
import bin.graphics.shape.GLine;
import bin.graphics.shape.GText;
import bin.util.Arrays;
import bin.util.algebra.Fraction;
import bin.util.algebra.Matrix;
import bin.util.algorithm.Fulkerson;
import bin.util.geometry.Circle;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;
import bin.util.paint.TextStroke;

public class Flow
{
    private static final int NODE_SIZE = 15;
    private static final float RANDOM_DISTANCE = 300;

    private int sleep;

    private Matrix graphInitial;
    private Matrix graph;

    private ArrayList<Point> points;

    private ArrayList<GCircle> circles;
    private HashMap<Integer, GLine> lines;
    private ArrayList<GText> texts;
    private ArrayList<GArrowTip> arrows;

    public Flow(Matrix graph, String[] labels, int width, int height, int sleep)
    {
        this.graphInitial = graph.clone();
        this.graph = graph;
        this.sleep = sleep;

        maximum();
        setupDisplay(graph, labels, width, height);
    }

    public Flow(Matrix graph, int width, int height, int sleep)
    {
        this.graphInitial = graph.clone();
        this.graph = graph;
        this.sleep = sleep;

        maximum();
        setupDisplay(graph, null, width, height);
    }

    private void setupDisplay(Matrix graph, String[] labels, int width, int height)
    {
        points = new ArrayList<>();
        lines = new HashMap<>();
        texts = new ArrayList<>();
        arrows = new ArrayList<>();
        circles = new ArrayList<>();
        
        Random random = new Random();
        for(int i = 0; i < graph.getRowCount(); i++)
        {
            Point p = new Point(random.nextInt(width), random.nextInt(height));
            if(!Arrays.some(points, pt -> pt.distance(p) < RANDOM_DISTANCE))
            {
                GCircle circle = new GCircle(p, NODE_SIZE);
                circle.getProperty(3)
                    .from(new GraphicFloat(0))
                    .with(Animation.BEHAVIOUR_OVERSHOOT)
                    .in(300)
                    .after(random.nextInt(1000, 1500))
                    .create();

                GText text = new GText(p, (labels != null && labels.length > i) ? labels[i]: i + "");
                text.getProperty(GText.PROPERTY_COLOR)
                    .from(new GraphicColor(0, 0, 0, 0))
                    .in(1000)
                    .after(random.nextInt(1500, 1700))
                    .create();
                circles.add(circle);
                texts.add(text);
                points.add(p);
            }
            else
            { i--; }
        }

        Fraction zero = Fraction.zero();
        for(int i = 0; i < graph.getRowCount(); i++)
        {
            for(int j = 0; j < graph.getColumnCount(); j++)
            {
                if(graphInitial.get(i, j).compareTo(zero) > 0)
                {
                    Vector2 p1 = new Vector2(points.get(i));
                    Vector2 p2 = new Vector2(points.get(j));

                    Vector2 v = Vector2.sub(p2, p1);
                    v.scale(3*NODE_SIZE/(2*v.mag()));
                    
                    GLine line = new GLine(
                        p1.add(v).toPoint(),
                        p2.sub(v).toPoint(),
                        Color.GRAY,
                        2,
                        new float[] { 10, 10 }
                    );

                    line.getProperty(GLine.PROPERTY_TRACE)
                        .from(new GraphicFloat(0))
                        .with(Animation.BEHAVIOUR_CUBIC)
                        .in(3000)
                        .after(random.nextInt(1000, 2000))
                        .create();
                    line.getProperty(GLine.PROPERTY_COLOR)
                        .from(new GraphicColor(0, 0, 0, 0))
                        .in(3000)
                        .after(random.nextInt(1000, 2000))
                        .create();

                    if(graph.get(j, i).compareTo(zero) > 0)
                    {
                        line.getProperty(GLine.PROPERTY_COLOR)
                            .set(new GraphicColor(1f, 0, 0, 1f))
                            .in(1000)
                            .after(9000 + 500 * i)
                            .create();
                        line.getProperty(GLine.PROPERTY_STROKE)
                            .set(new GraphicStroke(3f, new float[] { 1f }, 0f))
                            .in(1000)
                            .after(9000 + 500 * i)
                            .create();
                    }

                    GArrowTip arrow = new GArrowTip();
                    arrow.pin(line, 1f);
                    GText text = new GText(STR."\{ graph.get(j, i) }/\{ graphInitial.get(i, j) }");
                    text.pin(line, .5f);
                    text.setRotationBehaviour(GText.RotationBehaviour.HALF);
                    text.getProperty(GText.PROPERTY_COLOR)
                        .from(new GraphicColor(0, 0, 0, 0))
                        .in(3500)
                        .after(random.nextInt(1000, 2000))
                        .create();

                    lines.put(i * graph.getColumnCount() + j, line);
                    arrows.add(arrow);
                    texts.add(text);
                }
            }
        }
    }

    
    public static Flow read(String filename, int width, int height, int sleep)
        throws IllegalArgumentException, IOException
    {
        String data = Files.readString(Path.of(filename));
        String[] lines = data.split("\n");

        String[] labels = lines[0].trim().replaceAll("\t", " ").split("( ){1,}");
        StringBuilder matrix = new StringBuilder();
        for(int i = 1; i < lines.length; i++)
        { matrix.append(lines[i].substring(lines[i].indexOf(" "))).append("\n"); }

        return new Flow(Matrix.parse(matrix.toString()), labels, width, height, sleep);
    }

    public Fraction maximum()
    { return new Fulkerson(graph).maxflow(sleep); }

    public Point getPoint(int x, int y)
    { return Arrays.first(points, p -> p.distance(x, y) <= NODE_SIZE); }

    public void moveLines(Point p)
    {
        int i = Arrays.ifirst(points, (_, pt) -> pt == p);
        int j = 0;

        for(; j < graph.getColumnCount(); j++)
        {
            int index = i * graph.getColumnCount() + j;
            GLine l = lines.get(index);
            if(l == null)
            { continue; }

            Vector2 p1 = new Vector2(points.get(i));
            Vector2 p2 = new Vector2(points.get(j));

            Vector2 v = Vector2.sub(p2, p1);
            v.scale(3*NODE_SIZE/(2*v.mag()));
                  
            Point pfinal1 = p1.add(v).toPoint();
            Point pfinal2 = p2.sub(v).toPoint();
            l.p1.setLocation(pfinal1.x, pfinal1.y);
            l.p2.setLocation(pfinal2.x, pfinal2.y);
        }

        j = i;
        i = 0;
        for(; i < graph.getRowCount(); i++)
        {
            int index = i * graph.getColumnCount() + j;
            GLine l = lines.get(index);
            if(l == null)
            { continue; }

            Vector2 p1 = new Vector2(points.get(i));
            Vector2 p2 = new Vector2(points.get(j));

            Vector2 v = Vector2.sub(p2, p1);
            v.scale(3*NODE_SIZE/(2*v.mag()));
                    
            Point pfinal1 = p1.add(v).toPoint();
            Point pfinal2 = p2.sub(v).toPoint();
            l.p1.setLocation(pfinal1.x, pfinal1.y);
            l.p2.setLocation(pfinal2.x, pfinal2.y);
        }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("sansserif", Font.BOLD, 28));

        for(GLine l: lines.values())
        { l.paint(g2); }

        for(GText t: texts)
        { t.paint(g2); }

        for(GArrowTip a: arrows)
        { a.paint(g2); }

        for(GCircle c: circles)
        { c.paint(g2); }
    }

}
