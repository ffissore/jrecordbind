package it.assist.jarb.test;

import it.assist.jarb.Converter;

public class SimpleRecordFloatConverter implements Converter<Float> {

  @Override
  public Float convert(String value) {
    assert value.length() == 3;
    return Float.valueOf(value.substring(0, 1) + "." + value.substring(1));
  }

}
