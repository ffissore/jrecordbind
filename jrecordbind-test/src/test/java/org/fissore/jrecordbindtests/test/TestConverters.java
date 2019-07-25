package org.fissore.jrecordbindtests.test;

import org.fissore.jrecordbind.Converter;
import org.jrecordbind.schemas.jrb._enum.CarType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TestConverters {

  public static class BigDecimalConverter implements Converter {

    public Object convert(String value) {
      return new BigDecimal(value.trim());
    }

    public String toString(Object value) {
      return ((BigDecimal) value).toPlainString();
    }

  }

  public static class CarTypeConverter implements Converter {

    @Override
    public Object convert(String value) {
      if (value == null || value.isEmpty()) {
        return null;
      }
      return CarType.fromValue(value);
    }

    @Override
    public String toString(Object value) {
      if (value == null) {
        return "";
      }
      return value.toString();
    }
  }

  public static class MyEnumConverter implements Converter {

    public Object convert(String value) {
      return TestTypes.MyEnum.valueOf(value.trim());
    }

    public String toString(Object value) {
      return ((TestTypes.MyEnum) value).name();
    }

  }

  public static class MyOtherEnumConverter implements Converter {

    public Object convert(String value) {
      return TestTypes.MyOtherEnum.valueOf(value.trim());
    }

    public String toString(Object value) {
      if (value == null) {
        return "";
      }
      return ((TestTypes.MyOtherEnum) value).name();
    }

  }

  public static class SimpleRecordDateConverter implements Converter {

    private final SimpleDateFormat simpleDateFormat;

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

  public static class SimpleRecordFloatConverter implements Converter {

    private final DecimalFormat decimalFormat;

    public SimpleRecordFloatConverter() {
      this.decimalFormat = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    }

    public Object convert(String value) {
      return Float.valueOf(value.substring(0, value.length() - 2) + "." + value.substring(value.length() - 2));
    }

    public String toString(Object value) {
      if (value == null) {
        return "";
      }

      return decimalFormat.format(value).replace(".", "");
    }

  }

  public static class YNBooleanConverter implements Converter {

    public Object convert(String value) {
      return "Y".equals(value);
    }

    public String toString(Object value) {
      return (boolean) value ? "Y" : "N";
    }

  }

}
