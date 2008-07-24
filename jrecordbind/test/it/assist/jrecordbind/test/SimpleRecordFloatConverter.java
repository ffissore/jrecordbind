package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

public class SimpleRecordFloatConverter implements Converter<Float> {

  @Override
  public Float convert(String value) {
    assert value.length() == 3;
    return Float.valueOf(value.substring(0, 1) + "." + value.substring(1));
  }

  @Override
  public String toString(Object value) {
    String f = value.toString();

    return f.toString().substring(f.indexOf(".") - 1, f.indexOf("."))
        + f.toString().substring(f.indexOf(".") + 1, f.indexOf(".") + 3);
  }

}
