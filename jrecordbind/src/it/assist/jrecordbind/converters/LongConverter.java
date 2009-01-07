package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class LongConverter implements Converter {

  public Object convert(String value) {
    return Long.valueOf(value);
  }

  public String toString(Object value) {
    return value.toString();
  }

}
