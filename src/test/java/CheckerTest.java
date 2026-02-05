import ontey.check.Checker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerTest {
   
   @Test
   void checkArgument() {
      // Normal usage
      Checker.checkArgument(true);
      assertThrows(IllegalArgumentException.class, () -> Checker.checkArgument(false));
      
      // assigning null to @NonNull fields (more of a lombok test)
      assertThrows(NullPointerException.class, () -> Checker.checkArgument(true, null));
   }
   
   @Test
   void checkBounds() {
      // Normal usage
      Checker.checkBounds(1, 10, 4);
      Checker.checkBounds(1, 10, 1);
      assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBounds(1, 10, 11));
      
      // Wrong usage should throw
      assertThrows(IllegalArgumentException.class, () -> Checker.checkBounds(10, 1, 4));
   }
}
