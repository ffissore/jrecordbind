package it.assist.jrecordbind.converters;

import it.assist.jrecordbind.Converter;

public class StringConverter implements Converter<String> {

  @Override
  public String convert(String value) {
    return value;
  }

}
