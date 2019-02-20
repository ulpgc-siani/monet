package org.monet.bpi;

import java.util.Iterator;

public class Range {

	public static class PartialRange {

		int fromValue = 0;

		PartialRange(int fromValue) {
			this.fromValue = fromValue;
		}

		public Iterable<Integer> to(final int value) {
			return new Iterable<Integer>() {

				@Override
				public Iterator<Integer> iterator() {
					final int fromValue = PartialRange.this.fromValue;
					return new Iterator<Integer>() {

						int current = fromValue;

						@Override
						public void remove() {
						}

						@Override
						public Integer next() {
							return current++;
						}

						@Override
						public boolean hasNext() {
							return current < value;
						}
					};
				}
			};
		}

	}

	public static PartialRange from(int value) {
		return new PartialRange(value);
	}

}
