package ontey.variable;

import lombok.NonNull;

public class Variable extends SimpleVariable {
   
   @NonNull
   public final VariableFormat format;
   
   public Variable(@NonNull String name, @NonNull VariableFormat format) {
      super(name);
      this.format = format;
   }
   
   public String format() {
      return format.format(name);
   }
}
