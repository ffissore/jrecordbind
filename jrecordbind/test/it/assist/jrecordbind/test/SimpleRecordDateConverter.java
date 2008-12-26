package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SimpleRecordDateConverter implements Converter {

  private SimpleDateFormat simpleDateFormat;

  public SimpleRecordDateConverter() {
    simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
  }

  public Object convert(String value) {
    try {
      Calendar instance = Calendar.getInstance();
      instance.setTime(simpleDateFormat.parse(value));
      return instance;
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public String toString(Object value) {
    return simpleDateFormat.format(((Calendar) value).getTime());
  }

}
