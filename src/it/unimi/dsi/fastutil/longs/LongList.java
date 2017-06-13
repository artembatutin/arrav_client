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
package it.unimi.dsi.fastutil.longs;
import java.util.List;
/** A type-specific {@link List}; provides some additional methods that use polymorphism to avoid (un)boxing.
	*
	* <P>Note that this type-specific interface extends {@link Comparable}: it is expected that implementing
	* classes perform a lexicographical comparison using the standard operator "less then" for primitive types,
	* and the usual {@link Comparable#compareTo(Object) compareTo()} method for objects.
	*
	* <P>Additionally, this interface strengthens {@link #listIterator()},
	* {@link #listIterator(int)} and {@link #subList(int,int)}.
	*
	* <P>Besides polymorphic methods, this interfaces specifies methods to copy into an array or remove contiguous
	* sublists. Although the abstract implementation of this interface provides simple, one-by-one implementations
	* of these methods, it is expected that concrete implementation override them with optimized versions.
	*
	* @see List
	*/
public interface LongList extends List<Long>, Comparable<List<? extends Long>>, LongCollection {
	/** Returns a type-specific iterator on the elements of this list.
	 *
	 * <p>Note that this specification strengthens the one given in {@link List#iterator()}.
	 * It would not be normally necessary, but {@link java.lang.Iterable#iterator()} is bizarrily re-specified
	 * in {@link List}.
	 *
	 * @return an iterator on the elements of this list.
	 */
	@Override
	LongListIterator iterator();
	/** Returns a type-specific list iterator on the list.
	 *
	 * @see #listIterator()
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator()}.
	 */
	@Deprecated
	LongListIterator longListIterator();
	/** Returns a type-specific list iterator on the list starting at a given index.
	 *
	 * @see #listIterator(int)
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #listIterator(int)}.
	 */
	@Deprecated
	LongListIterator longListIterator(int index);
	/** Returns a type-specific list iterator on the list.
	 *
	 * @see List#listIterator()
	 */
	LongListIterator listIterator();
	/** Returns a type-specific list iterator on the list starting at a given index.
	 *
	 * @see List#listIterator(int)
	 */
	LongListIterator listIterator(int index);
	/** Returns a type-specific view of the portion of this list from the index <code>from</code>, inclusive, to the index <code>to</code>, exclusive.
	 * @see List#subList(int,int)
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #subList(int,int)}.
	 */
	@Deprecated
	LongList longSubList(int from, int to);
	/** Returns a type-specific view of the portion of this list from the index <code>from</code>, inclusive, to the index <code>to</code>, exclusive.
	 *
	 * <P>Note that this specification strengthens the one given in {@link List#subList(int,int)}.
	 *
	 * @see List#subList(int,int)
	 */
	@Override
	LongList subList(int from, int to);
	/** Sets the size of this list.
	 *
	 * <P>If the specified size is smaller than the current size, the last elements are
	 * discarded. Otherwise, they are filled with 0/{@code null}/<code>false</code>.
	 *
	 * @param size the new size.
	 */
	void size(int size);
	/** Copies (hopefully quickly) elements of this type-specific list into the given array.
	 *
	 * @param from the start index (inclusive).
	 * @param a the destination array.
	 * @param offset the offset into the destination array where to store the first element copied.
	 * @param length the number of elements to be copied.
	 */
	void getElements(int from, long a[], int offset, int length);
	/** Removes (hopefully quickly) elements of this type-specific list.
	 *
	 * @param from the start index (inclusive).
	 * @param to the end index (exclusive).
	 */
	void removeElements(int from, int to);
	/** Add (hopefully quickly) elements to this type-specific list.
	 *
	 * @param index the index at which to add elements.
	 * @param a the array containing the elements.
	 */
	void addElements(int index, long a[]);
	/** Add (hopefully quickly) elements to this type-specific list.
	 *
	 * @param index the index at which to add elements.
	 * @param a the array containing the elements.
	 * @param offset the offset of the first element to add.
	 * @param length the number of elements to add.
	 */
	void addElements(int index, long a[], int offset, int length);
	/** Appends the specified element to the end of this list (optional operation).
	 * @see List#add(Object)
	 */
	@Override
	boolean add(long key);
	/** Inserts the specified element at the specified position in this list (optional operation).
	 * @see List#add(int,Object)
	 */
	void add(int index, long key);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	void add(int index, Long key);
	/** Inserts all of the elements in the specified type-specific collection into this type-specific list at the specified position (optional operation).
	 * @see List#addAll(int,Collection)
	 */
	boolean addAll(int index, LongCollection c);
	/** Inserts all of the elements in the specified type-specific list into this type-specific list at the specified position (optional operation).
	 * @see List#add(int,Object)
	 */
	boolean addAll(int index, LongList c);
	/** Appends all of the elements in the specified type-specific list to the end of this type-specific list (optional operation).
	 * @see List#add(int,Object)
	 */
	boolean addAll(LongList c);
	/** Replaces the element at the specified position in this list with the specified element (optional operation).
	 * @see List#set(int,Object)
	 */
	long set(int index, long k);
	/** Returns the element at the specified position in this list.
	 * @see List#get(int)
	 */
	long getLong(int index);
	/** Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#indexOf(Object)
	 */
	int indexOf(long k);
	/** Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#lastIndexOf(Object)
	 */
	int lastIndexOf(long k);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	Long get(int index);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	int indexOf(Object o);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	int lastIndexOf(Object o);
	/** {@inheritDoc}
	 * <p>This method specification is a workaround for
	 * <a href="http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8177440">bug 8177440</a>.
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	boolean add(Long k);
	/** Removes the element at the specified position in this list (optional operation).
	 * @see List#remove(int)
	 */
	long removeLong(int index);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	Long remove(int index);
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	Long set(int index, Long k);
}
