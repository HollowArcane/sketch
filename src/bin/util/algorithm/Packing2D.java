package bin.util.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import bin.sketch.packing.Item1D;
import bin.sketch.packing.ShapeItem2D;
import bin.util.geometry.Circle;
import bin.util.geometry.Parallelogram;
import bin.util.geometry.Shape;
import bin.util.geometry.Vector2;
import bin.util.geometry.Shape.Rectangle;

public class Packing2D
{
    private double width, height;
    private int sleep;
    private ArrayList<Level> levels;

    public Packing2D(double width, double height, int sleep)
    {
        this.width = width;
        this.height = height;
        this.sleep = sleep;
    }

    public void NFDH(ShapeItem2D... items)
    {
        // choose the last level if can fit else -1
        LevelSeeker seeker = (levels, b, h) -> levels.size() > 0 && levels.getLast().canFit(b) ? levels.getLast(): null;
        // run standard fit with the current level seeker
        standardFit(seeker, items);
    }

    public void BFDH(ShapeItem2D... items)
    {
        // seek the level which can fit the best
        LevelSeeker seeker = (levels, b, h) -> bin.util.Arrays.min(
            bin.util.Arrays.filter(
                levels, (Integer _, Level l) -> l.canFit(b)
            ),
            (l1, l2) -> Double.compare(l1.width - b, l2.width - b)
        );
        // run standard fit with the current level seeker
        standardFit(seeker, items);
    }

    public void FFDH(ShapeItem2D... items)
    {
        // seek the first level which can fit
        LevelSeeker seeker = (levels, b, h) -> bin.util.Arrays.first(levels,
            l -> l.canFit(b)
        );
        // run standard fit with the current level seeker
        standardFit(seeker, items);
    }

    private static interface LevelSeeker
    { public Level seek(ArrayList<Level> levels, double width, double height); }

    private static record Position(int x, int y, int rotation)
    {
        public double angle()
        { return rotation * Math.PI / 2; }

        public void transform(Shape shape)
        {
            Rectangle bound = shape.boundingBox();
        
            shape.translate(new Vector2(
                bound.base()/2 + x(),
                bound.height()/2 + y()
            )).rotate(angle());
        }

        public void transformReverse(Shape shape)
        {
            Rectangle bound = shape.boundingBox();
        
            shape.rotate(-angle())
            .translate(new Vector2(
                -bound.base()/2 - x(),
                -bound.height()/2 - y()
            ));
        }
    }

    private static class Level
    {
        private double y;
        private double initialWidth;
        private double width;
        private double height;

        public Level(double y, double width, double height)
        {
            this.y = y;
            this.height = height;
            this.initialWidth = width;
            this.width = width;
        }

        public void trim(double amount)
        { this.width -= amount; }

        public boolean canFit(double width)
        { return this.width >= width; }

        public double currentX()
        { return initialWidth - width; }

        public double currentY()
        { return y; }

        public double nextY()
        { return y + height; }

        public void move(ShapeItem2D item, double x, double y, double b, double h)
        {
            Vector2 center = item.getShape().center();
            
            item.getShape().translate(new Vector2(
                currentX() - x,
                currentY() - y
            ));
            trim(b);
        }
    }

    private void setupFit(ShapeItem2D... items)
    {
        // clear array of levels
        levels = new ArrayList<>();
        // sort array by decreasing height
        Arrays.sort(items, (s1, s2) -> Double.compare(
            s2.getShape().boundingBox().height(),
            s1.getShape().boundingBox().height())
        );
    }

    private void standardFit(LevelSeeker seeker, ShapeItem2D... items)
    {
        setupFit(items);

        for(int i = 0; i < items.length; i++)
        {
            if(items[i].getShape().boundingBox() instanceof Rectangle(double x, double y, double b, double h))
            {
                // seek level to fit item
                Level fit = seeker.seek(levels, b, h);

                // check if found any fit
                if(fit != null)
                // if so place it and go to the next
                { fit.move(items[i], x, y, b, h); }
                else
                {
                    // if not, add level
                    Level level = new Level((levels.size() > 0 ? levels.getLast().nextY(): 0), width, h);

                    if(level.canFit(0) && level.nextY() <= height)
                    {
                        levels.add(level);
                        level.move(items[i], x, y, b, h);
                    }
                    else
                    // go to next shape if a new level cannot fit
                    { continue; }
                }
                items[i].setFit(true);

                try
                { Thread.sleep(sleep); }
                catch (InterruptedException e) { }
            }
        }
    }

    public void bruteForceFit(ShapeItem2D... items)
        throws CloneNotSupportedException
    {
        // generate all possible placement configurations for the items
        ArrayList<ArrayList<Position>> possibilities = tryAllPossibilities(width, height, items);
        System.out.println("ALL POSSIBILITIES GENERATED");
        // find the best configuration based on the total area covered
        ArrayList<Position> best = bin.util.Arrays.max(possibilities, (p1, p2) -> Double.compare(totalArea(p1, items), totalArea(p2, items)));
        // iterate over the best positions found for each item
        for(int i = 0; i < best.size(); i++)
        {
            // if the position is null, skip to the next item
            if(best.get(i) == null)
            { continue; }

            // get the bounding rectangle of the current item
            Rectangle bounds = items[i].getShape().boundingBox();
            // move the shape to the calculated position
            items[i].getShape().translate(new Vector2(best.get(i).x() + bounds.base()/2, best.get(i).y() + bounds.height()/2));
            System.out.println("POSITION: " + best.get(i).x() + ", " + best.get(i).y());
            // rotate the shape according to the calculated angle
            items[i].getShape().rotate(best.get(i).angle());
            System.out.println("ANGLE: " + best.get(i).angle());
            // mark the item as fitted
            items[i].setFit(true); 

            // attempt to pause the current thread to visualize the placement process
            try
            { Thread.sleep(sleep); }
            catch (InterruptedException e) { }
        }
        System.out.println("DONE");
    }

    private static ArrayList<ArrayList<Position>> tryAllPossibilities(double width, double height, ShapeItem2D... items)
            throws CloneNotSupportedException
    {
        // initialize a list to store all possible position combinations
        ArrayList<ArrayList<Position>> possibilities = new ArrayList<>();
        // add an empty list to start with
        possibilities.add(new ArrayList<>());

        int i = 0;
        // iterate over each item to place
        for (ShapeItem2D item : items)
        {
            System.out.println(++i);
            // generate all possible positions for the current item
            ArrayList<Position> positions = generatePositions(width, height, item);

            // iterate over existing combinations
            int length = possibilities.size();
            for(int k = 0; k < length; k++)
            {
                ArrayList<Position> possibility = possibilities.get(k);
                // iterate over new positions to add to existing combinations
                for(Position p: positions)
                {

                    // check if the new position is valid with the current combination
                    if(validPosition(p, possibility, items))
                    {
                        // create a new combination by adding the new valid position
                        ArrayList<Position> newPossibility = new ArrayList<>(possibility);
                        newPossibility.add(p);
                        possibilities.add(newPossibility);
                    }
                }

                // add the current combination as is (without the new position)
                ArrayList<Position> newPossibility = new ArrayList<>(possibility);
                newPossibility.add(null);
                possibilities.add(newPossibility);

                possibility.add(null);
            }
        }
        // return all possible combinations of positions
        return possibilities;
    }

    private static double totalArea(ArrayList<Position> positions, ShapeItem2D[] items)
    { return bin.util.Arrays.sum(positions, (i, p) -> p != null ? items[i].getShape().area(): 0, (sum, value) -> sum + value); }

    private static boolean validPosition(Position position, ArrayList<Position> possibility, ShapeItem2D[] items)
        throws CloneNotSupportedException
    {
        Rectangle b2 = items[possibility.size()].getShape().boundingBox();
        Shape s2 = ((Shape)items[possibility.size()].getShape().clone())
                        .translate(new Vector2(position.x() + b2.base()/2, position.y() + b2.height()/2))
                        .rotate(position.angle());

        for(int i = 0; i < possibility.size(); i++)
        {
            Position p = possibility.get(i);
            if(p == null)
            { continue; }

            Rectangle b1 = items[i].getShape().boundingBox();
            Shape s1 = ((Shape)items[i].getShape().clone())
                            .translate(new Vector2(p.x() + b1.base()/2, p.y() + b1.height()/2))
                            .rotate(p.angle());

            if(s1.intersects(s2))
            { return false; }
        }
        return true;
    }

    private static ArrayList<Position> generatePositions(double width, double height, ShapeItem2D item)
    {
        Rectangle bound = item.getShape().boundingBox();
        ArrayList<Position> positions = new ArrayList<>();
        for(int y = 0; y <= height - bound.height(); y++)
        {
            for(int x = 0; x <= width - bound.base(); x++)
            { 
                positions.add(new Position(x, y, 0));
                if(item.getShape() instanceof Circle
                || item.getShape() instanceof Parallelogram)
                { continue; }
                
                positions.add(new Position(x, y, 2));
            }

        }

        if(item.getShape() instanceof Circle)
        { return positions; }

        for(int y = (int)((bound.base() - bound.height())/2); y <= height - bound.base() + (bound.base() - bound.height())/2; y++)
        {
            for(int x = (int)((bound.height() - bound.base())/2); x <= width - bound.height() + (bound.height() - bound.base())/2; x++)
            { positions.add(new Position(x, y, 1)); }
        }

        return positions;
    }
}