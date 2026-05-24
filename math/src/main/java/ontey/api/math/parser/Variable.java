package ontey.api.math.parser;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Variable {
	
	@NonNull
	private final String name;
	
	@Setter
	private double value;
	
	@NonNull
	public static Variable of(@NonNull String name, double value) {
		return new Variable(name, value);
	}
}
