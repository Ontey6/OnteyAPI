package ontey.api.config.yaml.utils;

import ontey.api.config.util.StringUtils;
import ontey.api.config.yaml.file.YamlFile;
import org.cactoos.io.InputStreamOf;
import org.cactoos.io.ResourceOf;
import org.cactoos.io.TempFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public final class TestResources {
	
	public static URI getResourceURI(String file) throws URISyntaxException {
		return Objects.requireNonNull(getResourceURL(file).toURI());
	}
	
	public static URL getResourceURL(String file) {
		return TestResources.class.getClassLoader().getResource(file);
	}
	
	public static InputStream getResourceInputStream(String file) {
		return new InputStreamOf(new ResourceOf(file));
	}
	
	public static File tempFile() throws Exception {
		return new TempFile().value().toFile();
	}
	
	public static File tempFile(String contents) throws Exception {
		final File newTempFile = tempFile();
		
		try(FileWriter tempWriter = new FileWriter(newTempFile)) {
			tempWriter.write(contents);
		}
		
		return newTempFile;
	}
	
	public static String fileToStringUnix(YamlFile yamlFile) throws IOException {
		// Strip Windows carriage to ensure testable contents are the same as in Unix
		return StringUtils.stripCarriage(yamlFile.fileToString());
	}
	
	public static String testContent() {
		return """
		  test:
		    number: 5
		    string: Hello world
		    boolean: true
		    list:
		      - Each
		      - word
		      - will
		      - be
		      - in
		      - a
		      - separated
		      - entry
		  math:
		    pi: 3.141592653589793
		  timestamp:
		    canonicalDate: 2020-07-04T13:18:04.458Z
		    formattedDate: 04/07/2020 15:18:04
		  """;
	}
	
	public static String testHeader() {
		return """
		  ######################
		  ##  HEADER COMMENT  ##
		  ######################
		  
		  """;
	}
	
	public static String testWithHeader() {
		return testHeader() +
		  ("""
			 test:
			   number: 5
			   string: Hello world
			   boolean: true
			   list:
			     - Each
			     - word
			     - will
			     - be
			     - in
			     - a
			     - separated
			     - entry
			   wrap: '# this is not a comment'
			   blank: ''
			 math:
			   pi: 3.141592653589793
			 timestamp:
			   canonicalDate: 2020-07-04T13:18:04.458Z
			   formattedDate: 04/07/2020 15:18:04
			 """);
	}
	
	public static String testComments() {
		return testHeader() +
		  ("""
			 # Test comments
			 test:
			   number: 5
			   # Hello!
			   string: Hello world
			   boolean: true
			   # List of words
			   list:
			     - Each
			     - word
			     - will
			     - be
			     - in
			     - a
			     - separated
			     # Comment on a list item
			     - entry # :)
			   # This is a
			   # multiline comment
			   wrap: '# this is not a comment'
			 
			   blank: ''
			 
			 # Wonderful numbers
			 math:
			   pi: 3.141592653589793
			   # Side comment below
			 
			 # Some timestamps
			 timestamp:
			   # ISO
			   canonicalDate: 2020-07-04T13:18:04.458Z
			   # Date/Time with format
			   formattedDate: 04/07/2020 15:18:04 # dd/MM/yyyy HH:mm:ss
			 
			 # End
			 """);
	}
	
	public static String testCommentsSpecial() {
		return testHeader() +
		  ("""
			 # Test comments
			 test:
			   # Block #comment with # character
			   ' wrap ': ' # not a comment ' # Side #comment with # character
			   single: 'text with # character' # Side #comment with # character
			   double: 'text with # character'  # Side #comment with # character
			   es'cape: text with "#" character \\" # Side #comment with # character
			   es'cape2': text with '#' character \\" # Side #comment with # character
			   :es:cape3": 'This is a string ''''with a # character "inside of it' # Side #comment with # character
			   -? escape4: -'# not a \\#comment # Side #comment with # character
			   multiline: 'This is a string\\" \\" which got ''wrapped and also contains a     #
			     in its ''content.' # Side #comment with # character
			   # This is a # multiline'
			   # comment
			   multiline2: | # Side #comment with # character
			     'line one' # not a comment
			     line two # not a comment
			   special2: text"#"' # Side #comment with # character
			      # unexpected indentation comment but valid
			   special3: text'#''# not comment # Side #comment with # character
			   special4: text''#''# not comment # Side #comment with # character
			   deep0: # Side #comment with # character
			     deep1:
			       # Deep block comment
			       deep2: deep # Deep side comment
			       # Deep side comment below
			   entries: # Side #comment with # character
			     # Comment on a list item with # character
			     - entry'#'' #:)
			     # dangling comment
			 
			 blank:
			   # Block #comment with # character
			 
			   # Multiple line comment with blank line
			   comment: 'text with # character' # Side #comment with # character
			 
			   # Multiple line comment
			   #  with blank line
			   empty: '' # Side #comment with # character
			            # Multiple line side comment
			 
			 explicit:
			   # This is explicit style
			   # Key comment 1
			   'this is a # multiline'' key': value # Value comment
			   # Key comment 2
			   key: |- # Value comment
			     this is a multiline
			     value with blank line
			   # Key comment 3
			   # Key comment 4
			   ? |
			     this is a # multiline'
			     key literal
			   : | # Value comment
			     this is a # multiline'
			     value literal
			   # Value comment below
			 
			 # Multiline
			 # footer with blank lines
			 """);
	}
	
	public static String testCommentsSingle() {
		return testHeader() +
		  ("""
			 # Test comments
			 'test':
			   'number': !!int '5'
			   # Hello!
			   'string': 'Hello world'
			   'boolean': !!bool 'true'
			   # List of words
			   'list':
			     - 'Each'
			     - 'word'
			     - 'will'
			     - 'be'
			     - 'in'
			     - 'a'
			     - 'separated'
			     # Comment on a list item
			     - 'entry' # :)
			   # This is a
			   # multiline comment
			   'wrap': '# this is not a comment'
			 
			   'blank': ''
			 
			 # Wonderful numbers
			 'math':
			   'pi': !!float '3.141592653589793'
			   # Side comment below
			 
			 # Some timestamps
			 'timestamp':
			   # ISO
			   'canonicalDate': !!timestamp '2020-07-04T13:18:04.458Z'
			   # Date/Time with format
			   'formattedDate': '04/07/2020 15:18:04' # dd/MM/yyyy HH:mm:ss
			 
			 # End
			 """);
	}
	
	public static String testCommentsLiteral() {
		return testHeader() +
		  ("""
			 # Test comments
			 "test":
			   "number": !!int |-
			     5
			   # Hello!
			   "string": |-
			     Hello world
			   "boolean": !!bool |-
			     true
			   # List of words
			   "list":
			     - |-
			       Each
			     - |-
			       word
			     - |-
			       will
			     - |-
			       be
			     - |-
			       in
			     - |-
			       a
			     - |-
			       separated
			     # Comment on a list item
			     - |- # :)
			       entry
			   # This is a
			   # multiline comment
			   "wrap": |-
			     # this is not a comment
			 
			   "blank": ""
			 
			 # Wonderful numbers
			 "math":
			   "pi": !!float |-
			     3.141592653589793
			   # Side comment below
			 
			 # Some timestamps
			 "timestamp":
			   # ISO
			   "canonicalDate": !!timestamp |-
			     2020-07-04T13:18:04.458Z
			   # Date/Time with format
			   "formattedDate": |- # dd/MM/yyyy HH:mm:ss
			     04/07/2020 15:18:04
			 
			 # End
			 """);
	}
	
	public static String testCommentsFolded() {
		return testCommentsLiteral().replace(" |-", " >-");
	}
}
