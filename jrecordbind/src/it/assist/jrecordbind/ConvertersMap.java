package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class ConvertersMap extends HashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  public ConvertersMap(RecordDefinition definition) {
    List<Property> properties = collectProperties(definition);
    for (Property property : properties) {
      if (!containsKey(property.getConverter())) {
        try {
          put(property.getConverter(), Class.forName(property.getConverter()).newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private List<Property> collectProperties(RecordDefinition definition) {
    LinkedList<Property> properties = new LinkedList<Property>(definition.getProperties());
    for (RecordDefinition subDefinition : definition.getSubRecords()) {
      properties.addAll(collectProperties(subDefinition));
    }
    return properties;
  }
}
