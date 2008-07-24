package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class IntegerConverter implements Converter<Integer> {

  @Override
  public Integer convert(String value) {
    return Integer.valueOf(value);
  }

  @Override
  public String toString(Object value) {
    return value.toString();
  }

}
