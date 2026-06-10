package ontey.api.input;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A utility to query input from somewhere or someone.
 * An instance can perform multiple queries.
 *
 * @param <S> The source - the somewhere or someone - to query from
 * @param <T> The return type of the query
 */

public interface Input<S, T> {
	
	/**
	 * Queries the input
	 *
	 * @param source The source to query from
	 * @return A {@link CompletableFuture}.
	 * Call {@link CompletableFuture#thenAccept(Consumer)} or {@link CompletableFuture#thenAcceptAsync(Consumer)}
	 * to specify the action to run when the input was completed.
	 * The return value of the future may be null, e.g. if a connection was interrupted, canceled, etc.
	 */
	
	@NonNull
	CompletableFuture<@Nullable T> queryInput(@UnknownNullability S source);
}
