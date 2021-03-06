/**
 * Copyright (c) 2013, Institute of Information Systems (Sven Groppe and contributors of LUPOSDATE), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lupos.datastructures.dbmergesortedds;

import java.io.Serializable;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class MapEntry<K, V> implements Entry<K, V>, Serializable,
		Comparable<Entry<K, V>> {
	private static final long serialVersionUID = -1799734392680458236L;

	public MapEntry(final K key) {
		this.k = key;
	}

	public MapEntry(final K key, final V value) {
		this.k = key;
		this.v = value;
	}

	private final K k;
	private V v;

	@Override
	public K getKey() {
		return this.k;
	}

	@Override
	public V getValue() {
		return this.v;
	}

	@Override
	public V setValue(final V value) {
		final V res = this.v;
		this.v = value;
		return res;
	}

	@Override
	public boolean equals(final Object other) {
		return this.k.equals(((MapEntry<K, V>) other).k);
	}

	@Override
	public int compareTo(final Entry<K, V> other) {
		return ((Comparable<K>) this.k).compareTo(other.getKey());
	}

	@Override
	public String toString() {
		return this.k + " => " + this.v;
	}

	@Override
	public int hashCode() {
		return (int)(((long)this.k.hashCode() + this.v.hashCode())%Integer.MAX_VALUE);
	}
}
