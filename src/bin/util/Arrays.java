package bin.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Arrays
{
    public static <E> ArrayList<E> of(int num, Supplier<E> function)
    { return fill(new ArrayList<E>(num), num, function); }

    public static <E> ArrayList<E> of(E[] array)
    { return fill(new ArrayList<E>(array.length), array.length, i -> array[i]); }

    public static <E> E[] fill(E[] array, Supplier<E> function)
    {
        for(int i = 0; i < array.length; i++)
        { array[i] = function.get(); }
        return array;
    }

    public static <T extends Collection<E>, E> T fill(T array, int num, Supplier<E> function)
    {
        for(int i = 0; i < num; i++)
        { array.add(function.get()); }
        return array;
    }

    public static <E> E[] fill(E[] array, Function<Integer, E> function)
    {
        for(int i = 0; i < array.length; i++)
        { array[i] = function.apply(i); }
        return array;
    }

    public static <T extends Collection<E>, E> T fill(T array, int num, Function<Integer, E> function)
    {
        for(int i = 0; i < num; i++)
        { array.add(function.apply(i)); }
        return array;
    }

    public static <E> boolean every(Iterable<E> array, Predicate<E> test)
    { return !some(array, test.negate()); }

    public static <E> boolean every(E[] array, BiPredicate<Integer, E> test)
    { return !some(array, test.negate()); }

    public static <E> boolean every(List<E> array, BiPredicate<Integer, E> test)
    { return !some(array, test.negate()); }

    public static <E, T> boolean every(Map<E, T> array, BiPredicate<E, T> test)
    { return !some(array, test.negate()); }

    public static <E> boolean some(Iterable<E> array, Predicate<E> test)
    { return first(array, test) != null; }

    public static <E> boolean some(E[] array, BiPredicate<Integer, E> test)
    { return ifirst(array, test) != -1; }

    public static <E> boolean some(List<E> array, BiPredicate<Integer, E> test)
    { return ifirst(array, test) != -1; }

    public static <E, T> boolean some(Map<E, T> array, BiPredicate<E, T> test)
    { return ifirst(array, test) != null; }
    
    public static String join(Iterable<?> array, CharSequence join)
    {
        StringBuilder builder = new StringBuilder();

        for (Object e : array)
        { builder.append(e).append(join); }
        
        try
        { builder.delete(builder.length() - join.length(), builder.length()); }
        catch (Exception e) { }

        return builder.toString();
    }

    public static String join(Object[] array, CharSequence join)
    {
        StringBuilder builder = new StringBuilder();
        
        for (Object e : array)
        { builder.append(e).append(join); }

        try
        { builder.delete(builder.length() - join.length(), builder.length()); }
        catch (Exception e) { }

        return builder.toString();
    }
    
    public static <E, T> T sum(Iterable<E> array, BiFunction<T, E, T> function)
    {
        T sum = null;
        for (E e : array)
        { sum = function.apply(sum, e); }
        return sum;
    }

    public static <E, T> T sum(E[] array, BiFunction<Integer, E, T> getter, BiFunction<T, T, T> summer)
    {
        T sum = null;
        for (int i = 0; i < array.length; i++)
        { sum = summer.apply(sum, getter.apply(i, array[i])); }
        return sum;
    }

    public static <E, T> T sum(List<E> array, BiFunction<Integer, E, T> getter, BiFunction<T, T, T> summer)
    {
        T sum = null;
        for (int i = 0; i < array.size(); i++)
        { sum = summer.apply(sum, getter.apply(i, array.get(i))); }
        return sum;
    }

    public static <E, T, L> L sum(Map<E, T> array, BiFunction<E, T, L> getter, BiFunction<L, L, L> summer)
    {
        L sum = null;
        for (Entry<E, T> entry: array.entrySet())
        { sum = summer.apply(sum, getter.apply(entry.getKey(), entry.getValue())); }
        return sum;
    }
    
    public static <E> double sum(Iterable<E> array, Function<E, Double> function)
    {
        double sum = 0;
        for (E e : array)
        { sum += function.apply(e); }
        return sum;
    }

    public static <E> double sum(E[] array, BiFunction<Integer, E, Double> function)
    {
        double sum = 0;
        for (int i = 0; i < array.length; i++)
        { sum += function.apply(i, array[i]); }
        return sum;
    }

    public static <E> double sum(List<E> array, BiFunction<Integer, E, Double> function)
    {
        double sum = 0;
        for (int i = 0; i < array.size(); i++)
        { sum += function.apply(i, array.get(i)); }
        return sum;
    }
    
    public static <E, T> double sum(Map<E, T> array, BiFunction<E, T, Double> function)
    {
        double sum = 0;
        for (Entry<E, T> entry: array.entrySet())
        { sum += function.apply(entry.getKey(), entry.getValue()); }
        return sum;
    }
    
    public static <E> E first(E[] array, Predicate<E> function)
    {
        for (E e: array)
        {
            if(function.test(e))
            { return e; }
        }
        return null;
    }

    public static <E> E first(Iterable<E> array, Predicate<E> function)
    {
        for (E e: array)
        {
            if(function.test(e))
            { return e; }
        }
        return null;
    }

    public static <E> int ifirst(List<E> array, BiPredicate<Integer, E> function)
    {
        for (int i = 0; i < array.size(); i++)
        {
            if(function.test(i, array.get(i)))
            { return i; }
        }
        return -1;
    }

    public static <E> int ifirst(E[] array, BiPredicate<Integer, E> function)
    {
        for (int i = 0; i < array.length; i++)
        {
            if(function.test(i, array[i]))
            { return i; }
        }
        return -1;
    }
    
    public static <E, T> E ifirst(Map<E, T> array, BiPredicate<E, T> function)
    {
        Entry<E, T> entry = first(array.entrySet(), e -> function.test(e.getKey(), e.getValue()));
        return entry != null ? entry.getKey() : null;
    }
    
    public static <E> E max(E[] array, Comparator<? super E> comparator)
    {
        E max = null;
        for (E e: array)
        {
            if(max == null || comparator.compare(e, max) > 0)
            { max = e; }
        }
        return max;
    }
    
    public static <E> E max(Iterable<E> array, Comparator<? super E> comparator)
    {
        E max = null;
        for (E e: array)
        {
            if(max == null || comparator.compare(e, max) > 0)
            { max = e; }
        }
        return max;
    }

    public static <E> int imax(List<E> array, Comparator<? super E> comparator)
    {
        int imax = -1;
        for (int i = 0; i < array.size(); i++)
        {
            if(imax == -1 || comparator.compare(array.get(i), array.get(imax)) > 0)
            { imax = i; }
        }
        return imax;
    }

    public static <E> int imax(E[] array, Comparator<? super E> comparator)
    {
        int imax = -1;
        for (int i = 0; i < array.length; i++)
        {
            if(imax == -1 || comparator.compare(array[i], array[imax]) > 0)
            { imax = i; }
        }
        return imax;
    }
    
    public static <E, T> E imax(Map<E, T> array, Comparator<? super T> comparator)
    { return max(array.entrySet(), (e1, e2) -> comparator.compare(e1.getValue(), e2.getValue())).getKey(); }
    
    public static <E> E min(E[] array, Comparator<? super E> comparator)
    { return max(array, comparator.reversed()); }
    
    public static <E> E min(Iterable<E> array, Comparator<? super E> comparator)
    { return max(array, comparator.reversed()); }

    public static <E> int imin(List<E> array, Comparator<? super E> comparator)
    {
        System.out.println(array.size());
        return imax(array, comparator.reversed());
    }

    public static <E> int imin(E[] array, Comparator<? super E> comparator)
    { return imax(array, comparator.reversed()); }
    
    public static <E, T> E imin(Map<E, T> array, Comparator<? super T> comparator)
    { return imax(array, comparator.reversed()); }
    
    public static <E> double avg(Collection<E> array, Function<E, Double> function)
    { return sum(array, function)/array.size(); }

    public static <E> double avg(E[] array, BiFunction<Integer, E, Double> function)
    { return sum(array, function)/array.length; }

    public static <E> double avg(List<E> array, BiFunction<Integer, E, Double> function)
    { return sum(array, function)/array.size(); }

    public static <E, T> double avg(Map<E, T> array, BiFunction<E, T, Double> function)
    { return sum(array, function)/array.size(); }

    public static <E> E random(E[] array)
    { return array[(int)(Math.random() * array.length)]; }

    public static <E> E random(List<E> array)
    { return array.get((int)(Math.random() * array.size())); }

    public static <E> E random(Vector<E> array)
    { return array.get((int)(Math.random() * array.size())); }

    @SuppressWarnings("unchecked")
    public static <E> E[] filter(E[] array, BiPredicate<Integer, E> function)
    {
        ArrayList<E> newArray = new ArrayList<>();
        for (int i = 0; i < array.length; i++)
        {
            if(function.test(i, array[i]))
            { newArray.add(array[i]); }
        }

        return newArray.toArray((E[]) Array.newInstance(array.getClass().componentType(), newArray.size()));
    }

    public static <E> ArrayList<E> filter(List<E> array, BiPredicate<Integer, E> function)
    {
        ArrayList<E> newArray = new ArrayList<>();
        for (int i = 0; i < array.size(); i++)
        {
            if(function.test(i, array.get(i)))
            { newArray.add(array.get(i)); }
        }

        return newArray;
    }

    public static <E> Vector<E> filter(Vector<E> array, BiPredicate<Integer, E> function)
    {
        Vector<E> newArray = new Vector<>();
        for (int i = 0; i < array.size(); i++)
        {
            if(function.test(i, array.get(i)))
            { newArray.add(array.get(i)); }
        }

        return newArray;
    }
    
    public static <T, E> HashMap<T, E> filter(Map<T, E> array, BiPredicate<T, E> function)
    {
        HashMap<T, E> newArray = new HashMap<>();
        for (Entry<T, E> entry: array.entrySet())
        {
            if(function.test(entry.getKey(), entry.getValue()))
            { newArray.put(entry.getKey(), entry.getValue()); }
        }

        return newArray;
    }
    
    public static <E> Integer[] ifilter(E[] array, BiPredicate<Integer, E> function)
    { return filter(fill(new Integer[array.length], i -> i), (_, i) -> function.test(i, array[i])); }

    public static <E> Integer[] ifilter(List<E> array, BiPredicate<Integer, E> function)
    { return filter(fill(new Integer[array.size()], i -> i), (_, i) -> function.test(i, array.get(i))); }

    public static <E> Integer[] ifilter(Vector<E> array, BiPredicate<Integer, E> function)
    { return filter(fill(new Integer[array.size()], i -> i), (_, i) -> function.test(i, array.get(i)));  }
    
    @SuppressWarnings("unchecked")
    public static <T, E> T[] ifilter(Map<T, E> array, BiPredicate<T, E> function)
    { return filter(array.keySet().toArray((T[]) Array.newInstance(array.keySet().getClass().componentType(), array.size())), (_, key) -> function.test(key, array.get(key))); }
    
    @SuppressWarnings("unchecked")
    public static <E, T> T[] map(E[] array, BiFunction<Integer, E, T> function)
    {
        T[] newArray = null;
        
        for (int i = 0; i < array.length; i++)
        {
            T item = function.apply(i, array[i]);

            if(newArray == null)
            { newArray = (T[]) Array.newInstance(item.getClass(), array.length); }

            newArray[i] = item;
        }

        return newArray;
    }
    
    public static <E, T> ArrayList<T> map(List<E> array, BiFunction<Integer, E, T> function)
    {
        ArrayList<T> newArray = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++)
        { newArray.add(function.apply(i, array.get(i))); }

        return newArray;
    }
    
    public static <E, T> Vector<T> map(Vector<E> array, BiFunction<Integer, E, T> function)
    {
        Vector<T> newArray = new Vector<>(array.size());
        for (int i = 0; i < array.size(); i++)
        { newArray.add(function.apply(i, array.get(i))); }

        return newArray;
    }
    
    public static <T, E, L> HashMap<T, L> map(Map<T, E> array, BiFunction<T, E, L> function)
    {
        HashMap<T, L> newArray = new HashMap<>();
        for (Entry<T, E> entry: array.entrySet())
        { newArray.put(entry.getKey(), function.apply(entry.getKey(), entry.getValue())); }

        return newArray;
    }
}
