package ontey.api.config.util;

import ontey.api.config.ConfigSection;

import java.util.Map;

public final class SectionUtils {
	
	public static void addMapToSection(Map<?, ?> values, ConfigSection section) {
		if(values == null)
			return;
		
		for(var entry : values.entrySet()) {
			Object keyObject = entry.getKey();
			String key = keyObject == null ? "" : keyObject.toString();
			final Object value = entry.getValue();
			
			if(value instanceof Map<?, ?> map)
				addMapToSection(map, section.createSection(key));
			else
				section.set(key, value);
		}
	}
}
