package org.fissore.jrecordbind.example;

import org.fissore.jrecordbind.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateConverter implements Converter {

  private SimpleDateFormat simpleDateFormat;

  public DateConverter(String format) {
    simpleDateFormat = new SimpleDateFormat(format);
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
