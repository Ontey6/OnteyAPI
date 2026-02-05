package ontey.regex;

import lombok.NonNull;
import ontey.check.Checker;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record RegexString(String str) {
   
   @NonNull
   public String replaceAll(@Language("RegExp") @NonNull String target, @NonNull String replacementFormat, @NonNull Function<@NonNull String, @NonNull String> replacement) {
      return replaceAll(target, List.of(replacementFormat), list -> replacement.apply(list[0]));
   }
   
   public String replaceAll(@Language("RegExp") @NonNull String target, @NonNull List<@NonNull String> replacementFormat, @NonNull Function<@NonNull String @NonNull [], @NonNull String> replacement) {
      Matcher matcher = Pattern.compile(target).matcher(str);
      StringBuilder sb = new StringBuilder();
      
      while(matcher.find()) {
         int count = matcher.groupCount();
         String[] groups = new String[count + 1];
         
         for(int i = 0; i <= count; i++)
            groups[i] = matcher.group(i);
         
         List<String> replaced = new ArrayList<>(replacementFormat);
         for(String rep : replacementFormat) {
            Checker.checkNonNull(rep, "an element of replacementFormat is null!");
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