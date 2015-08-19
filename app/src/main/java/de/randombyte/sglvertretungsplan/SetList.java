package de.randombyte.sglvertretungsplan;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author http://stackoverflow.com/a/7781697
 * Set and List somewhat combined
 */
public class SetList<E> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        return !contains(e) && super.add(e);
    }

    @Override
    public void add(int index, E e) {
        if (!contains(e)) {
            super.add(index, e);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Collection<E> copy = new ArrayList<E>(c);
        copy.removeAll(this);
        return super.addAll(index, copy);
    }

}
