package bin.util.algorithm;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

import bin.util.Arrays;
import bin.util.algebra.Fraction;
import bin.util.algebra.Matrix;

public class Fulkerson
{
    private Matrix graph;

    public Fulkerson(Matrix graph)
    { this.graph = graph; }

    private void overflow(int sleep)
    {
        BFS<Integer> seeker = new BFS<>(0, graph.getRowCount() - 1);
        Function<Integer, Integer[]> spreadFunction = node -> Arrays.ifilter(
                graph.getRow(node), (_, element) -> element.compareTo(new Fraction(0)) > 0
        );

        ArrayList<Integer> result = null;

        while((result = seeker.search(spreadFunction)) != null)
        { underflow(result, sleep); }
    }

    private void underflow(ArrayList<Integer> result, int sleep)
    {
        System.out.println(STR."PATH: \{ result }");
        Fraction minflow = minflow(result);
        for(int i = 0; i < result.size() - 1; i++)
        {
            Fraction flow = graph.get(result.get(i), result.get(i+1));
            graph.set(result.get(i), result.get(i+1), flow.substract(minflow).simplify());

            flow = graph.get(result.get(i+1), result.get(i));
            graph.set(result.get(i+1), result.get(i), flow.add(minflow).simplify());

            try
            { Thread.sleep(sleep); }
            catch (InterruptedException e) { }
        }
        System.out.println(graph);
    }

    private Fraction minflow(ArrayList<Integer> result)
    {
        Fraction minflow = null;
        for(int i = 0; i < result.size() - 1; i++)
        {
            Fraction flow = graph.get(result.get(i), result.get(i+1));
            if(minflow == null || flow.compareTo(minflow) < 0)
            { minflow = flow; }
        }
        return minflow;
    }

    public Fraction maxflow()
    { return maxflow(0); }

    public Fraction maxflow(int sleep)
    {
        overflow(sleep);
        return Arrays.sum(graph.lastRow(), (i, f) -> f, (sum, value) -> Objects.requireNonNullElse(sum, new Fraction(0)).add(value));
    }
}
