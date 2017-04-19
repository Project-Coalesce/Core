package com.coalesce.command.resolve;

import com.coalesce.command.base.CmdContext;
import com.coalesce.util.Safety;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public final class Resolver {

	private static final Map<Class<?>, BiFunction<CmdContext, String, ?>> FUNCTIONS = new HashMap<>();

	private Resolver() {}

	static {

		register(Player.class, (context, input) -> {

			final Set<Player> matches = new HashSet<>();

			for (Player player : Bukkit.getOnlinePlayers()) {
				String name = player.getName().toLowerCase();
				if (name.equalsIgnoreCase(input)) {
					matches.clear();
					return player;
				}

				String lowerIn = input.toLowerCase();
				if (name.startsWith(lowerIn) || name.endsWith(lowerIn) || (input.length() >= 3 && name
						.contains(lowerIn))) {
					matches.add(player);
				}
			}

			context.send(matches.isEmpty() ? "{error}Player {value}" + input + "{error} not found" : "");
			return null;
		});

	}


	/**
	 * Register a type resolver
	 *
	 * @param clazz The object class
	 * @param function The resolver
	 * @param <O> The type
	 */
	public static <O> void register(Class<O> clazz, BiFunction<CmdContext, String, O> function) {
		FUNCTIONS.put(clazz, function);
	}

	/**
	 * Resolve a type from an argument in a {@link CmdContext}
	 *
	 * @param clazz The object class
	 * @param context The target {@link CmdContext}
	 * @param index The index of the argument
	 * @param <O> The type
	 *
	 * @return The resolved type, or null
	 */
	@SuppressWarnings("unchecked")
	public static <O> O read(Class<O> clazz, CmdContext context, int index) {
		if (!context.hasArgs(index)) return null;
		final String input = context.argAt(index);

		if (clazz.isEnum()) {
			String fixed = input.replace("_", "");

			for (Enum<?> anEnum : ((Class<Enum<?>>) clazz).getEnumConstants()) {
				if (anEnum.name().replace("_", "").equalsIgnoreCase(fixed)) return ((O) anEnum);
			}
		}

		return ((O) Safety.apply(FUNCTIONS.get(clazz), cmdContextIntegerObjectBiFunction -> cmdContextIntegerObjectBiFunction.apply(context, input)));
	}

}
