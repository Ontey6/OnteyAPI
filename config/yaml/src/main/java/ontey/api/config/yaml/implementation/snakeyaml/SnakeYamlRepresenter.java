package ontey.api.config.yaml.implementation.snakeyaml;

import ontey.api.config.ConfigSection;
import ontey.api.config.serialization.ConfigSerializable;
import ontey.api.config.serialization.ConfigSerialization;
import ontey.api.config.yaml.implementation.api.QuoteValue;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlRepresenter.java">Bukkit Source</a>
 */

public class SnakeYamlRepresenter extends Representer {
	
	private final DumperOptions dumperOptions;
	
	public SnakeYamlRepresenter(DumperOptions dumperOptions) {
		super(dumperOptions);
		this.dumperOptions = dumperOptions;
		this.multiRepresenters.put(ConfigSection.class, new RepresentConfigurationSection());
		this.multiRepresenters.put(ConfigSerializable.class, new RepresentConfigurationSerializable());
		this.multiRepresenters.put(QuoteValue.class, new RepresentQuoteValue());
	}
	
	protected final DumperOptions getDumperOptions() {
		return this.dumperOptions;
	}
	
	private final class RepresentConfigurationSection extends RepresentMap {
		
		@Override
		public Node representData(Object data) {
			return super.representData(((ConfigSection) data).getValues(false));
		}
	}
	
	private final class RepresentConfigurationSerializable extends RepresentMap {
		
		@Override
		public Node representData(Object data) {
			final ConfigSerializable serializable = (ConfigSerializable) data;
			final Map<String, Object> values = new LinkedHashMap<>();
			values.put(ConfigSerialization.SERIALIZED_TYPE_KEY, ConfigSerialization.getAlias(serializable.getClass()));
			values.putAll(serializable.serialize());
			
			return super.representData(values);
		}
	}
	
	private final class RepresentQuoteValue implements Represent {
		
		@Override
		public Node representData(Object data) {
			final QuoteValue<?> quoteValue = (QuoteValue<?>) data;
			
			final DumperOptions.ScalarStyle quoteScalarStyle = SnakeYamlQuoteValue.getQuoteScalarStyle(quoteValue.quoteStyle());
			final Object value = quoteValue.value();
			
			if(value == null) {
				return representScalar(Tag.NULL, "", quoteScalarStyle);
			}
			
			DumperOptions.ScalarStyle defaultScalarStyle = getDefaultScalarStyle();
			setDefaultScalarStyle(quoteScalarStyle); // change default scalar style
			
			final Node node = SnakeYamlRepresenter.this.representData(value);
			
			setDefaultScalarStyle(defaultScalarStyle); // restore default scalar style
			
			return node;
		}
	}
}
