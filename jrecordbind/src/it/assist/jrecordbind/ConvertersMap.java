package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class ConvertersMap extends HashMap {

  private static final long serialVersionUID = 1L;

  public ConvertersMap(List properties) {
    for (Iterator iter = properties.iterator(); iter.hasNext();) {
      Property property = (Property) iter.next();
      if (!containsKey(property.getConverter())) {
        try {
          put(property.getConverter(), Class.forName(property.getConverter()).newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
