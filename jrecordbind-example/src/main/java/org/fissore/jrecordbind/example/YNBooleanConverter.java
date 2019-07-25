package org.fissore.jrecordbind.example;

import org.fissore.jrecordbind.Converter;

public class YNBooleanConverter implements Converter {

  public Object convert(String value) {
    return "Y".equals(value);
  }

  public String toString(Object value) {
    return (boolean) value ? "Y" : "N";
  }

}
