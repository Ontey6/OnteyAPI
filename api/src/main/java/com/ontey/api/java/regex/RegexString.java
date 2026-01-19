package com.ontey.api.java.regex;

import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NullMarked
public record RegexString(String str) {
   
   public String replaceAll(@Language("RegExp") String target, String replacementFormat, Function<String, String> replacement) {
      return replaceAll(target, List.of(replacementFormat), list -> replacement.apply(list[0]));
   }
   
   public String replaceAll(@Language("RegExp") String target, List<String> replacementFormat, Function<String[], String> replacement) {
      Objects.requireNonNull(target, "target");
      Objects.requireNonNull(replacementFormat, "replacementFormat");
      Objects.requireNonNull(replacement, "replacement");
      
      Matcher matcher = Pattern.compile(target).matcher(str);
      StringBuilder sb = new StringBuilder();
      
      while(matcher.find()) {
         int count = matcher.groupCount();
         String[] groups = new String[count + 1];
         
         for(int i = 0; i <= count; i++)
            groups[i] = matcher.group(i);
         
         List<String> replaced = new ArrayList<>(replacementFormat);
         for(String rep : replacementFormat) {
            Objects.requireNonNull(rep, "element of replacementFormat");
            for(int i = 0; i < groups.length; i++)
               replaced.set(i, rep.replaceAll("\\$" + i, groups[i] != null ? Matcher.quoteReplacement(groups[i]) : ""));
         }
         
         matcher.appendReplacement(
           sb,
           Matcher.quoteReplacement(
             replacement.apply(
               replaced.toArray(String[]::new)
             )
           )
         );
      }
      
      matcher.appendTail(sb);
      return sb.toString();
   }
}