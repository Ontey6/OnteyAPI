package ontey.api.config.yaml.implementation.api;

import lombok.NonNull;
import ontey.api.config.util.StringUtils;

public record QuoteValue<T>(T value, QuoteStyle quoteStyle) {
	
	@Override
	public @NonNull String toString() {
		return quoteStyle.toString() + "=" + (value == null ? "!!null" : StringUtils.quoteNewLines(value.toString()));
	}
}
