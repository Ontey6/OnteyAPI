package ontey.api.config.util;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class StringUtils {
	
	public static final String BLANK_LINE = "\n\n";
	
	public static final Pattern NEW_LINE = Pattern.compile("\\R"); // like "\r?\n" but also with other Unicode line separators
	
	public static final Pattern INDENTATION = Pattern.compile("^[^\\S\n]+", Pattern.MULTILINE); // all leading whitespace except new line
	
	public static final Pattern LIST_INDEX = Pattern.compile("^(.*)\\[(-?\\d+)]$", Pattern.DOTALL); // for list indexing: list[0]
	
	public static final char ESCAPE_CHAR = '\\';
	
	private static String SEPARATOR = ".";
	
	private static String ESCAPE_SEPARATOR = ESCAPE_CHAR + SEPARATOR;
	
	@Contract("null, _ -> null; !null, _ -> !null")
	public static String[] splitNewLines(@Nullable String s, int limit) {
		return s != null ? NEW_LINE.split(s, limit) : null;
	}
	
	@Contract("null, _ -> null; !null, _ -> !null")
	public static String[] lines(@Nullable String content, boolean stripTrailingNewLines) {
		return content != null ? splitNewLines(content, stripTrailingNewLines ? 0 : -1) : null;
	}
	
	@Contract("null -> null; !null -> !null")
	public static String[] lines(@Nullable String content) {
		return lines(content, true);
	}
	
	public static String indentation(int n) {
		return padding(n, ' ');
	}
	
	public static String padding(int n, char pad) {
		if(n <= 0)
			return "";
		
		char[] padding = new char[n];
		Arrays.fill(padding, pad);
		return new String(padding);
	}
	
	@Contract(value = "null -> null; !null -> !null", pure = true)
	public static String stripIndentation(@Nullable String s) {
		return s != null ? INDENTATION.matcher(s).replaceAll("") : null;
	}
	
	@Contract("null, _ -> null; !null, _ -> !null")
	public static String stripPrefix(@Nullable String s, @Nullable String prefix) {
		return stripPrefix(s, prefix, null);
	}
	
	@Contract("null, _, _ -> null; !null, _, _ -> !null")
	public static String stripPrefix(@Nullable String s, @Nullable String prefix, @Nullable String defaultPrefix) {
		if(s == null)
			return null;
		
		int skip = 0;
		if(prefix != null && s.startsWith(prefix))
			skip = prefix.length();
		else if(defaultPrefix != null && s.startsWith(defaultPrefix))
			skip = defaultPrefix.length();
		
		return s.substring(skip);
	}
	
	@Contract("null -> null; !null -> !null")
	public static String afterNewLine(@Nullable String s) {
		if(s == null)
			return null;
		
		int nl = s.indexOf('\n');
		return nl >= 0 ? s.substring(nl + 1) : "";
	}
	
	@Contract("null -> null; !null -> !null")
	public static @Nullable String[] splitTrailingNewLines(@Nullable String s) {
		if(s == null)
			return null;
		
		final String[] parts = new String[2];
		int i = s.length() - 1;
		while(i >= 0 && s.charAt(i) == '\n') {
			i--;
		}
		parts[0] = i >= 0 ? s.substring(0, i + 1) : "";
		parts[1] = s.substring(i + 1);
		return parts;
	}
	
	@Contract(pure = true)
	public static int lastSeparatorIndex(@NonNull String path, char sep, int fromIndex) {
		if(fromIndex < 0)
			fromIndex = 0;
		
		boolean escape = false;
		int len = path.length();
		int idx = -1;
		for(int i = fromIndex; i < len; i++) {
			char c = path.charAt(i);
			if(c == ESCAPE_CHAR) { // escape separator with \
				escape = !escape;
			} else {
				if(c == sep && !escape)
					idx = i;
				
				escape = false;
			}
		}
		return idx;
	}
	
	@Contract(pure = true)
	public static int lastSeparatorIndex(@NonNull String path, char sep) {
		return lastSeparatorIndex(path, sep, 0);
	}
	
	@Contract(pure = true)
	public static int firstSeparatorIndex(@NonNull String path, char sep, int fromIndex) {
		if(fromIndex < 0)
			fromIndex = 0;
		
		boolean escape = false;
		int len = path.length();
		for(int i = fromIndex; i < len; i++) {
			char c = path.charAt(i);
			
			if(c == ESCAPE_CHAR) // escape separator with \
				escape = !escape;
			else if(c == sep && !escape)
				return i;
			else
				escape = false;
		}
		return -1;
	}
	
	public static int firstSeparatorIndex(@NonNull String path, char sep) {
		return firstSeparatorIndex(path, sep, 0);
	}
	
	public static boolean allLinesArePrefixed(@Nullable String comment, String prefix) {
		String[] lines = lines(comment, false);
		return lines != null && Arrays.stream(lines).allMatch(line -> line.trim().startsWith(prefix));
	}
	
	@Contract(value = "null, _ -> false", pure = true)
	public static boolean allLinesArePrefixedOrBlank(@Nullable String comment, String prefix) {
		String[] lines = lines(comment);
		return lines != null && Arrays.stream(lines).map(String::trim).allMatch(line -> line.isEmpty() || line.startsWith(prefix));
	}
	
	@Contract(value = "null -> null; !null -> !null", pure = true)
	public static String quoteNewLines(@Nullable String s) {
		return s != null ? NEW_LINE.matcher(s).replaceAll("\\\\n") : null;
	}
	
	@Contract(value = "null -> null; !null -> !null", pure = true)
	public static String stripCarriage(@Nullable String s) {
		return s != null ? s.replace("\r", "") : null;
	}
	
	@Contract(value = "null -> !null; !null -> !null", pure = true)
	public static String wrap(@Nullable String value) {
		return value == null ? "" : '\'' + value + '\'';
	}
	
	@Contract(pure = false)
	public static void setSeparator(char separator) {
		SEPARATOR = String.valueOf(separator);
		ESCAPE_SEPARATOR = ESCAPE_CHAR + SEPARATOR;
	}
	
	@Contract(value = "null -> null; !null -> !null", pure = true)
	public static String escape(@Nullable String s) {
		return s != null ? s.replace(SEPARATOR, ESCAPE_SEPARATOR) : null;
	}
}
