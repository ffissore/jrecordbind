package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

public class SimpleRecordFloatConverter implements Converter {

  public Object convert(String value) {
    return Float.valueOf(value.substring(0, 1) + "." + value.substring(1));
  }

  public String toString(Object value) {
    String f = value.toString();

    return f.substring(f.indexOf(".") - 1, f.indexOf(".")) + f.substring(f.indexOf(".") + 1, f.indexOf(".") + 3);
  }

}
