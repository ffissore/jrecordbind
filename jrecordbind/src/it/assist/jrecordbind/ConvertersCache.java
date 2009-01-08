package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.List;

/**
 * Converters used in a fixed-length file definition are instanciated and cached
 * here
 * 
 * @author Federico Fissore
 */
class ConvertersCache extends Cache<Converter> {

  public ConvertersCache(RecordDefinition definition) {
    List<Property> properties = collectProperties(definition);
    for (Property property : properties) {
      if (!containsKey(property.getConverter())) {
        try {
          put(property.getConverter(), (Converter) Class.forName(property.getConverter()).newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
