package ontey.api.config.yaml.implementation.api;

public enum QuoteStyle {
	
	/**
	 * Wrap values with single quotes.
	 * <br>
	 * <pre>{@code single: 'value'}</pre>
	 */
	
	SINGLE,
	
	/**
	 * Wrap values with double quotes.
	 * <br>
	 * <pre>{@code double: "value"}</pre>
	 */
	
	DOUBLE,
	
	/**
	 * Default style, without quotes when possible.
	 * <br>
	 * <pre>{@code plain: value}</pre>
	 * <br>
	 * If value have characters that must be escaped then {@link #SINGLE} quote style is used.
	 */
	
	PLAIN,
	
	/**
	 * <pre>{@code
	 * literal: |-
	 *   Each line
	 *   is literal
	 *   and joined with new lines
	 * }</pre>
	 */
	
	LITERAL,
	
	/**
	 * <pre>{@code
	 * folded: >-
	 *   Each line
	 *   is literal
	 *   and joined with spaces
	 * }</pre>
	 */
	
	FOLDED
}
