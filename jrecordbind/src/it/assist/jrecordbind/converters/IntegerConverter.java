package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class IntegerConverter implements Converter {

  public Object convert(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return Integer.valueOf(value);
  }

  public String toString(Object value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

}
