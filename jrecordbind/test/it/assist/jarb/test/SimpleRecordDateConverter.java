package it.assist.jarb.test;

import it.assist.jarb.Converter;

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

}