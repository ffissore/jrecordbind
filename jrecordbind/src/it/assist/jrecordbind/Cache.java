package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

abstract class Cache<E> extends HashMap<String, E> {

  protected List<Property> collectProperties(RecordDefinition definition) {
    LinkedList<Property> properties = new LinkedList<Property>(definition.getProperties());
    for (RecordDefinition subDefinition : definition.getSubRecords()) {
      properties.addAll(collectProperties(subDefinition));
    }
    return properties;
  }

}
