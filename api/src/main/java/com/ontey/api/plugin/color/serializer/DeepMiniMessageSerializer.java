package com.ontey.api.plugin.color.serializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static com.ontey.api.plugin.color.MiniMessageColor.MINI_MESSAGE;
import static net.kyori.adventure.text.minimessage.internal.parser.TokenParser.*;

/**
 * A converter from Component to MiniMessage {@link String}.
 * <p>
 * Does a deep search for color/formatting on the {@link Component} and the {@code text} level;
 * Adds colors/formatting from tags in the {@code text} while keeping formatting from the {@link Component}.
 */

@NullMarked
public class DeepMiniMessageSerializer implements ComponentSerializer<Component, Component, String> {
   
   private DeepMiniMessageSerializer() { }
   
   private static final DeepMiniMessageSerializer instance = new DeepMiniMessageSerializer();
   
   public static DeepMiniMessageSerializer deepMiniMessage() {
      return instance;
   }
   
   public static String deepSerialize(Component component) {
      return instance.serialize(component);
   }
   
   @Override
   public Component deserialize(String input) {
      return MINI_MESSAGE.deserialize(input);
   }
   
   @Override
   public String serialize(Component component) {
      Objects.requireNonNull(component, "component");
      
      String str = MINI_MESSAGE.serializeOrNull(component);
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