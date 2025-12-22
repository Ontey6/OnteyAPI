package com.ontey.api.color.minimessage;

import com.ontey.api.color.MiniMessageColor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

import static net.kyori.adventure.text.minimessage.internal.parser.TokenParser.*;

/**
 * A converter from Component to MiniMessage {@link String}.
 * <p>
 * Does a deep search for color/formatting on the {@link Component} and the {@code text} level;
 * Adds colors/formatting from tags in the {@code text} while keeping formatting from the {@link Component}.
 */

public class DeepMiniMessageSerializer {
   
   @Contract("null -> null; !null -> !null")
   public static String deepSerialize(Component cmp) {
      if(cmp == null)
         return null;
      
      String str = MiniMessageColor.MINI_MESSAGE.serializeOrNull(cmp);
      StringBuilder sb = new StringBuilder();
      
      for(int i = 0; i < str.length(); i++) {
         char c = str.charAt(i);
         
         if(c == ESCAPE && i + 1 < str.length()) {
            char next = str.charAt(i + 1);
            
            if(next == ESCAPE || next == TAG_START) {
               sb.append(ESCAPE);
               if(next == ESCAPE)
                  i++;
               continue;
            }
         }
         
         sb.append(c);
      }
      
      return sb.toString();
   }
}