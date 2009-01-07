package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class LongConverter implements Converter {

  public Object convert(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return Long.valueOf(value);
  }

  public String toString(Object value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

}
