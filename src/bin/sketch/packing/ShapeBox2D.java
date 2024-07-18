package bin.sketch.packing;

import java.awt.Graphics;
import java.util.ArrayList;

import bin.util.Arrays;
import bin.util.algorithm.Packing2D;
import bin.util.geometry.Shape;

public class ShapeBox2D
{
    private ArrayList<ShapeItem2D> shapes;
    private int scale;

    public ShapeBox2D(int scale, Shape... shapes)
    {
        this.scale = scale;
        this.shapes = Arrays.fill(new ArrayList<ShapeItem2D>(shapes.length), shapes.length, i -> new ShapeItem2D(shapes[i], i + ""));
    }

    public void clear()
    { shapes.clear(); }

    public void addShape(Shape s)
    { shapes.add(new ShapeItem2D(s, shapes.size() + "")); }

    public void fitNFDH(double width, double height, int sleep)
    { new Packing2D(width, height, sleep).NFDH(shapes.toArray(ShapeItem2D[]::new)); }

    public void fitBFDH(double width, double height, int sleep)
    { new Packing2D(width, height, sleep).BFDH(shapes.toArray(ShapeItem2D[]::new)); }

    public void fitFFDH(double width, double height, int sleep)
    { new Packing2D(width, height, sleep).FFDH(shapes.toArray(ShapeItem2D[]::new)); }

    public void fitBruteForce(double width, double height, int sleep)
    {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");

        System.out.println("STARTED AT: " + sdf.format(new java.util.Date(System.currentTimeMillis())));
        try
        { new Packing2D(width, height, sleep).bruteForceFit(shapes.toArray(ShapeItem2D[]::new)); }
        catch (Exception e)
        { e.printStackTrace(); }

        System.out.println("ENDED AT: " + sdf.format(new java.util.Date(System.currentTimeMillis())));
        System.out.println(shapes);
    }

    public void draw(Graphics g)
    {
        for (ShapeItem2D shape : shapes)
        { shape.draw(scale, g); }
    }
}
