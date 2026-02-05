package ontey.log;

import lombok.NonNull;

public final class Log {
   
   @NonNull
   public static NamedLogger of(@NonNull String name) {
      return new NamedLogger(name);
   }
}