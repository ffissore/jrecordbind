package it.assist.jarb.converters;

import it.assist.jarb.Converter;

public class StringConverter implements Converter<String> {

  @Override
  public String convert(String value) {
    return value;
  }

}
