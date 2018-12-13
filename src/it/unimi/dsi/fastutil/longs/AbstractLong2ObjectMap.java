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
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.Map;
/** An abstract class providing basic methods for maps implementing a type-specific interface.
	*
	* <P>Optional operations just throw an {@link
	* UnsupportedOperationException}. Generic versions of accessors delegate to
	* the corresponding type-specific counterparts following the interface rules
	* (they take care of returning {@code null} on a missing key).
	*
	* <P>As a further help, this class provides a {@link BasicEntry BasicEntry} inner class
	* that implements a type-specific version of {@link java.util.Map.Entry}; it
	* is particularly useful for those classes that do not implement their own
	* entries (e.g., most immutable maps).
	*/
public abstract class AbstractLong2ObjectMap <V> extends AbstractLong2ObjectFunction <V> implements Long2ObjectMap <V>, java.io.Serializable {
	private static final long serialVersionUID = -4940583368468432370L;
	protected AbstractLong2ObjectMap() {}
	@Override
	public boolean containsValue(Object v) {
	 return values().contains(v);
	}
	@Override
	public boolean containsKey(long k) {
	 ObjectIterator<Long2ObjectMap.Entry <V> > i = long2ObjectEntrySet().iterator();
	 while(i.hasNext())
	  if (i.next().getLongKey() == k)
	   return true;
	 return false;
	}
	/** Puts all pairs in the given map.
	 * If the map implements the interface of this map,
	 * it uses the faster iterators.
	 *
	 * @param m a map.
	 */
	 @SuppressWarnings({"unchecked","deprecation"})
	public void putAll(Map<? extends Long,? extends V> m) {
	 int n = m.size();
	 final Iterator<? extends Map.Entry<? extends Long,? extends V>> i = m.entrySet().iterator();
	 if (m instanceof Long2ObjectMap) {
	  Long2ObjectMap.Entry <? extends V> e;
	  while(n-- != 0) {
	   e = (Long2ObjectMap.Entry <? extends V>)i.next();
	   put(e.getLongKey(), e.getValue());
	  }
	 }
	 else {
	  Map.Entry<? extends Long,? extends V> e;
	  while(n-- != 0) {
	   e = i.next();
	   put(e.getKey(), e.getValue());
	  }
	 }
	}
	public boolean isEmpty() {
	 return size() == 0;
	}
	/** This class provides a basic but complete type-specific entry class for all those maps implementations
	 * that do not have entries on their own (e.g., most immutable maps).
	 *
	 * <P>This class does not implement {@link java.util.Map.Entry#setValue(Object) setValue()}, as the modification
	 * would not be reflected in the base map.
	 */
	public static class BasicEntry <V> implements Long2ObjectMap.Entry <V> {
	 protected long key;
	 protected V value;
	 public BasicEntry(final Long key, final V value) {
	  this.key = ((key).longValue());
	  this.value = (value);
	 }
	 public BasicEntry(final long key, final V value) {
	  this.key = key;
	  this.value = value;
	 }
	 /** {@inheritDoc}
		 * @deprecated Please use the corresponding type-specific method instead. */
	 @Deprecated
	 @Override
	 public Long getKey() {
	  return (Long.valueOf(key));
	 }
	 @Override
	 public long getLongKey() {
	  return key;
	 }
	 @Override
	 public V getValue() {
	  return (value);
	 }
	 @Override
	 public V setValue(final V value) {
	  throw new UnsupportedOperationException();
	 }
	 @Override
	 public boolean equals(final Object o) {
	  if (!(o instanceof Map.Entry)) return false;
	  final Map.Entry<?,?> e = (Map.Entry<?,?>)o;
	  if (e.getKey() == null || ! (e.getKey() instanceof Long)) return false;
	  return ( (key) == (((((Long)(e.getKey())).longValue()))) ) && ( (value) == null ? ((e.getValue())) == null : (value).equals((e.getValue())) );
	 }
	 @Override
	 public int hashCode() {
	  return it.unimi.dsi.fastutil.HashCommon.long2int(key) ^ ( (value) == null ? 0 : (value).hashCode() );
	 }
	 @Override
	 public String toString() {
	  return key + "->" + value;
	 }
	}
	/** Returns a type-specific-set view of the keys of this map.
	 *
	 * <P>The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the keys of this map; it may be safely cast to a type-specific interface.
	 */
	@Override
	public LongSet keySet() {
	 return new AbstractLongSet () {
	   public boolean contains(final long k) { return containsKey(k); }
	   public int size() { return AbstractLong2ObjectMap.this.size(); }
	   public void clear() { AbstractLong2ObjectMap.this.clear(); }
	   public LongIterator iterator() {
	    return new AbstractLongIterator () {
	      final ObjectIterator<Map.Entry<Long,V>> i = entrySet().iterator();
	      @Override
	      public long nextLong() { return ((Long2ObjectMap.Entry <V>)i.next()).getLongKey(); };
	      @Override
	      public boolean hasNext() { return i.hasNext(); }
	      @Override
	      public void remove() { i.remove(); }
	     };
	   }
	  };
	}
	/** Returns a type-specific-set view of the values of this map.
	 *
	 * <P>The view is backed by the set returned by {@link #entrySet()}. Note that
	 * <em>no attempt is made at caching the result of this method</em>, as this would
	 * require adding some attributes that lightweight implementations would
	 * not need. Subclasses may easily override this policy by calling
	 * this method and caching the result, but implementors are encouraged to
	 * write more efficient ad-hoc implementations.
	 *
	 * @return a set view of the values of this map; it may be safely cast to a type-specific interface.
	 */
	@Override
	public ObjectCollection <V> values() {
	 return new AbstractObjectCollection <V>() {
	   public boolean contains(final Object k) { return containsValue(k); }
	   public int size() { return AbstractLong2ObjectMap.this.size(); }
	   public void clear() { AbstractLong2ObjectMap.this.clear(); }
	   public ObjectIterator <V> iterator() {
	    return new AbstractObjectIterator <V>() {
	      final ObjectIterator<Map.Entry<Long,V>> i = entrySet().iterator();
	      /** {@inheritDoc}
							 * @deprecated Please use the corresponding type-specific method instead. */
	      @Deprecated
	      public V next() { return ((Long2ObjectMap.Entry <V>)i.next()).getValue(); };
	      public boolean hasNext() { return i.hasNext(); }
	     };
	   }
	  };
	}
	/** {@inheritDoc}
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ObjectSet<Map.Entry<Long, V>> entrySet() {
	 return (ObjectSet)long2ObjectEntrySet();
	}
	/** Returns a hash code for this map.
	 *
	 * The hash code of a map is computed by summing the hash codes of its entries.
	 *
	 * @return a hash code for this map.
	 */
	@Override
	public int hashCode() {
	 int h = 0, n = size();
	 final ObjectIterator<? extends Map.Entry<Long,V>> i = entrySet().iterator();
	 while(n-- != 0) h += i.next().hashCode();
	 return h;
	}
	@Override
	public boolean equals(Object o) {
	 if (o == this) return true;
	 if (! (o instanceof Map)) return false;
	 Map<?,?> m = (Map<?,?>)o;
	 if (m.size() != size()) return false;
	 return entrySet().containsAll(m.entrySet());
	}
	@Override
	public String toString() {
	 final StringBuilder s = new StringBuilder();
	 final ObjectIterator<? extends Map.Entry<Long,V>> i = entrySet().iterator();
	 int n = size();
	 Long2ObjectMap.Entry <V> e;
	 boolean first = true;
	 s.append("{");
	 while(n-- != 0) {
	  if (first) first = false;
	  else s.append(", ");
	  e = (Long2ObjectMap.Entry <V>)i.next();
	   s.append(String.valueOf(e.getLongKey()));
	  s.append("=>");
	  if (this == e.getValue()) s.append("(this map)"); else
	   s.append(String.valueOf(e.getValue()));
	 }
	 s.append("}");
	 return s.toString();
	}
}
