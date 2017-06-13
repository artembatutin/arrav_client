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
/**  An abstract class facilitating the creation of type-specific {@linkplain it.unimi.dsi.fastutil.BidirectionalIterator bidirectional iterators}.
	*
	* <P>To create a type-specific bidirectional iterator, besides what is needed
	* for an iterator you need both a method returning the previous element as
	* primitive type and a method returning the previous element as an
	* object. However, if you inherit from this class you need just one (anyone).
	*
	* <P>This class implements also a trivial version of {@link #back(int)} that
	* uses type-specific methods.
	*/
public abstract class AbstractLongBidirectionalIterator extends AbstractLongIterator implements LongBidirectionalIterator {
	protected AbstractLongBidirectionalIterator() {}
	/** <p>This implementation delegates to {@link #previous()}. */
	@Override
	public long previousLong() { return previous().longValue(); }
	/** {@inheritDoc}
	 * <p>This implementation delegates to the corresponding type-specific method.
	 * @deprecated Please use the corresponding type-specific method instead. */
	@Deprecated
	@Override
	public Long previous() { return Long.valueOf(previousLong()); }
	/** {@inheritDoc}
	 *  <P>This implemntation iterates the type-specific version of {@link #previous()} for
	 * at most {@code n} times, stopping if {@link #hasPrevious()} becomes false.
	 *
	 * @param n the number of elements to skip backwards.
	 * @return the number of elements actually skipped.
	 * @see #previous()
	 */
	@Override
	public int back(final int n) {
	 int i = n;
	 while(i-- != 0 && hasPrevious()) previousLong();
	 return n - i - 1;
	}
}
