package org.fissore.jrecordbind.example;

import org.fissore.jrecordbind.Converter;

public class YNBooleanConverter implements Converter {

  public Object convert(String value) {
    if (value == null || value.length() == 0) {
      return Boolean.FALSE;
    }
    return "Y".equals(value);
  }

  public String toString(Object value) {
    boolean b = (Boolean) value;
    return b ? "Y" : "N";
  }

}
