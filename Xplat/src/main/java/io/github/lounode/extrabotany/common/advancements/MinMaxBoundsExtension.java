package io.github.lounode.extrabotany.common.advancements;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.math.BigInteger;
import java.util.Optional;

public final class MinMaxBoundsExtension {
	private MinMaxBoundsExtension() {}

	public record Longs(Optional<Long> min, Optional<Long> max) {
		public static final Longs ANY = new Longs(Optional.empty(), Optional.empty());
		private static final Codec<Longs> RANGE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.LONG.optionalFieldOf("min").forGetter(Longs::min),
				Codec.LONG.optionalFieldOf("max").forGetter(Longs::max)
		).apply(instance, Longs::new));
		public static final Codec<Longs> CODEC = Codec.either(Codec.LONG, RANGE_CODEC).xmap(
				value -> value.map(Longs::exactly, range -> range),
				range -> range.min.isPresent() && range.min.equals(range.max) ? Either.left(range.min.get()) : Either.right(range)
		).validate(range -> range.min.isPresent() && range.max.isPresent() && range.min.get() > range.max.get()
				? DataResult.error(() -> "Minimum must not exceed maximum") : DataResult.success(range));

		public static Longs exactly(long value) {
			return between(value, value);
		}

		public static Longs between(long min, long max) {
			return new Longs(Optional.of(min), Optional.of(max));
		}

		public static Longs atLeast(long min) {
			return new Longs(Optional.of(min), Optional.empty());
		}

		public static Longs atMost(long max) {
			return new Longs(Optional.empty(), Optional.of(max));
		}

		public boolean matches(long value) {
			return min.map(bound -> value >= bound).orElse(true) && max.map(bound -> value <= bound).orElse(true);
		}

		public boolean matchesSqr(long value) {
			var squared = BigInteger.valueOf(value);
			return min.map(bound -> squared.compareTo(BigInteger.valueOf(bound).pow(2)) >= 0).orElse(true)
					&& max.map(bound -> squared.compareTo(BigInteger.valueOf(bound).pow(2)) <= 0).orElse(true);
		}
	}
}
