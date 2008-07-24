package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleRecordDateConverter implements Converter<Date> {

  private SimpleDateFormat simpleDateFormat;

  public SimpleRecordDateConverter() {
    simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
  }

  @Override
  public Date convert(String value) {
    try {
      return simpleDateFormat.parse(value);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString(Object value) {
    return simpleDateFormat.format((Date) value);
  }

}
