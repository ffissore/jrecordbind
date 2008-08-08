package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class StringConverter implements Converter {

  public Object convert(String value) {
    return value;
  }

  public String toString(Object value) {
    return value.toString();
  }

}
