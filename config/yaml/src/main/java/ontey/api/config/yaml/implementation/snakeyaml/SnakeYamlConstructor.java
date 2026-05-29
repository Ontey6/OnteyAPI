package ontey.api.config.yaml.implementation.snakeyaml;

import ontey.api.config.serialization.ConfigSerialization;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlConstructor.java">Bukkit Source</a>
 */

public class SnakeYamlConstructor extends SafeConstructor {
	
	public SnakeYamlConstructor(LoaderOptions loaderOptions) {
		super(loaderOptions);
		this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
	}
	
	@Override
	public void flattenMapping(MappingNode node) {
		super.flattenMapping(node);
	}
	
	public Object construct(Node node) {
		return super.constructObject(node);
	}
	
	protected boolean hasSerializedTypeKey(MappingNode node) {
		for(NodeTuple nodeTuple : node.getValue()) {
			Node keyNode = nodeTuple.getKeyNode();
			if(keyNode instanceof ScalarNode scalar) {
				String key = scalar.getValue();
				if(key.equals(ConfigSerialization.SERIALIZED_TYPE_KEY))
					return true;
			}
		}
		return false;
	}
	
	private final class ConstructCustomObject extends SafeConstructor.ConstructYamlMap {
		
		@Override
		public Object construct(Node node) {
			if(node.isTwoStepsConstruction())
				throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
			
			Map<?, ?> raw = (Map<?, ?>) super.construct(node);
			if(!raw.containsKey(ConfigSerialization.SERIALIZED_TYPE_KEY))
				return raw;
			
			Map<String, Object> typed = new LinkedHashMap<>(raw.size());
			for(var entry : raw.entrySet())
				typed.put(entry.getKey().toString(), entry.getValue());
			
			try {
				return ConfigSerialization.deserializeObject(typed);
			} catch(IllegalArgumentException ex) {
				throw new YAMLException("Could not deserialize object", ex);
			}
		}
		
		@Override
		public void construct2ndStep(Node node, Object object) {
			throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
		}
	}
}
