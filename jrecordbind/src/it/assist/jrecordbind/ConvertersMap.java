package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ConvertersMap extends HashMap<String, Converter<?>> {

  private static final long serialVersionUID = 1L;

  public ConvertersMap(List<Property> properties) {
    for (Iterator<Property> iter = properties.iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (!containsKey(property.getConverter())) {
        try {
          put(property.getConverter(), (Converter<?>) Class.forName(property.getConverter()).newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
