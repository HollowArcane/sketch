package bin.util.algebra;

import java.util.Arrays;

public class Matrix implements Cloneable
{
    Fraction[][] elements;

    public Matrix(Fraction[][] elements)
    { this.elements = elements; }

    public static Matrix zero(int rows, int cols)
    {
        Fraction[][] elements = new Fraction[rows][cols];

        for(int i = 0; i < elements.length; i++)
        { Arrays.fill(elements[i], new Fraction(0)); }

        return new Matrix(elements);
    }

    public static Matrix identity(int size)
    {
        return new Matrix(bin.util.Arrays.fill(new Fraction[size][], i -> 
            bin.util.Arrays.fill(new Fraction[size], j -> new Fraction((i == j) ? 1: 0))
        ));
    }

    @Override
    public Matrix clone()
    {
        Fraction[][] fractions = new Fraction[elements.length][];
        for(int i = 0; i < fractions.length; i++)
        {
            fractions[i] = new Fraction[elements[i].length];
            for(int j = 0; j < fractions[i].length; j++)
            { fractions[i][j] = elements[i][j].copy(); }
        }

        return new Matrix(fractions);
    }

    public void set(int row, int column, Fraction value)
    { elements[row][column] = value; }

    public Fraction[] firstRow()
    { return getRow(0); }

    public Fraction[] lastRow()
    { return getRow(elements.length - 1); }
    
    public Fraction[] getRow(int row)
    {
        if(row < 0 || row >= elements.length)
        { return null; }

        return elements[row];
    }

    public Fraction[] firstColumn()
    { return getColumn(0); }

    public Fraction[] lastColumn()
    { return getColumn(getColumnCount() - 1); }
    {  }
    public Fraction[] getColumn(int column)
    {
        if(column < 0 || elements.length > 0 && column >= elements[0].length)
        { return null; }

        return bin.util.Arrays.map(elements, (i, f) -> elements[i][column]);
    }

    public Matrix setRow(int row, Fraction... values)
    {
        if(row < 0 || row >= elements.length)
        { return this; }

        for(int i = 0; i < Math.min(values.length, elements[row].length); i++)
        { elements[row][i] = values[i]; }
        return this;
    }

    public Matrix setColumn(int column, Fraction... values)
    {
        if(column < 0 || elements.length > 0 && column >= elements[0].length)
        { return this; }

        for(int i = 0; i < Math.min(values.length, elements.length); i++)
        { elements[i][column] = values[i]; }

        return this;
    }

    public Matrix scale(Fraction fraction)
    {
        for(int i = 0; i < elements.length; i++)
        { L(i, fraction); }
        return this;
    }

    public Matrix L(int i, int j)
    {
        Fraction[] vessel = elements[i];
        elements[i] = elements[j];
        elements[j] = vessel;
        return this;
    }

    public Matrix L(int i, int j, Fraction lambda)
    {
        for(int n = 0; n < elements[i].length; n++)
        { elements[i][n].add(elements[j][n].copy().multiply(lambda)); }
        return this;
    }

    public Matrix L(int i, Fraction lambda)
    {
        for(int n = 0; n < elements[i].length; n++)
        { elements[i][n].multiply(lambda); }
        return this;
    }

    public Matrix pivot(int i, int j)
    {
        if(i < 0 || i >= elements.length)
        { return this; }

        if(j < 0 || j >= elements[i].length)
        { return this; }

        if(elements[i][j].numerator() == 0)
        { return this; }

        L(i, elements[i][j].inverse());
        
        for(int n = 0; n < elements.length; n++)
        {
            if(n != i)
            { L(n, i, elements[n][j].copy().multiply(-1)); }
        }
        return this;
    }

    public Matrix ladder()
    {
        int j = -1;
        for(int i = 0; i < elements.length; i++)
        {
            j++;
            if(j > elements[i].length)
            { return this; }

            if(elements[i][j].numerator() == 0)
            {
                for(int n = 0; n < elements.length; n++)
                {
                    if(elements[n][j].numerator() != 0)
                    { L(n, j); }
                }
            }

            for(int n = i + 1; n < elements.length; n++)
            { L(n, i, elements[n][j].copy().multiply(-1)); }
        }
        return this;
    }

    public Matrix simplify()
    {
        for (int i = 0; i < elements.length; i++)
        {
            for (int j = 0; j < elements[i].length; j++)
            { elements[i][j].simplify(); }
        }
        return this;
    }

    public Fraction get(int i, int j)
    {
        if(i < 0 || j < 0 || i > elements.length || j > elements[i].length)
        { return null; }

        return elements[i][j].copy();
    }

    public Matrix deleteRow(int i)
    {
        if(elements.length == 0 || elements.length <= i)
        { return this; }

        Fraction[][] news = new Fraction[elements.length - 1][];

        for(int j = 0, k = 0; j < news[0].length; j++)
        {
            if(j == i)
            { continue; }

            news[k++] = elements[j];
        }
        elements = news;
        return this;
    }

    public Matrix deleteColumn(int j)
    {
        if(elements.length == 0 || elements[0].length <= j)
        { return this; }

        Fraction[][] news = new Fraction[elements.length][];

        for(int i = 0; i < news.length; i++)
        {
            news[i] = new Fraction[elements[i].length - 1];
            for(int k = 0, l = 0; k < news[i].length; k++, l++)
            {
                if(l == j)
                { k--; }
                else
                { news[i][k] = elements[i][l]; }
            }
        }
        elements = news;
        return this;
    }

    public int getColumnCount()
    { return elements.length > 0 ? elements[0].length: 0; }

    public int getRowCount()
    { return elements.length; }

    private int[] getColumnMaxLength(int column)
    {
        int[] length = { 0, 0 };
        for(int i = 0; i < elements.length; i++)
        {
            if(column < 0 || column > elements[i].length)
            { return new int[] { -1, -1 }; }

            int numLength = Integer.toString(elements[i][column].numerator()).length();
            int denLength = Integer.toString(elements[i][column].denominator()).length();

            if(numLength > length[0])
            { length[0] = numLength; }
            
            if(denLength > length[1])
            { length[1] = denLength; }
        }
        return length;
    }

    public String toString(int space)
    {
        if(elements.length == 0)
        { return ""; }
        
        int[][] lengths = new int[elements[0].length][2];
        Arrays.fill(lengths, new int[] { 0, 0 });

        for(int i = 0; i < lengths.length; i++)
        { lengths[i] = getColumnMaxLength(i); }

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < elements.length; i++)
        {
            for(int j = 0; j < elements[i].length; j++)
            { builder.append(elements[i][j].toString(lengths[j][0], lengths[j][1])).append(" ".repeat(space)); }
            builder.append("\n");
        }
        return builder.toString();
    }
    @Override
    public String toString()
    { return toString(1);  }

    public static Matrix parse(String matrix)
        throws IllegalArgumentException
    {
        return new Matrix(
            bin.util.Arrays.map(matrix.split("\n"), (_, row) ->
                bin.util.Arrays.map(row.trim().split("( ){2,}"), (_, col) -> Fraction.parse(col))
            )
        );
    }
}
