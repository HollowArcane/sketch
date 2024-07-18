package bin.graphics.property;

import java.util.ArrayList;

import bin.graphics.animation.Animatable;
import bin.graphics.animation.Animation;
import bin.graphics.animation.AnimationBuilder;

public abstract class Property implements Animatable<Property>
{
    private ArrayList<Animation<Property>> animation;

    public Property()
    { animation = new ArrayList<>(); }

    public void animate(Animation<Property> animation)
    { this.animation.add(animation); }

    @SuppressWarnings("unchecked")
    public <E extends Property> E get(Class<E> clazz)
    {
        if(animation.size() > 0 && animation.getFirst().ended())
        {
            Animation<Property> current = animation.removeFirst();
            setCurrentValues(current.next());
        }

        if(animation.size() == 0)
        { return (E)this; }

        return (E)animation.getFirst().next();
    }

    protected abstract void setCurrentValues(Property p);

    public <T> AnimationBuilder<Property> set(Property end)
    { return new AnimationBuilder<Property>(this).to(end).onCreate(this::animate); }

    public <T> AnimationBuilder<Property> from(Property start)
    { return new AnimationBuilder<Property>(start).to(this).onCreate(this::animate); }
}
