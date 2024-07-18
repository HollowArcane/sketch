package bin.graphics.animation;

import java.util.function.Function;

public class Animation<E extends Animatable<E>>
{
    private long lastMillis;
    private long duration;
    private float progression;
    private float delay;

    private E start, end;
    private Function<Float, Float> behaviour;

    public static final Function<Float, Float> BEHAVIOUR_LINEAR = x -> x;
    public static final Function<Float, Float> BEHAVIOUR_CUBIC = x -> x*x*(-2*x + 3);
    public static final Function<Float, Float> BEHAVIOUR_OVERSHOOT = x -> x*(-2*x + 3);
    public static final Function<Float, Float> BEHAVIOUR_EASE_IN = x -> x*(3 - x*(3 + x));
    public static final Function<Float, Float> BEHAVIOUR_EASE_OUT = x -> -(1-x)*(3 - (1-x)*(3 + 1-x));

    public Animation(E start, E end, int millis, Function<Float, Float> behaviour)
    {
        lastMillis = System.currentTimeMillis();
        progression = 0;
        delay = 0;
        duration = millis;

        this.behaviour = behaviour;

        this.start = start;
        this.end = end;
    }    

    public Animation(E start, E end, float seconds, Function<Float, Float> behaviour)
    { this(start, end, Math.round(seconds * 1000), behaviour); }    

    public static <E extends Animatable<E>> AnimationBuilder<E> from(E start)
    { return new AnimationBuilder<E>(start); }

    public void delay(float seconds)
    { delay(seconds * 1000); }

    public void delay(int millis)
    { delay = (float)millis/duration; }

    public boolean ended()
    { return progression >= 1; }

    public E next()
    {
        long currentMillis = System.currentTimeMillis();

        float deltatime = (float)(currentMillis - lastMillis)/duration;

        delay = Math.max(delay - deltatime, 0);
        progression = Math.clamp(progression + deltatime - delay, 0, 1);
        lastMillis = currentMillis;
        
        return start.interpolate(end, behaviour.apply(progression));
    }
}
