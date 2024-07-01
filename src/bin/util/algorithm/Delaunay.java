package bin.util.algorithm;

import java.util.ArrayList;
import java.util.HashSet;

import bin.util.geometry.Circle;
import bin.util.geometry.Polygon;
import bin.util.geometry.Vector2;

public class Delaunay
{
    ArrayList<Vector2> seeds;
    ArrayList<Circle> circumcircles;

    public Delaunay(ArrayList<Vector2> seeds)
    { this.seeds = seeds; }

    public ArrayList<Polygon> veronoi(int minX, int minY, int maxX, int maxY)
    {
        if(circumcircles == null)
        { return null; }

        ArrayList<Polygon> diagram = new ArrayList<>(seeds.size());
        
        ArrayList<Vector2> corners = new ArrayList<>(circumcircles.size() + 4);
        for(int i = 0; i < circumcircles.size(); i++)
        { corners.add(circumcircles.get(i).center()); }

        corners.add(new Vector2(maxX, maxY));
        corners.add(new Vector2(minX, maxY));
        corners.add(new Vector2(minX, minY));
        corners.add(new Vector2(maxX, minY));

        for(Vector2 seed: seeds)
        {
            HashSet<Vector2> cell = new HashSet<>(4);
            for(int j = 0; j < 4; j++)
            {
                Vector2 closest = corners.get(0);
                double dist = closest.distance2(seed);
                for(int k = 1; k < corners.size(); k++)
                {
                    double newDist = corners.get(k).distance2(seed);
                    if(cell.contains(closest) || !cell.contains(corners.get(k)) && newDist < dist)
                    {
                        closest = corners.get(k);
                        dist = newDist;
                    }
                }
                cell.add(closest);
            }
            diagram.add(new Polygon(cell.toArray(Vector2[]::new)));
        }
        return diagram;
    }

    public ArrayList<Polygon> triangulise()
    {
        circumcircles = new ArrayList<>(seeds.size() - 1);
        ArrayList<Polygon> triangles = new ArrayList<>(seeds.size() - 1);
        
        for(int i = 0; i < seeds.size(); i++)
        {
            for(int j = i + 1; j < seeds.size(); j++)
            {
                for(int k = j + 1; k < seeds.size(); k++)
                {
                    Circle circumcircle = Circle.through(seeds.get(i), seeds.get(j), seeds.get(k));
                    boolean good = true;
                    for(int m = 0; m < seeds.size(); m++)
                    {
                        if(m != i && m != j && m != k && circumcircle.contains(seeds.get(m)))
                        {
                            good = false;
                            break;
                        }
                    }
                    if(good)
                    {
                        circumcircles.add(circumcircle);
                        triangles.add(new Polygon(seeds.get(i), seeds.get(j), seeds.get(k)));
                    }
                }
            }
        }

        return triangles;
    }
}
