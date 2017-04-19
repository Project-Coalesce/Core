package com.coalesce.util;

import java.util.Objects;

public final class Pair<F, S> {

	private final F first;
	private final S second;

	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pair)) return false;
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(getFirst(), pair.getFirst()) &&
				Objects.equals(getSecond(), pair.getSecond());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getFirst(), getSecond());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Pair{");
		sb.append("first=").append(first);
		sb.append(", second=").append(second);
		sb.append('}');
		return sb.toString();
	}


	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<>(first, second);
	}

}
