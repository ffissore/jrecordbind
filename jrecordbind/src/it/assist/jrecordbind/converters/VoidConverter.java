package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class VoidConverter implements Converter {

  public Object convert(String value) {
    return null;
  }

  public String toString(Object value) {
    return "";
  }

}
