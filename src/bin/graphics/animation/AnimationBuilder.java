package bin.graphics.animation;

import java.util.function.Consumer;
import java.util.function.Function;

public class AnimationBuilder<E extends Animatable<E>>
{
    E start;
    E end;

    Consumer<Animation<E>> onCreate;
    Function<Float, Float> behaviour;
    int durationMillis;

    public AnimationBuilder(E start)
    {
        this.start = start;
        this.end = start;

        behaviour = Animation.BEHAVIOUR_LINEAR;
        durationMillis = 0;
    }

    public AnimationBuilder<E> onCreate(Consumer<Animation<E>> onCreate)
    {
        this.onCreate = onCreate;
        return this;
    }

    public AnimationBuilder<E> from(E start)
    {
        this.start = start;
        return this;
    }

    public AnimationBuilder<E> to(E end)
    {
        this.end = end;
        return this;
    }

    public AnimationBuilder<E> with(Function<Float, Float> behaviour)
    {
        this.behaviour = behaviour;
        return this;
    } 

    public AnimationBuilder<E> in(int durationMillis)
    {
        this.durationMillis = durationMillis;
        return this;
    } 

    public AnimationBuilder<E> after(int durationMillis)
    {
        return this;
    }

    public Animation<E> create()
    {
        Animation<E> animation = new Animation<E>(start, end, durationMillis, behaviour);
        onCreate.accept(animation);
        return animation;
    }
}
