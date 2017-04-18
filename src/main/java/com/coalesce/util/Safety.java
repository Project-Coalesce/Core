package com.coalesce.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Safety {

	private Safety() {}


	public static <O> void access(@Nullable O object, Consumer<@NotNull O> block) {
		if (object != null) block.accept(object);
	}

	public static <O, R> @Nullable R apply(@Nullable O object, Function<@NotNull O, R> block) {
		return object != null ? block.apply(object) : null;
	}

	public static <O> O ifNull(@Nullable O object, Supplier<O> block) {
		return object != null ? object : block.get();
	}


}
