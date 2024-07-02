package bin.graphics.property;

import bin.graphics.animation.Animatable;
import bin.graphics.animation.Animation;
import bin.graphics.animation.AnimationBuilder;

public abstract class Property implements Animatable<Property>
{
    private Animation<Property> animation;

    public Property()
    { animation = null; }

    public void animate(Animation<Property> animation)
    { this.animation = animation; }

    @SuppressWarnings("unchecked")
    public <E extends Property> E get(Class<E> clazz)
    {
        if(animation != null && animation.ended())
        { animation = null; }

        if(animation == null)
        { return (E)this; }

        return (E)animation.next();
    }

    public <T> AnimationBuilder<Property> set(Property end)
    { return new AnimationBuilder<Property>(this).to(end).onCreate(this::animate); }

    public <T> AnimationBuilder<Property> from(Property start)
    { return new AnimationBuilder<Property>(start).to(this).onCreate(this::animate); }
}
