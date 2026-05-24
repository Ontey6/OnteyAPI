package ontey.api.lambda;

import lombok.NonNull;

import java.util.function.Function;

/**
 * A {@link Function} that returns a {@link RuntimeException} and takes a {@link Throwable} (The cause) as input.
 */

@FunctionalInterface
public interface ExceptionProvider extends Function<@NonNull Throwable, @NonNull RuntimeException> {

}
