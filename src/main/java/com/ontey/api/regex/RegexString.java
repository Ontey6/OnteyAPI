package com.ontey.api.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record RegexString(String str) {
   
   public String replaceAll(String target, String replacementFormat, Function<String, String> replacement) {
      return replaceAll(target, List.of(replacementFormat), list -> replacement.apply(list[0]));
   }
   
   public String replaceAll(String target, List<String> replacementFormat, Function<String[], String> replacement) {
      Matcher matcher = Pattern.compile(target).matcher(str);
      StringBuilder sb = new StringBuilder();
      
      while(matcher.find()) {
         int count = matcher.groupCount();
         String[] groups = new String[count + 1];
         
         for(int i = 0; i <= count; i++)
            groups[i] = matcher.group(i);
         
         List<String> replaced = new ArrayList<>(replacementFormat);
         for(String rep : replacementFormat)
            for(int i = 0; i < groups.length; i++)
               replaced.set(i, rep.replace("$" + i, groups[i] != null ? groups[i] : ""));
         
         matcher.appendReplacement(
           sb,
           Matcher.quoteReplacement(
             replacement.apply(
               replaced.toArray(new String[0])
             )
           )
         );
      }
      
      matcher.appendTail(sb);
      return sb.toString();
   }
}