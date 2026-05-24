package ontey.api.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckerTest {
	
	@Test
	void checkArgument() {
		// Normal usage
		Checker.checkArgument(true);
		assertThrows(IllegalArgumentException.class, () -> Checker.checkArgument(false));
	}
	
	@Test
	void checkBounds() {
		// Normal usage
		Checker.checkBounds(1, 10, 4);
		Checker.checkBounds(1, 10, 1);
		assertThrows(IndexOutOfBoundsException.class, () -> Checker.checkBounds(1, 10, 11));
		
		// Wrong usage should throw
		assertThrows(IllegalArgumentException.class, () -> Checker.checkBounds(10, 1, 4));
	}
	
	@Test
	void lenientFormatTest() {
		assertEquals("Hello, World!", Checker.lenientFormat("Hello, %s!", "World"));
		assertEquals("Hello, World!", Checker.lenientFormat("Hello, %s%s", "World", "!"));
		assertEquals("Hello, World%s", Checker.lenientFormat("Hello, %s%s", "World"));
		assertEquals("Hello, World! [argument]", Checker.lenientFormat("Hello, %s!", "World", "argument"));
	}
}
