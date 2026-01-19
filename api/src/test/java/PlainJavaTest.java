import com.ontey.api.java.color.JavaColor;
import com.ontey.api.java.math.parser.MathParser;

public class PlainJavaTest {
   
   public static void main(String[] args) {
      System.out.print(JavaColor.GREEN);
      
      
      
      System.out.print(JavaColor.RESET);
   }
   
   @SuppressWarnings("SameParameterValue")
   private static void eval(String exp, MathParser parser) {
      
      MathParser.EvalResult result = parser.evaluateDebug(exp);
      String valOf = Double.valueOf(result.result()).toString();
      
      System.out.println("Tokens: " + result.tokens());
      System.out.println(result.result() % 1 != 0 ? valOf : valOf.substring(0, valOf.length() - 2));
   }
}
