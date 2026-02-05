package ontey.config;

import java.util.Collections;
import static java.util.Collections.*;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ontey.check.Nullity;
import org.jetbrains.annotations.Nullable;

public final class SectionPathData {
   
   @Setter
   @Getter
   @Nullable
   private Object data;
   
   /**
    * If no comments exist, an empty list.
    * A null entry in the list represents an empty line.
    *
    * @return An unmodifiable list.
    */
   
   @Getter
   @NonNull
   private List<String> comments, inLineComments;
   
   public SectionPathData(@Nullable Object data) {
      this.data = data;
      this.comments = emptyList();
      this.inLineComments = emptyList();
   }
   
   /**
    * Represents the comments on a {@link ConfigSection} entry.
    * <p>
    * A null entry in the List is an empty line and an empty String entry is an
    * empty comment line. Any existing comments will be replaced, regardless of
    * what the new comments are.
    *
    * @param comments New comments to set every entry represents one line.
    */
   
   public void setComments(@Nullable final List<String> comments) {
      this.comments = Nullity.nonNullOr(comments, Collections::unmodifiableList, emptyList());
   }
   
   /**
    * Represents the comments on a {@link ConfigSection} entry.
    * <p>
    * A null entry in the List is an empty line and an empty String entry is an
    * empty comment line. Any existing comments will be replaced, regardless of
    * what the new comments are.
    *
    * @param inLineComments New comments to set every entry represents one
    * line.
    */
   
   public void setInLineComments(@Nullable final List<String> inLineComments) {
      this.inLineComments = Nullity.nonNullOr(inLineComments, Collections::unmodifiableList, emptyList());
   }
}
