package ontey.api.config.comment.format;

import ontey.api.config.util.StringUtils;

import java.util.Objects;

public class CommentFormatterConfig {
	
	private String prefixFirst, prefixMultiline, suffixMultiline, suffixLast;
	
	public CommentFormatterConfig prefix(String prefix) {
		return prefix(prefix, prefix);
	}
	
	public CommentFormatterConfig prefix(String prefixFirst, String prefixMultiline) {
		this.prefixFirst = prefixFirst;
		this.prefixMultiline = prefixMultiline;
		return this;
	}
	
	public CommentFormatterConfig suffix(String suffixLast) {
		this.suffixLast = suffixLast;
		return this;
	}
	
	public CommentFormatterConfig suffix(String suffixLast, String suffixMultiline) {
		this.suffixLast = suffixLast;
		this.suffixMultiline = suffixMultiline;
		return this;
	}
	
	public String prefixFirst(String defaultPrefix) {
		return this.prefixFirst != null ? this.prefixFirst : defaultPrefix;
	}
	
	public String prefixFirst() {
		return prefixFirst("");
	}
	
	public String prefixMultiline(String defaultPrefix) {
		return this.prefixMultiline != null ? this.prefixMultiline : prefixFirst(defaultPrefix);
	}
	
	public String prefixMultiline() {
		return prefixMultiline("");
	}
	
	public String suffixMultiline(String defaultSuffix) {
		return this.suffixMultiline != null ? this.suffixMultiline : defaultSuffix;
	}
	
	public String suffixMultiline() {
		return suffixMultiline("");
	}
	
	public String suffixLast(String defaultSuffix) {
		return this.suffixLast != null ? this.suffixLast : defaultSuffix;
	}
	
	public String suffixLast() {
		return suffixLast("");
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CommentFormatterConfig that = (CommentFormatterConfig) o;
		return Objects.equals(this.prefixFirst, that.prefixFirst)
		  && Objects.equals(this.prefixMultiline, that.prefixMultiline)
		  && Objects.equals(this.suffixMultiline, that.suffixMultiline)
		  && Objects.equals(this.suffixLast, that.suffixLast);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.prefixFirst, this.prefixMultiline, this.suffixMultiline, this.suffixLast);
	}
	
	@Override
	public String toString() {
		return StringUtils.quoteNewLines("{" +
		  "prefixFirst='" + this.prefixFirst + '\'' +
		  ", prefixMultiline='" + this.prefixMultiline + '\'' +
		  ", suffixMultiline='" + this.suffixMultiline + '\'' +
		  ", suffixLast='" + this.suffixLast + '\'' +
		  '}');
	}
}
