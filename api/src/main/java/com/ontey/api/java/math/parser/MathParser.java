package com.ontey.api.java.math.parser;

import com.google.common.math.DoubleMath;
import com.ontey.api.java.math.MathUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

import static com.ontey.api.java.math.parser.MathParser.Config.DEFAULT;
import static com.ontey.api.java.math.parser.Token.Type.*;

// String var/func/op definition coming soon

@NullMarked
public class MathParser {
   private final Map<String, Variable> variables = new HashMap<>();
   private final Map<String, Function> functions = new HashMap<>();
   private final Map<String, Operator> operators = new HashMap<>();
   
   @UnknownNullability // to not confuse NullMarked
   private List<Token> tokens;
   private int currentTokenIndex;
   final Config config;
   
   private static final int UNARY_PRECEDENCE = 3;
   
   private MathParser() {
      this(DEFAULT);
   }
   
   private MathParser(Config config) {
      this.config = config;
   }
   
   public static MathParser plain() {
      return plain(DEFAULT);
   }
   
   public static MathParser plain(Config config) {
      return new MathParser(config);
   }
   
   public static MathParser simple() {
      return simple(DEFAULT);
   }
   
   public static MathParser simple(Config config) {
      return withPresets(config, Preset.SIMPLE_OPERATORS);
   }
   
   public static MathParser advanced() {
      return advanced(DEFAULT);
   }
   
   public static MathParser advanced(Config config) {
      return withPresets(config, Preset.ALL);
   }
   
   public static MathParser withPresets(Preset... presets) {
      return withPresets(DEFAULT, presets);
   }
   
   public static MathParser withPresets(Config config, Preset... presets) {
      MathParser out = new MathParser(config);
      
      for(var preset : presets)
         preset.apply(out);
      
      return out;
   }
   
   public MathParser registerVariable(Variable variable) {
      assertUniqueName(variable.getName());
      variables.put(variable.getName(), variable);
      return this;
   }
   
   public MathParser registerVariable(String name, double value) {
      return registerVariable(Variable.of(name, value));
   }
   
   public MathParser setVariable(String name, double value) {
      if(!variables.containsKey(name))
         throw new MathParserException("variable " + name + " has not been registered yet and therefore can't be set!");
      variables.remove(name);
      variables.put(name, Variable.of(name, value));
      return this;
   }
   
   public MathParser registerFunction(Function function) {
      assertUniqueName(function.getName());
      functions.put(function.getName(), function);
      return this;
   }
   
   public MathParser registerFunction(String name, int arity, java.util.function.Function<double[], Double> operation) {
      return registerFunction(new Function(name, arity, operation));
   }
   
   public MathParser registerFunction(String name, DoubleUnaryOperator operation) {
      return registerFunction(new Function(name, 1, args -> operation.applyAsDouble(args[0])));
   }
   
   public MathParser registerFunction(String name, BiFunction<Double, Double, Double> operation) {
      return registerFunction(new Function(name, 2, args -> operation.apply(args[0], args[1])));
   }
   
   public MathParser registerOperator(Operator operator) {
      assertUniqueName(operator.getName());
      operators.put(operator.getName(), operator);
      return this;
   }
   
   public MathParser registerOperator(String name, int precedence, boolean rightAssociative, BiFunction<Double, Double, Double> operation) {
      return registerOperator(Operator.of(name, precedence, rightAssociative, operation));
   }
   
   public MathParser registerOperator(String name, int precedence, BiFunction<Double, Double, Double> operation) {
      return registerOperator(name, precedence, false, operation);
   }
   
   public MathParser registerOperator(String name, BiFunction<Double, Double, Double> operation) {
      return registerOperator(name, 1, operation);
   }
   
   private void assertUniqueName(String name) {
      if(
        variables.containsKey(name)
          || operators.containsKey(name)
          || functions.containsKey(name)
      )
         throw new MathParserException("Ambiguous name: " + name);
   }
   
   public double evaluate(String expression) {
      Tokenizer tokenizer = new Tokenizer(expression, this);
      tokens = tokenizer.tokenize();
      currentTokenIndex = 0;
      
      double result = parseExpression(0);
      
      if(currentToken().getType() != END)
         throw new MathParserException("Unexpected token: " + currentToken());
      
      return result;
   }
   
   public EvalResult evaluateDebug(String expression) {
      double result = evaluate(expression);
      return new EvalResult(tokens, result);
   }
   
   private Token currentToken() {
      return tokens.get(currentTokenIndex);
   }
   
   private Token peekToken(int offset) {
      int index = currentTokenIndex + offset;
      return index < tokens.size() ? tokens.get(index) : new Token(END, null);
   }
   
   private void consumeToken() {
      currentTokenIndex++;
   }
   
   private double parseExpression(int minPrecedence) {
      double left = parsePrimary();
      
      while(isImplicitMultiplication() || (currentToken().getType() == OPERATOR && operators.containsKey(currentToken().getValue()))) {
         
         if(isImplicitMultiplication()) {
            int precedence = 2;
            if(precedence < minPrecedence)
               break;
            double right = parseExpression(precedence + 1);
            left *= right;
            continue;
         }
         
         String opId = currentToken().getValue();
         Operator op = operators.get(opId);
         
         if(op == null)
            throw new MathParserException("Undefined operator: " + opId);
         
         int precedence = op.getPrecedence();
         
         if(precedence < minPrecedence)
            break;
         
         consumeToken();
         
         int nextMinPrecedence = op.isRightAssociative() ? precedence : precedence + 1;
         double right = parseExpression(nextMinPrecedence);
         
         left = op.eval(left, right);
      }
      
      return left;
   }
   
   private boolean isImplicitMultiplication() {
      // get the token before because parsePrimary() already
      // consumed the current token as implicit multiplication
      // has 2 arguments, not 3.
      Token currentToken = peekToken(-1);
      Token nextToken;
      
      {
         int i = 1;
         while(peekToken(i).getType() == RIGHT_PARENTHESES)
            i++;
         nextToken = peekToken(i - 1);
      }
      
      Token.Type cur = currentToken.getType();
      Token.Type next = nextToken.getType();
      
      if(cur == RIGHT_PARENTHESES && (next == NUMBER || next == VARIABLE || next == LEFT_PARENTHESES))
         return true;
      
      if(cur == NUMBER && (next == VARIABLE || next == LEFT_PARENTHESES))
         return true;
      
      return cur == VARIABLE && (next == NUMBER || next == LEFT_PARENTHESES);
   }
   
   private double parsePrimary() {
      Token token = currentToken();
      
      if(token.getType() == NUMBER) {
         consumeToken();
         return token.getNumValue();
      }
      
      if(token.getType() == OPERATOR && token.getValue().equals("-")) {
         consumeToken();
         return -parseExpression(UNARY_PRECEDENCE);
      }
      
      if(token.getType() == OPERATOR && token.getValue().equals("+")) {
         consumeToken();
         return parsePrimary();
      }
      
      if(token.getType() == LEFT_PARENTHESES) {
         consumeToken();
         double result = parseExpression(0);
         if(currentToken().getType() != RIGHT_PARENTHESES)
            throw new MathParserException("Expected ')'");
         
         consumeToken();
         return result;
      }
      
      if(token.getType() == VARIABLE) {
         String name = token.getValue();
         consumeToken();
         
         Variable var = variables.get(name);
         if(var != null)
            return var.getValue();
         
         if(currentToken().getType() == LEFT_PARENTHESES && functions.containsKey(name))
               return parseFunctionCall(name);
         
         throw new MathParserException("Undefined variable: " + name);
      }
      
      throw new MathParserException("Unexpected token: " + token);
   }
   
   private double parseFunctionCall(String functionName) {
      Function func = functions.get(functionName);
      if(func == null)
         throw new MathParserException("Undefined function: " + functionName);
      
      consumeToken();
      
      List<Double> args = new ArrayList<>();
      if(currentToken().getType() != RIGHT_PARENTHESES) {
         args.add(parseExpression(0));
         while(currentToken().getType() == FUNCTION_DELIMITER) {
            consumeToken();
            args.add(parseExpression(0));
         }
      }
      
      if(currentToken().getType() != RIGHT_PARENTHESES)
         throw new MathParserException("Expected ')'");
      
      consumeToken();
      
      double[] argsArray = new double[args.size()];
      for(int i = 0; i < args.size(); i++)
         argsArray[i] = args.get(i);
      
      return func.apply(argsArray);
   }
   
   @AllArgsConstructor(access = AccessLevel.PRIVATE)
   public static class Preset {
      
      public static final Preset
        
        // Basic Operators
        ADDITION       = new Preset(p -> p.registerOperator("+", MathUtils::add)),
        SUBTRACTION    = new Preset(p -> p.registerOperator("-", MathUtils::subtract)),
        DIVISION       = new Preset(p -> p.registerOperator("/", 2, MathUtils::divide)),
        MULTIPLICATION = new Preset(p -> p.registerOperator("*", 2, MathUtils::multiply)),
      
        // Advanced Operators
        MODULO         = new Preset(p -> p.registerOperator("%", 2, MathUtils::modulo)),
        EXPONENTIATION = new Preset(p -> p.registerOperator("^", 4, true, MathUtils::pow)),
      
        // Presets
        SIMPLE_OPERATORS   = combine(ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION),
        ADVANCED_OPERATORS = combine(SIMPLE_OPERATORS, MODULO, EXPONENTIATION),
      
        // Functions
        SIN   = new Preset(p -> p.registerFunction("sin", Math::sin)),
        COS   = new Preset(p -> p.registerFunction("cos", Math::cos)),
        TAN   = new Preset(p -> p.registerFunction("tan", Math::tan)),
        SQRT  = new Preset(p -> p.registerFunction("sqrt", Math::sqrt)),
        ABS   = new Preset(p -> p.registerFunction("abs", Math::abs)),
        LOG   = new Preset(p -> p.registerFunction("ln", Math::log)),
        LOG10 = new Preset(p -> p.registerFunction("log10", Math::log10).registerFunction("log", Math::log10)),
        EXP   = new Preset(p -> p.registerFunction("exp", Math::exp)),
        FLOOR = new Preset(p -> p.registerFunction("floor", Math::floor)),
        CEIL  = new Preset(p -> p.registerFunction("ceil", Math::ceil)),
        FAC  = new Preset(p -> p.registerFunction("ceil", x -> DoubleMath.factorial(requireInt(x)))),
      
        // Preset
        DEFAULT_FUNCTIONS = combine(SIN, COS, TAN, SQRT, ABS, LOG, LOG10, EXP, FLOOR, CEIL),
        
        // All combined
        ALL = combine(ADVANCED_OPERATORS, DEFAULT_FUNCTIONS);
      
      private final Consumer<MathParser> action;
      
      private static final int MAX_FACTORIAL = 170;
      
      private static int requireInt(double x) {
         if(x % 1 != 0)
            throw new MathParserException("factorial function requires an integer, but got a double: " + x);
         
         if(x > MAX_FACTORIAL)
            throw new MathParserException("argument passed into factorial function is too large. Expected <" + MAX_FACTORIAL + ", got " + x);
         
         return (int) x;
      }
      
      private static Preset combine(Preset... presets) {
         return new Preset(parser -> {
            for(var preset : presets)
               preset.apply(parser);
         });
      }
      
      public void apply(MathParser parser) {
         action.accept(parser);
      }
   }
   
   public static class PresetCreator {
      
      public Preset of(Consumer<MathParser> action) {
         return new Preset(action);
      }
      
      public static Preset combined(Preset... presets) {
         return Preset.combine(presets);
      }
   }
   
   @AllArgsConstructor
   public static class Config {
      final char decimalSeparator;
      
      final List<Character> ignoredCharacters;
      
      static final Config DEFAULT = new Config('.', List.of('_', ','));
   }
   
   public record EvalResult(List<Token> tokens, double result) {
   
   }
}
