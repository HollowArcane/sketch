package bin.util.algorithm;

import java.util.ArrayList;

import bin.sketch.packing.Item1D;
import bin.util.Arrays;

public class Packing1D
{
    private static class Container
    {
        private ArrayList<Item1D> items;
        private double size;

        public Container(double size)
        {
            this.size = size;
            items = new ArrayList<>();
        }

        public boolean canFit(double size)
        { return this.size >= size; }

        public void fit(Item1D item)
        {
            this.size -= item.size();
            items.add(item);
        }
    }

    private static interface ContainerSeeker
    { public Container seek(ArrayList<Container> containers, double size); }

    private ArrayList<Container> containers;
    private double size;

    public Packing1D(double size)
    { this.size = size; }

    public ArrayList<ArrayList<Item1D>> NF(Item1D... items)
    {
        // return list of containers
        return standardFit((containers, size) -> 
            // get the last container if it can fit the item
            containers.size() > 0 && containers.getLast().canFit(size) ? containers.getLast(): null,
        items);
    }

    public ArrayList<ArrayList<Item1D>> FF(Item1D... items)
    {
        // return list of containers
        return standardFit((containers, size) -> Arrays.first(
            // get the first container that can fit the item
            containers, container -> container.canFit(size)
        ), items);
    }

    public ArrayList<ArrayList<Item1D>> BF(Item1D... items)
    {
        // return list of containers
        return standardFit((containers, size) -> Arrays.max(
            // get the minimum size of gap
            Arrays.filter(containers, (_, container) -> container.canFit(size)),
            (c1, c2) -> Double.compare(c2.size - size, c1.size - size)
        ), items);
    }

    public ArrayList<ArrayList<Item1D>> WF(Item1D... items)
    {
        // return list of containers
        return standardFit((containers, size) -> Arrays.max(
            // get the maximum size of gap
            Arrays.filter(containers, (_, container) -> container.canFit(size)),
            (c1, c2) -> Double.compare(c1.size - size, c2.size - size)
        ), items);
    }

    public ArrayList<ArrayList<Item1D>> standardFit(ContainerSeeker seeker, Item1D... items)
    {
        setupFit();

        for(Item1D item: items)
        {
            // search for container to fit the item
            Container container = seeker.seek(containers, item.size());
            if(container == null)
            {
                // if item size is greater than max size, ignore item
                if(item.size() > size)
                { continue; }
                // create new container
                container = new Container(size);
                // add container to list
                containers.add(container);
            }
            // fit item into container
            container.fit(item);
        };
        // return list of containers
        return Arrays.map(containers, (i, c) -> c.items);
    }

    public ArrayList<ArrayList<Item1D>> bruteForceFit(Item1D... items)
    {
        // return list of containers
        return Arrays.min(
            // permute the items
            // next-fit each permutation
            // get the minimum size of the next-fits
            Arrays.map(permute(items), (_, permutation) -> NF(permutation)),
            (f1, f2) -> Integer.compare(f1.size(), f2.size())
        );
    }

    private ArrayList<Item1D[]> permute(Item1D[] items)
    {
        ArrayList<Item1D[]> permutations = new ArrayList<>();
        permuteRecursive(items, 0, permutations);
        return permutations;
    }

    private void permuteRecursive(Item1D[] items, int index, ArrayList<Item1D[]> permutations)
    {
        if (index == items.length)
        { permutations.add(items.clone()); }
        else
        {
            for (int i = index; i < items.length; i++)
            {
                swap(items, index, i);
                permuteRecursive(items, index + 1, permutations);
                swap(items, index, i); // backtrack
            }
        }
    }

    private void swap(Item1D[] items, int i, int j)
    {
        Item1D temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }

    private void setupFit()
    { containers = new ArrayList<>(); }
}