package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class StringConverter implements Converter<String> {

  @Override
  public String convert(String value) {
    return value;
  }

  @Override
  public String toString(Object value) {
    return value.toString();
  }

}
