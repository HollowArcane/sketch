package bin.sketch.packing;

import java.util.ArrayList;

import bin.util.Arrays;
import bin.util.algorithm.Packing1D;

public class Box1D
{
    private ArrayList<Item1D> items;
    private int size;
    private ArrayList<ArrayList<Item1D>> result;

    public Box1D(int size, double... items)
    {
        this.size = size;
        this.items = Arrays.fill(new ArrayList<Item1D>(items.length), items.length, i -> new Item1D(i + "", items[i]));
    }

    public void fitFF()
    { result = new Packing1D(size).FF(items.toArray(Item1D[]::new)); }

    public void fitBF()
    { result = new Packing1D(size).BF(items.toArray(Item1D[]::new)); }

    public void fitWF()
    { result = new Packing1D(size).WF(items.toArray(Item1D[]::new)); }

    public void fitBruteForce()
    { result = new Packing1D(size).bruteForceFit(items.toArray(Item1D[]::new)); }

    @Override
    public String toString()
    {
        if(result == null)
        { return "Items has not been fit yet"; }

        return Arrays.join(result, "\n");
    }
}
