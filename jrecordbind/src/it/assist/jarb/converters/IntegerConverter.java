package it.assist.jarb.converters;

import it.assist.jarb.Converter;

public class IntegerConverter implements Converter<Integer> {

  @Override
  public Integer convert(String value) {
    return Integer.valueOf(value);
  }

}
