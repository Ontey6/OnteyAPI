package ontey.lambda;

import lombok.NonNull;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionWrapper extends Function<@NonNull Throwable, @NonNull RuntimeException> { }
