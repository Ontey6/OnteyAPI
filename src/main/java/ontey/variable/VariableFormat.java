package ontey.variable;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class VariableFormat {
   
   @NonNull
   public String format, replacementFormat;
   
   @NonNull
   public String format(@NonNull String name) {
      return format.replace(replacementFormat, name);
   }
}