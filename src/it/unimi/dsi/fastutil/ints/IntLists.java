/*
	* Copyright (C) 2002-2017 Sebastiano Vigna
	*
	* Licensed under the Apache License, Version 2.0 (the "License");
	* you may not use this file except in compliance with the License.
	* You may obtain a copy of the License at
	*
	*     http://www.apache.org/licenses/LICENSE-2.0
	*
	* Unless required by applicable law or agreed to in writing, software
	* distributed under the License is distributed on an "AS IS" BASIS,
	* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	* See the License for the specific language governing permissions and
	* limitations under the License.
	*/
package it.unimi.dsi.fastutil.ints;
import java.util.List;
import java.util.Collection;
import java.util.Random;
import java.util.RandomAccess;
/** A class providing static methods and objects that do useful things with type-specific lists.
	*
	* @see java.util.Collections
	*/
public class IntLists {
	private IntLists() {}
	/** Shuffles the specified list using the specified pseudorandom number generator.
	 *
	 * @param l the list to be shuffled.
	 * @param random a pseudorandom number generator (please use a <a href="http://dsiutils.dsi.unimi.it/docs/it/unimi/dsi/util/XorShiftStarRandom.html">XorShift*</a> generator).
	 * @return <code>l</code>.
	 */
	public static IntList shuffle(final IntList l, final Random random) {
	 for(int i = l.size(); i-- != 0;) {
	  final int p = random.nextInt(i + 1);
	  final int t = l.getInt(i);
	  l.set(i, l.getInt(p));
	  l.set(p, t);
	 }
	 return l;
	}
	/** An immutable class representing an empty type-specific list.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */
	public static class EmptyList extends IntCollections.EmptyCollection implements IntList , RandomAccess, java.io.Serializable, Cloneable {
	 private static final long serialVersionUID = -7046029254386353129L;
	 protected EmptyList() {}
	 @Override
	 public int getInt(int i) { throw new IndexOutOfBoundsException(); }
	 @Override
	 public boolean rem(int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public int removeInt(int i) { throw new UnsupportedOperationException(); }
	 @Override
	 public void add(final int index, final int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public int set(final int index, final int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public int indexOf(int k) { return -1; }
	 @Override
	 public int lastIndexOf(int k) { return -1; }
	 @Override
	 public boolean addAll(int i, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(IntList c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(int i, IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(int i, IntList c) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public void add(final int index, final Integer k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer get(final int index) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public boolean add(final Integer k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer set(final int index, final Integer k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer remove(int k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int indexOf(Object k) { return -1; }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int lastIndexOf(Object k) { return -1; }
	
	 public IntListIterator listIterator() { return IntIterators.EMPTY_ITERATOR; }
	
	 public IntListIterator iterator() { return IntIterators.EMPTY_ITERATOR; }
	
	 public IntListIterator listIterator(int i) { if (i == 0) return IntIterators.EMPTY_ITERATOR; throw new IndexOutOfBoundsException(String.valueOf(i)); }
	 /** {@inheritDoc}
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator()}. */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator() { return listIterator(); }
	 /** {@inheritDoc}
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator(int)}. */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator(int i) { return listIterator(i); }
	 public IntList subList(int from, int to) { if (from == 0 && to == 0) return this; throw new IndexOutOfBoundsException(); }
	 /* {@inheritDoc}
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #subList(int,int)}.
		 */
	 @Deprecated
	 @Override
	 public IntList intSubList(int from, int to) { return subList(from, to); }
	 @Override
	 public void getElements(int from, int[] a, int offset, int length) { if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) return; throw new IndexOutOfBoundsException(); }
	 @Override
	 public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
	 @Override
	 public void addElements(int index, final int a[], int offset, int length) { throw new UnsupportedOperationException(); }
	 @Override
	 public void addElements(int index, final int a[]) { throw new UnsupportedOperationException(); }
	 @Override
	 public void size(int s) { throw new UnsupportedOperationException(); }
	 @Override
	 public int compareTo(final List<? extends Integer> o) {
	  if (o == this) return 0;
	  return ((List<?>)o).isEmpty() ? 0 : -1;
	 }
	 @Override
	 public Object clone() { return EMPTY_LIST; }
	 @Override
	 public int hashCode() { return 1; }
	 @Override
	 @SuppressWarnings("rawtypes")
	 public boolean equals(Object o) { return o instanceof List && ((List)o).isEmpty(); }
	 @Override
	 public String toString() { return "[]"; }
	 private Object readResolve() { return EMPTY_LIST; }
	}
	/** An empty list (immutable). It is serializable and cloneable.
	 */

	public static final EmptyList EMPTY_LIST = new EmptyList();
	/** An immutable class representing a type-specific singleton list.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific list.
	 */
	public static class Singleton extends AbstractIntList implements RandomAccess, java.io.Serializable, Cloneable {
	 private static final long serialVersionUID = -7046029254386353129L;
	 private final int element;
	 private Singleton(final int element) {
	  this.element = element;
	 }
	 @Override
	 public int getInt(final int i) { if (i == 0) return element; throw new IndexOutOfBoundsException(); }
	 @Override
	 public boolean rem(int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public int removeInt(final int i) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean contains(final int k) { return ( (k) == (element) ); }
	 /* Slightly optimized w.r.t. the one in ABSTRACT_SET. */
	 @Override
	 public int[] toIntArray() {
	  int a[] = new int[1];
	  a[0] = element;
	  return a;
	 }
	 @Override
	 public IntListIterator listIterator() { return IntIterators.singleton(element); }
	 @Override
	 public IntListIterator iterator() { return listIterator(); }
	 @Override
	 public IntListIterator listIterator(int i) {
	  if (i > 1 || i < 0) throw new IndexOutOfBoundsException();
	  IntListIterator l = listIterator();
	  if (i == 1) l.next();
	  return l;
	 }
	 @Override
	
	 public IntList subList(final int from, final int to) {
	  ensureIndex(from);
	  ensureIndex(to);
	  if (from > to) throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
	  if (from != 0 || to != 1) return EMPTY_LIST;
	  return this;
	 }
	 @Override
	 public boolean addAll(int i, Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(final Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean removeAll(final Collection<?> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean retainAll(final Collection<?> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(IntList c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(int i, IntList c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(int i, IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(final IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean removeAll(final IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean retainAll(final IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public int size() { return 1; }
	 @Override
	 public void size(final int size) { throw new UnsupportedOperationException(); }
	 @Override
	 public void clear() { throw new UnsupportedOperationException(); }
	 @Override
	 public Object clone() { return this; }
	}
	/** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */
	public static IntList singleton(final int element) { return new Singleton (element); }
	/** Returns a type-specific immutable list containing only the specified element. The returned list is serializable and cloneable.
	 *
	 * @param element the only element of the returned list.
	 * @return a type-specific immutable list containing just <code>element</code>.
	 */
	public static IntList singleton(final Object element) { return new Singleton (((((Integer)(element)).intValue()))); }
	/** A synchronized wrapper class for lists. */
	public static class SynchronizedList extends IntCollections.SynchronizedCollection implements IntList , java.io.Serializable {
	 private static final long serialVersionUID = -7046029254386353129L;
	 protected final IntList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.
	 protected SynchronizedList(final IntList l, final Object sync) {
	  super(l, sync);
	  this.list = l;
	 }
	 protected SynchronizedList(final IntList l) {
	  super(l);
	  this.list = l;
	 }
	 @Override
	 public int getInt(final int i) { synchronized(sync) { return list.getInt(i); } }
	 @Override
	 public int set(final int i, final int k) { synchronized(sync) { return list.set(i, k); } }
	 @Override
	 public void add(final int i, final int k) { synchronized(sync) { list.add(i, k); } }
	 @Override
	 public int removeInt(final int i) { synchronized(sync) { return list.removeInt(i); } }
	 @Override
	 public int indexOf(final int k) { synchronized(sync) { return list.indexOf(k); } }
	 @Override
	 public int lastIndexOf(final int k) { synchronized(sync) { return list.lastIndexOf(k); } }
	 @Override
	 public boolean addAll(final int index, final Collection<? extends Integer> c) { synchronized(sync) { return list.addAll(index, c); } }
	 @Override
	 public void getElements(final int from, final int a[], final int offset, final int length) { synchronized(sync) { list.getElements(from, a, offset, length); } }
	 @Override
	 public void removeElements(final int from, final int to) { synchronized(sync) { list.removeElements(from, to); } }
	 @Override
	 public void addElements(int index, final int a[], int offset, int length) { synchronized(sync) { list.addElements(index, a, offset, length); } }
	 @Override
	 public void addElements(int index, final int a[]) { synchronized(sync) { list.addElements(index, a); } }
	 @Override
	 public void size(final int size) { synchronized(sync) { list.size(size); } }
	 @Override
	 public IntListIterator listIterator() { return list.listIterator(); }
	 @Override
	 public IntListIterator iterator() { return listIterator(); }
	 @Override
	 public IntListIterator listIterator(final int i) { return list.listIterator(i); }
	 /** Returns a type-specific list iterator on the list.
		 *
		 * @see #listIterator()
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator()}.
		 */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator() { return listIterator(); }
	 /** Returns a type-specific list iterator on the list.
		 *
		 * @see #listIterator()
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator(int)}.
		 */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator(final int i) { return listIterator(i); }
	 @Override
	 public IntList subList(final int from, final int to) { synchronized(sync) { return new SynchronizedList (list.subList(from, to), sync); } }
	 /* {@inheritDoc}
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #subList(int,int)}.
		 */
	 @Deprecated
	 @Override
	 public IntList intSubList(final int from, final int to) { return subList(from, to); }
	 @Override
	 public boolean equals(final Object o) { if (o == this) return true; synchronized(sync) { return collection.equals(o); } }
	 @Override
	 public int hashCode() { synchronized(sync) { return collection.hashCode(); } }
	 @Override
	 public int compareTo(final List<? extends Integer> o) { synchronized(sync) { return list.compareTo(o); } }
	 @Override
	 public boolean addAll(final int index, final IntCollection c) { synchronized(sync) { return list.addAll(index, c); } }
	 @Override
	 public boolean addAll(final int index, IntList l) { synchronized(sync) { return list.addAll(index, l); } }
	 @Override
	 public boolean addAll(IntList l) { synchronized(sync) { return list.addAll(l); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer get(final int i) { synchronized(sync) { return list.get(i); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public void add(final int i, Integer k) { synchronized(sync) { list.add(i, k); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer set(final int index, Integer k) { synchronized(sync) { return list.set(index, k); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer remove(final int i) { synchronized(sync) { return list.remove(i); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int indexOf(final Object o) { synchronized(sync) { return list.indexOf(o); } }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int lastIndexOf(final Object o) { synchronized(sync) { return list.lastIndexOf(o); } }
	 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	  synchronized(sync) { s.defaultWriteObject(); }
	 }
	}
	/** A synchronized wrapper class for random-access lists. */
	public static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess, java.io.Serializable {
	 private static final long serialVersionUID = 0L;
	 protected SynchronizedRandomAccessList(final IntList l, final Object sync) {
	  super(l, sync);
	 }
	 protected SynchronizedRandomAccessList(final IntList l) {
	  super(l);
	 }
	 @Override
	 public IntList subList(final int from, final int to) { synchronized(sync) { return new SynchronizedRandomAccessList (list.subList(from, to), sync); } }
	}
	/** Returns a synchronized type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */
	public static IntList synchronize(final IntList l) {
	 return l instanceof RandomAccess ? new SynchronizedRandomAccessList (l) : new SynchronizedList (l);
	}
	/** Returns a synchronized type-specific list backed by the given type-specific list, using an assigned object to synchronize.
	 *
	 * @param l the list to be wrapped in a synchronized list.
	 * @param sync an object that will be used to synchronize the access to the list.
	 * @return a synchronized view of the specified list.
	 * @see java.util.Collections#synchronizedList(List)
	 */
	public static IntList synchronize(final IntList l, final Object sync) {
	 return l instanceof RandomAccess ? new SynchronizedRandomAccessList (l, sync) : new SynchronizedList (l, sync);
	}
	/** An unmodifiable wrapper class for lists. */
	public static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements IntList , java.io.Serializable {
	 private static final long serialVersionUID = -7046029254386353129L;
	 protected final IntList list; // Due to the large number of methods that are not in COLLECTION, this is worth caching.
	 protected UnmodifiableList(final IntList l) {
	  super(l);
	  this.list = l;
	 }
	 @Override
	 public int getInt(final int i) { return list.getInt(i); }
	 @Override
	 public int set(final int i, final int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public void add(final int i, final int k) { throw new UnsupportedOperationException(); }
	 @Override
	 public int removeInt(final int i) { throw new UnsupportedOperationException(); }
	 @Override
	 public int indexOf(final int k) { return list.indexOf(k); }
	 @Override
	 public int lastIndexOf(final int k) { return list.lastIndexOf(k); }
	 @Override
	 public boolean addAll(final int index, final Collection<? extends Integer> c) { throw new UnsupportedOperationException(); }
	 @Override
	 public void getElements(final int from, final int a[], final int offset, final int length) { list.getElements(from, a, offset, length); }
	 @Override
	 public void removeElements(final int from, final int to) { throw new UnsupportedOperationException(); }
	 @Override
	 public void addElements(int index, final int a[], int offset, int length) { throw new UnsupportedOperationException(); }
	 @Override
	 public void addElements(int index, final int a[]) { throw new UnsupportedOperationException(); }
	 @Override
	 public void size(final int size) { list.size(size); }
	 @Override
	 public IntListIterator listIterator() { return IntIterators.unmodifiable(list.listIterator()); }
	 @Override
	 public IntListIterator iterator() { return listIterator(); }
	 @Override
	 public IntListIterator listIterator(final int i) { return IntIterators.unmodifiable(list.listIterator(i)); }
	 /** Returns a type-specific list iterator on the list.
		 *
		 * @see #listIterator()
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator()}.
		 */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator() { return listIterator(); }
	 /** Returns a type-specific list iterator on the list.
		 *
		 * @see #listIterator()
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator(int)}.
		 */
	 @Deprecated
	 @Override
	 public IntListIterator intListIterator(final int i) { return listIterator(i); }
	 public IntList subList(final int from, final int to) { return new UnmodifiableList (list.subList(from, to)); }
	 /* {@inheritDoc}
		 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #subList(int,int)}.
		 */
	 @Deprecated
	 @Override
	 public IntList intSubList(final int from, final int to) { return subList(from, to); }
	 @Override
	 public boolean equals(final Object o) { if (o == this) return true; return collection.equals(o); }
	 @Override
	 public int hashCode() { return collection.hashCode(); }
	 @Override
	 public int compareTo(final List<? extends Integer> o) { return list.compareTo(o); }
	 @Override
	 public boolean addAll(final int index, final IntCollection c) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(final IntList l) { throw new UnsupportedOperationException(); }
	 @Override
	 public boolean addAll(final int index, final IntList l) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer get(final int i) { return list.get(i); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public void add(final int i, Integer k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer set(final int index, Integer k) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Integer remove(final int i) { throw new UnsupportedOperationException(); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int indexOf(final Object o) { return list.indexOf(o); }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public int lastIndexOf(final Object o) { return list.lastIndexOf(o); }
	}
	/** An unmodifiable wrapper class for random-access lists. */
	public static class UnmodifiableRandomAccessList extends UnmodifiableList implements RandomAccess, java.io.Serializable {
	 private static final long serialVersionUID = 0L;
	 protected UnmodifiableRandomAccessList(final IntList l) {
	  super(l);
	 }
	 @Override
	 public IntList subList(final int from, final int to) { return new UnmodifiableRandomAccessList (list.subList(from, to)); }
	}
	/** Returns an unmodifiable type-specific list backed by the given type-specific list.
	 *
	 * @param l the list to be wrapped in an unmodifiable list.
	 * @return an unmodifiable view of the specified list.
	 * @see java.util.Collections#unmodifiableList(List)
	 */
	public static IntList unmodifiable(final IntList l) {
	 return l instanceof RandomAccess ? new UnmodifiableRandomAccessList (l) : new UnmodifiableList (l);
	}
}
