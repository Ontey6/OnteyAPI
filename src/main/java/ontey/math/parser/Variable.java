package ontey.math.parser;

import lombok.*;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Variable {
   
   @NonNull
   private final String name;
   
   @Setter
   private double value;
   
   @NonNull
   public static Variable of(@NonNull String name, double value) {
      Objects.requireNonNull(name, "name");
      
      return new Variable(name, value);
   }
}
