import static org.junit.jupiter.api.Assertions.*;

import ontey.math.parser.MathParser;
import ontey.math.parser.MathParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MathParserTest {
   
   private MathParser parser;
   
   @BeforeEach
   void setup() {
      parser = MathParser.advanced();
      parser.registerVariable("x", 3);
      parser.registerVariable("y", 4);
      
      parser.registerFunction("max", 2, args -> Math.max(args[0], args[1]));
   }
   
   private void assertEval(double expected, String expr) {
      double actual = parser.evaluate(expr);
      assertEquals(expected, actual, 1e-9, expr);
   }
   
   // ---- numbers & operators ----
   
   @Test void literals() {
      assertEval(5, "5");
      assertEval(-5, "-5");
      assertEval(5, "+5");
   }
   
   @Test void precedence() {
      assertEval(14, "2+3*4");
      assertEval(20, "(2+3)*4");
   }
   
   @Test void powerRightAssociative() {
      assertEval(512, "2^3^2");
   }
   
   @Test void unaryVsPower() {
      assertEval(-4, "-2^2");
      assertEval(4, "(-2)^2");
   }
   
   // ---- implicit multiplication ----
   
   @Test void implicitMultiplication() {
      assertEval(6, "2(3)");
      assertEval(6, "(2)(3)");
      assertEval(12, "3x+3");
      assertEval(14, "2(3+4)");
      assertEval(24, "2(3)(y)");
      assertEval(24, "2(3)y");
   }
   
   // ---- variables ----
   
   @Test void variables() {
      assertEval(7, "x+y");
      assertEval(14, "2*x+2*y");
   }
   
   // ---- functions ----
   
   @Test void builtInFunctions() {
      assertEval(0, "sin(0)");
      assertEval(1, "cos(0)");
      assertEval(3, "sqrt(9)");
   }
   
   @Test void customFunction() {
      assertEval(4, "max(3;4)");
   }
   
   @Test void nestedFunctions() {
      assertEval(3, "sqrt(max(9;4))");
   }
   
   // ---- errors ----
   
   @Test void undefinedVariable() {
      assertThrows(MathParserException.class, () -> parser.evaluate("z"));
   }
   
   @Test void undefinedFunction() {
      assertThrows(MathParserException.class, () -> parser.evaluate("foo(1)"));
   }
   
   @Test void mismatchedParentheses() {
      assertThrows(MathParserException.class, () -> parser.evaluate("(1+2"));
   }
   
   @Test void wrongFunctionArity() {
      assertThrows(MathParserException.class, () -> parser.evaluate("sin(1;2)"));
   }
   
   @Test void ambiguousNaming() {
      assertThrows(MathParserException.class, () -> parser.registerVariable("x", 0));
   }
}
