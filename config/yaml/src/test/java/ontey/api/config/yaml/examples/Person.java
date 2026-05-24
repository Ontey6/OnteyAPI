package ontey.api.config.yaml.examples;

import lombok.Getter;
import ontey.api.config.serialization.ConfigSerializable;
import ontey.api.config.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that represents a Person that is used only for the test examples.
 * <br>
 * Note that has methods to serialize and deserialize Person objects to save them later.
 */

@Getter
@SerializableAs("Person") // this is an alias to org.simpleyaml.examples.Person
public class Person implements ConfigSerializable {
	
	private final String id;
	
	private final String name;
	
	private final int birthYear;
	
	private boolean isAlive;
	
	public Person(String id, String name, int birthYear, boolean isAlive) {
		this.id = id;
		this.name = name;
		this.birthYear = birthYear;
		this.isAlive = isAlive;
	}
	
	public Person(String id, String name, int birthYear) {
		this(id, name, birthYear, true);
	}
	
	/*
	 * The following methods allows you to serialize and deserialize
	 * your object to save them correctly to a YAML file.
	 *
	 * If you want you can create a constructor that accepts a single Map<String, Object>
	 * to deserialize instead using the method deserialize of below.
	 */
	public static Person deserialize(Map<String, Object> mappedObject) { // note that is static
		return new Person((String) mappedObject.get("id"),
		  (String) mappedObject.get("name"),
		  (int) mappedObject.get("birthYear"),
		  (boolean) mappedObject.get("isAlive")
		);
	}
	
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> mappedObject = new LinkedHashMap<>();
		mappedObject.put("id", this.id);
		mappedObject.put("name", this.name);
		mappedObject.put("birthYear", this.birthYear);
		mappedObject.put("isAlive", this.isAlive);
		return mappedObject;
	}
	
	public void kill() {
		this.isAlive = false;
	}
	
	@Override
	public String toString() {
		return "Person [id= " + this.id + ", name= " + this.name + ", birthYear= " + this.birthYear + ", isAlive= " + this.isAlive + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return birthYear == person.birthYear && isAlive == person.isAlive && Objects.equals(id, person.id) && Objects.equals(name, person.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name, birthYear, isAlive);
	}
}
