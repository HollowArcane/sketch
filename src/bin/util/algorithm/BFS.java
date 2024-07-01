package bin.util.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

public class BFS<T>
{
    private HashMap<T, T> source;
    private Vector<T> queue;
    private Set<T> seen;

    private T firstItem;
    private T lastItem;

    public BFS(T firstItem, T lastItem)
    {
        source = new HashMap<>();
        queue = new Vector<>();
        seen = new HashSet<>();

        this.firstItem = firstItem;
        this.lastItem = lastItem;

        queue.add(firstItem);
    }

    public ArrayList<T> search(Function<T, T[]> spreadFunction)
    {
        while(queue.size() > 0)
        {
            T crtItem = queue.remove(0);
            
            ArrayList<T> result = exploreAll(crtItem, spreadFunction.apply(crtItem));
            if(result != null)
            { return result; }
        }

        return null;
    }

    private ArrayList<T> exploreAll(T source, T[] items)
    {
        for(T item: items)
        {
            if(!hasExplored(item))
            {
                explore(source, item);
                if(item.equals(lastItem))
                { return traceBack(); }
            }
        }
        return null;
    }

    private boolean hasExplored(T item)
    { return seen.contains(item); }

    private void explore(T source, T nextItem)
    {
        seen.add(nextItem);
        queue.add(nextItem);
        this.source.put(nextItem, source);
    }

    private ArrayList<T> traceBack()
    {
        ArrayList<T> trace = new ArrayList<>();

        T item = lastItem;
        while(item != firstItem)
        {
            trace.add(0, item);
            item = source.get(item);
        }
        trace.add(0, firstItem);
        return trace;
    }
}
