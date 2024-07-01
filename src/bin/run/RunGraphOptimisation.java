package bin.run;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bin.util.algebra.Matrix;
import bin.util.algorithm.Fulkerson;

public class RunGraphOptimisation
{
    public static Matrix graph;

    static void main()
        throws IOException
    {
        graph = Matrix.parse(Files.readString(Path.of("graph")));
        System.out.println(graph);

        Fulkerson flow = new Fulkerson(graph);
        System.out.println("MAXIMUM FLOW: " + flow.maxflow());
        System.out.println(graph);
    }
}
