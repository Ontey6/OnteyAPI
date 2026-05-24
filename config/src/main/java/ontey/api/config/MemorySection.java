package ontey.api.config;

import lombok.Getter;
import lombok.NonNull;
import ontey.api.config.serialization.ConfigSerializable;
import ontey.api.config.util.StringUtils;

import java.util.*;

/**
 * A type of {@link ConfigSection} that is stored in memory.
 * <br>
 * This implementation uses a {@link LinkedHashMap} to maintain insertion order of keys.
 *
 * @author Bukkit
 * @author Carlos Lazaro Costa
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/MemorySection.java">Bukkit Source</a>
 */

public class MemorySection implements ConfigSection {
	
	protected final Map<String, Object> map = new LinkedHashMap<>();
	
	@Getter
	private final Config root;
	
	@Getter
	private final ConfigSection parent;
	
	@Getter
	private final String name;
	
	@Getter
	private final String path;
	
	/**
	 * Creates an empty MemorySection for use as a root {@link Config} section.
	 * <br>
	 * Note that calling this without being yourself a {@link Config} will throw an exception!
	 * <br>
	 * Example usage (within a {@link Config} class):
	 * <pre>{@code
	 * public class MyConfig implements Config {
	 *     public MyConfig() {
	 *         super(); // Calls MemorySection() constructor
	 *     }
	 * }
	 * }</pre>
	 *
	 * @throws IllegalStateException Thrown if this is not a {@link Config} root.
	 */
	
	protected MemorySection() {
		if(!(this instanceof Config config))
			throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
		
		this.name = "";
		this.path = "";
		this.parent = null;
		this.root = config;
	}
	
	/**
	 * Creates an empty MemorySection with the specified parent and path.
	 * <br>
	 * This constructor is typically used internally when creating child sections.
	 *
	 * @param parent Parent section that contains this own section.
	 * @param name Path that you may access this section from via the root {@link Config}.
	 * @throws IllegalArgumentException Thrown if parent contains no root Configuration.
	 */
	
	protected MemorySection(@NonNull ConfigSection parent, @NonNull String name) {
		this.name = name;
		this.parent = parent;
		this.root = parent.getRoot();
		
		if(this.root == null)
			throw new IllegalArgumentException("Path cannot be orphaned");
		
		this.path = ConfigSection.createPath(parent, name);
	}
	
	private static boolean isSection(Object section) {
		return section instanceof ConfigSection || section instanceof ConfigSerializable || section instanceof Map<?, ?>;
	}
	
	private static Object findSection(Object section, String node) {
		if(section instanceof ConfigSection configSection) {
			section = configSection.get(node, null);
			if(section == null && configSection instanceof MemorySection mem) {
				section = mem.getDefault(node);
				if(section instanceof ConfigSection)
					section = configSection.createSection(node);
			}
		} else if(section instanceof Map<?, ?> map)
			section = map.get(node);
		
		return isSection(section) ? section : null;
	}
	
	private static Object find(Object section, String node) {
		Object it = null;
		if(section instanceof ConfigSection configSection) {
			it = configSection.get(node, null);
			if(it == null && configSection instanceof MemorySection mem)
				it = mem.getDefault(node);
		} else if(section instanceof Map<?, ?> map)
			it = map.get(node);
		
		return it;
	}
	
	private static Iterable<?> getIterable(Object section, String node) {
		if(node != null && !node.isEmpty())
			section = find(section, node);
		
		if(section instanceof ConfigSection configSection)
			section = configSection.getValues(false).values();
		
		return section instanceof Iterable<?> it ? it : null;
	}
	
	private static Object findIndexed(Object section, String iterableNode, int index) {
		return getIndexed(getIterable(section, iterableNode), index);
	}
	
	private static Object getIndexed(Iterable<?> iterable, int index) {
		if(iterable == null)
			return null;
		
		if(iterable instanceof Collection<?> collection) {
			var len = collection.size();
			
			index = asListIndex(index, len);
			if(index < 0 || index >= len)
				return null;
		}
		
		if(iterable instanceof List<?> list)
			return list.get(index); // O(1) for ArrayList, O(N) for LinkedList ...
		
		Object value = null;
		
		var it = iterable.iterator();
		
		if(index >= 0) {
			// Positive indexing of an iterable
			// time O(index + 1) <= O(N), N = iterable size
			
			var i = -1;
			
			while(it.hasNext()) {
				value = it.next();
				
				if(++i == index)
					break;
			}
			
			if(i != index)
				return null; // out of bounds
		} else {
			// Negative indexing of a non-Collection iterable
			// time O(N), N = iterable size
			// memory O(W) <= O(N), W = -index
			
			var window = new LinkedList<>();
			var windowSize = -index;
			var filled = 0;
			
			while(it.hasNext()) {
				window.add(it.next());
				
				if(filled == windowSize)
					window.removeFirst();
				else
					filled++;
			}
			
			if(filled < windowSize)
				return null; // out of bounds
			
			value = window.getFirst();
		}
		
		return value;
	}
	
	private static int asListIndex(int i, int size) {
		return i < 0 ? size + i : i;
	}
	
	@Override
	public @NonNull Set<String> getKeys(boolean deep) {
		var result = new LinkedHashSet<String>();
		
		if(root != null && root.options().copyDefaults()) {
			var defaults = this.getSectionInDefaults();
			
			if(defaults != null)
				result.addAll(defaults.getKeys(deep));
		}
		
		this.mapChildrenKeys(result, this, deep);
		
		return result;
	}
	
	@Override
	public @NonNull Map<String, Object> getValues(boolean deep) {
		var result = new LinkedHashMap<String, Object>();
		
		if(root != null && root.options().copyDefaults()) {
			var defaults = this.getSectionInDefaults();
			
			if(defaults != null)
				result.putAll(defaults.getValues(deep));
		}
		
		this.mapChildrenValues(result, this, deep);
		
		return result;
	}
	
	@Override
	public int size() {
		return this.map.size();
	}
	
	@Override
	public Object get(@NonNull String path) {
		return this.get(path, this.getDefault(path));
	}
	
	@Override
	public Object get(@NonNull String path, Object def) {
		if(path.isEmpty())
			return this;
		
		if(root == null)
			throw new IllegalStateException("Cannot access section without a root");
		
		var separator = root.options().pathSeparator();
		// i1 is the leading (higher) index
		// i2 is the trailing (lower) index
		int i1 = -1, i2;
		Object section = this;
		while((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) != -1) {
			var node = path.substring(i2, i1);
			section = this.getSection(section, node);
			if(section == null)
				return def;
		}
		
		var key = path.substring(i2);
		
		return this.getObject(section, key, def);
	}
	
	@Override
	public void set(@NonNull String path, Object value) {
		if(root == null)
			throw new IllegalStateException("Cannot use section without a root");
		
		var separator = root.options().pathSeparator();
		// i1 is the leading (higher) index
		// i2 is the trailing (lower) index
		int i1 = -1, i2;
		Object section = this;
		while((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) != -1) {
			var node = path.substring(i2, i1);
			var subSection = this.getSection(section, node);
			if(subSection == null) {
				if(section instanceof ConfigSection sections)
					section = sections.createSection(node);
				else
					return;
			} else
				section = subSection;
		}
		
		var key = path.substring(i2);
		
		this.setObject(section, key, value);
	}
	
	@Override
	public ConfigSection createSection(String path) {
		if(path == null || path.isEmpty())
			throw new IllegalArgumentException("Cannot create section at empty path");
		
		if(root == null)
			throw new IllegalStateException("Cannot create section without a root");
		
		var separator = root.options().pathSeparator();
		// i1 is the leading (higher) index
		// i2 is the trailing (lower) index
		int i1 = -1, i2;
		Object section = this;
		while((i1 = StringUtils.firstSeparatorIndex(path, separator, i2 = i1 + 1)) != -1) {
			var node = path.substring(i2, i1);
			var subSection = this.getSection(section, node);
			if(subSection == null) {
				if(section instanceof ConfigSection configSection)
					section = configSection.createSection(node);
				else
					return null;
			} else
				section = subSection;
		}
		
		var key = path.substring(i2);
		
		if(section == this) {
			var result = new MemorySection(this, key);
			this.map.put(key, result);
			return result;
		}
		if(section instanceof ConfigSection configSection)
			return configSection.createSection(key);
		return null;
	}
	
	private Object getObject(Object section, String node, Object def) {
		var listIndex = StringUtils.LIST_INDEX.matcher(node);
		
		if(!listIndex.matches())
			// Not indexed
			return this.getObjectRaw(section, node, def);
		
		// Indexed
		var object = findIndexed(section, listIndex.group(1), Integer.parseInt(listIndex.group(2)));
		
		return object != null ? object : def;
	}
	
	@SuppressWarnings("unchecked")
	private Object getObjectRaw(Object section, String node, Object def) {
		if(section == this)
			section = this.map;
		
		return switch(section) {
			case ConfigSection configSection -> configSection.get(node, def);
			case ConfigSerializable serializable -> serializable.serialize().getOrDefault(node, def);
			case Map<?, ?> m -> ((Map<?, Object>) m).getOrDefault(node, def);
			default -> def;
		};
	}
	
	@SuppressWarnings("unchecked")
	private <K, V> void setObject(Object section, K node, V value) {
		var listIndex = StringUtils.LIST_INDEX.matcher((CharSequence) node);
		
		if(!listIndex.matches()) {
			// Not indexed
			this.setObjectRaw(section, node, value);
		} else {
			// Indexed
			Object it = null;
			var iterableNode = listIndex.group(1);
			
			if(iterableNode != null && !iterableNode.isEmpty())
				it = find(section, iterableNode);
			
			if(it instanceof MemorySection memorySection)
				it = memorySection.map;
			else if(it instanceof ConfigSection configSection)
				it = configSection.getValues(false);
			
			if(it != null) {
				var index = Integer.parseInt(listIndex.group(2));
				
				switch(it) {
					case Map<?, ?> m -> {
						var itMap = (Map<K, V>) m;
						var len = itMap.size();
						index = asListIndex(index, len);
						if(index >= 0 && index < len) {
							K key = null;
							var iterator = itMap.keySet().iterator();
							var j = -1;
							while(iterator.hasNext() && ++j <= index)
								key = iterator.next();
							this.setObjectRaw(section, key, value);
						}
					}
					case List<?> l -> {
						var list = (List<V>) l;
						var len = list.size();
						
						if(value == null && index == -1 && !list.isEmpty())
							list.remove(len - 1);
						else if(value != null && (index == -1 || index == len))
							list.add(value);
						else {
							index = asListIndex(index, len);
							if(index >= 0 && index < len) {
								if(value == null)
									list.remove(index);
								else
									list.set(index, value);
							}
						}
					}
					case Collection<?> c when value != null -> {
						var collection = (Collection<V>) c;
						var len = collection.size();
						
						if(index == -1 || index == 0 || index == len)
							collection.add(value);
					}
					default -> {
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <K, V> void setObjectRaw(Object section, K key, V value) {
		if(key != null) {
			if(section == this)
				section = this.map;
			if(section instanceof ConfigSection configSection)
				configSection.set(String.valueOf(key), value);
			else if(section instanceof Map) {
				if(value == null)
					((Map<K, V>) section).remove(key);
				else
					((Map<K, V>) section).put(key, value);
			}
		}
	}
	
	private Object getSection(Object parent, String node) {
		var listIndex = StringUtils.LIST_INDEX.matcher(node);
		
		if(!listIndex.matches())
			// Not indexed
			return findSection(parent, node);
		
		// Indexed
		var section = findIndexed(parent, listIndex.group(1), Integer.parseInt(listIndex.group(2)));
		
		return isSection(section) ? section : null;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()
		  + "[path='" + this.getPath()
		  + "', root='" + (root == null ? null : root.getClass().getSimpleName()) + "']";
	}
	
	protected Object getDefault(@NonNull String path) {
		var defaults = root == null ? null : root.getDefaults();
		return defaults == null ? null : defaults.get(ConfigSection.createPath(this, path));
	}
	
	protected void mapChildrenKeys(Set<String> output, ConfigSection section, boolean deep) {
		if(section instanceof MemorySection sec) {
			for(var entry : sec.map.entrySet()) {
				output.add(ConfigSection.createPath(section, entry.getKey(), this));
				
				if(deep && entry.getValue() instanceof ConfigSection subsection)
					this.mapChildrenKeys(output, subsection, true);
			}
		} else {
			var keys = section.getKeys(deep);
			
			for(var key : keys)
				output.add(ConfigSection.createPath(section, key, this));
		}
	}
	
	protected void mapChildrenValues(Map<String, Object> output, ConfigSection section, boolean deep) {
		if(section instanceof MemorySection sec) {
			for(var entry : sec.map.entrySet()) {
				output.put(ConfigSection.createPath(section, entry.getKey(), this), entry.getValue());
				
				if(deep && entry.getValue() instanceof ConfigSection sub)
					this.mapChildrenValues(output, sub, true);
			}
		} else {
			var values = section.getValues(deep);
			
			for(var entry : values.entrySet())
				output.put(ConfigSection.createPath(section, entry.getKey(), this), entry.getValue());
		}
	}
}
