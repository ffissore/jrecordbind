package org.fissore.jrecordbind.example;

import org.fissore.jrecordbind.Converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FloatConverter implements Converter {

  private final DecimalFormat decimalFormat;

  public FloatConverter() {
    decimalFormat = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
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
