package ontey.api.javascript;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavascriptTest {
	
	private Javascript js;
	
	@BeforeEach
	void setup() {
		js = new Javascript();
	}
	
	@Test
	void basicJavascript() throws JavaScriptException {
		js.eval(
		  """
			 function testClosure() {
			   let count = 0
			   return function() {
			     count += 1
			     return count
			   }
			 }
			 
			 const counter = testClosure()
			 console.assert(counter() === 1, "Closure failed: initial increment")
			 console.assert(counter() === 2, "Closure failed: state persistence")
			 
			 
			 """
		);
	}
	
	@Test
	void javaType() throws JavaScriptException {
		js.eval(
		  """
			 const ArrayList = Java.type('java.util.ArrayList')
			 var list = new ArrayList()
			 list.add("Hello")
			 list.add("World!")
			 var String = Java.type("java.lang.String")
			 console.assert("Hello, World!" == String.join(", ", list), "Test ")
			 // Output: Hello, World!
			 
			 // static fields
			 const Math = Java.type("java.lang.Math")
			 console.assert(Math.PI == 3.141592653589793, "Math.PI")
			 
			 // destructuring
			 const { PI, E, max, sin } = Java.type('java.lang.Math');
			 console.assert(PI == 3.141592653589793, "destructured PI")
			 console.assert(max(10, 20) == 20, "destructured max")
			 """
		);
	}
	
	@Test
	void bindings() throws JavaScriptException {
		js
		  .addVariable("obj", new StringBuilder("Hello, World!"))
		  .addClass(StringBuilder.class);
		
		assertEquals("Hello, World!", js.eval("obj.toString()"));
		assertEquals("Hello, World!", js.eval("new StringBuilder('Hello, World!').toString()"));
	}
}
