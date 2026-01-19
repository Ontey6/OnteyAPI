package com.ontey.api.java.math.parser;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.ontey.api.java.math.parser.Token.Type.*;

@RequiredArgsConstructor
class Tokenizer {
   private final String input;
   private final MathParser parser;
   private int pos = 0;
   
   List<Token> tokenize() {
      List<Token> tokens = new ArrayList<>();
      
      while(pos < input.length()) {
         char current = input.charAt(pos);
         
         if(Character.isWhitespace(current)) {
            pos++;
            continue;
         }
         
         if(Character.isDigit(current) || current == '.') {
            tokens.add(readNumber());
            continue;
         }
         
         if(Character.isLetter(current) || current == '_') {
            tokens.add(readIdentifier());
            continue;
         }
         
         switch(current) {
            case '(' -> {
               tokens.add(new Token(LEFT_PARENTHESES, "("));
               pos++;
            }
            case ')' -> {
               tokens.add(new Token(RIGHT_PARENTHESES, ")"));
               pos++;
            }
            case ';' -> {
               tokens.add(new Token(FUNCTION_DELIMITER, ";"));
               pos++;
            }
            default -> {
               tokens.add(new Token(OPERATOR, String.valueOf(current)));
               pos++;
            }
         }
      }
      
      tokens.add(new Token(END, ""));
      return tokens;
   }
   
   private Token readNumber() {
      while(pos < input.length() && input.charAt(pos) == '(')
         pos++;
      
      if(pos < input.length() && !Character.isDigit(input.charAt(pos)))
         throw new MathParserException("Number doesn't start with a digit!");
      
      StringBuilder sb = new StringBuilder();
      
      for(; pos < input.length(); pos++) {
         char cur = input.charAt(pos);
         
         if(parser.config.ignoredCharacters.contains(cur))
            continue;
         
         if(Character.isDigit(cur) || cur == parser.config.decimalSeparator) {
            sb.append(cur);
            continue;
         }
         
         break;
      }
      
      String numStr = sb.toString();
      return new Token(NUMBER, Double.parseDouble(numStr));
   }
   
   private Token readIdentifier() {
      int start = pos;
      while(pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_'))
         pos++;
      
      return new Token(VARIABLE, input.substring(start, pos));
   }
}
