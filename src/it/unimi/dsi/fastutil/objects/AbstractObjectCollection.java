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
package it.unimi.dsi.fastutil.objects;
import java.util.AbstractCollection;
/** An abstract class providing basic methods for collections implementing a type-specific interface.
	*
	* <P>In particular, this class provide {@link #iterator()}, <code>add()</code>, {@link #remove(Object)} and
	* {@link #contains(Object)} methods that just call the type-specific counterpart.
	*
	* <p><strong>Warning</strong>: Because of a name clash between the list and collection interfaces
	* the type-specific deletion method of a type-specific abstract
	* collection is <code>rem()</code>, rather then <code>remove()</code>. A
	* subclass must thus override <code>rem()</code>, rather than
	* <code>remove()</code>, to make all inherited methods work properly.
	*/
public abstract class AbstractObjectCollection <K> extends AbstractCollection<K> implements ObjectCollection <K> {
	protected AbstractObjectCollection() {}
	/** {@inheritDoc}
	 *
	 * <p>This implementation delegates to the new covariantly stronger generic method {@link #iterator()}.
	 * @deprecated As of <code>fastutil</code> 5, replaced by {@link #iterator()}.
	 */
	@Deprecated
	@Override
	public ObjectIterator <K> objectIterator() {
	 return iterator();
	}
	@Override
	public abstract ObjectIterator <K> iterator();
	@Override
	public String toString() {
	 final StringBuilder s = new StringBuilder();
	 final ObjectIterator <K> i = iterator();
	 int n = size();
	 Object k;
	 boolean first = true;
	 s.append("{");
	 while(n-- != 0) {
	  if (first) first = false;
	  else s.append(", ");
	  k = i.next();
	  if (this == k) s.append("(this collection)"); else
	   s.append(String.valueOf(k));
	 }
	 s.append("}");
	 return s.toString();
	}
}
