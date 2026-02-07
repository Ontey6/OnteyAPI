package ontey.loader;

import ontey.lambda.ThrowingFunction;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Constructor;

@ApiStatus.Internal
public record ConstructionStrategy<T> (ThrowingFunction<Constructor<T>, T> function, Class<?>... params) { }
