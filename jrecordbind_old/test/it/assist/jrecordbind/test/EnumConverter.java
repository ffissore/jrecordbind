package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

public class EnumConverter implements Converter {

  @Override
  public Object convert(String value) {
    return MyEnum.valueOf(value.trim());
  }

  @Override
  public String toString(Object value) {
    return ((MyEnum) value).name();
  }

}
