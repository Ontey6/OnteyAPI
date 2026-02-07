package ontey.lambda;

public interface ThrowingFunction<T, R> {
   R apply(T t) throws Throwable;
}
