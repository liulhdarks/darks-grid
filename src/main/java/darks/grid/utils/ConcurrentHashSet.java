/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package darks.grid.utils;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<E> extends AbstractSet<E> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4868544861254524679L;

    private static final Object PRESENT = new Object();

    private transient ConcurrentHashMap<E, Object> data = null;

    public ConcurrentHashSet() {
        data = new ConcurrentHashMap<E, Object>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        data = new ConcurrentHashMap<E, Object>(initialCapacity);
    }

    public ConcurrentHashSet(Collection<E> c) {
        data = new ConcurrentHashMap<E, Object>(c.size());
        addAll(c);
    }


    @Override
    public Iterator<E> iterator() {
        return data.keySet().iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ConcurrentHashSet) {
            ConcurrentHashSet<E> os = (ConcurrentHashSet<E>) o;
            return data.equals(os.data);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return data.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o) != null;
    }

    @Override
    public void clear() {
        data.clear();
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(data.size());

        // Write out all elements in the proper order.
        for (E e : data.keySet())
            s.writeObject(e);
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException,
            ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        int size = s.readInt();
        data = new ConcurrentHashMap<E, Object>(size);

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++) {
            E e = (E) s.readObject();
            data.put(e, PRESENT);
        }
    }
}
