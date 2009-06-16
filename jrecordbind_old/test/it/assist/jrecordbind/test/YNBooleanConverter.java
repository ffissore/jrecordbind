package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

public class YNBooleanConverter implements Converter {

  @Override
  public Object convert(String value) {
    if (value == null || value.isEmpty()) {
      return Boolean.FALSE;
    }
    return Boolean.valueOf(value);
  }

  @Override
  public String toString(Object value) {
    boolean b = ((Boolean) value).booleanValue();
    return b ? "Y" : "N";
  }

}
