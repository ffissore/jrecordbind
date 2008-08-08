package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class IntegerConverter implements Converter {

  public Object convert(String value) {
    return Integer.valueOf(value);
  }

  public String toString(Object value) {
    return value.toString();
  }

}
