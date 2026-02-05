package ontey.variable;

import lombok.NonNull;

public class SimpleVariable {
   
   @NonNull
   public final String name;
   
   public SimpleVariable(@NonNull String name) {
      this.name = name;
   }
}
