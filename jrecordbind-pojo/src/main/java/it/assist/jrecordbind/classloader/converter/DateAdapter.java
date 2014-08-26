package it.assist.jrecordbind.classloader.converter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

public class DateAdapter extends XmlAdapter<String, Calendar> {

  public Calendar unmarshal(String value) {
    return DatatypeConverter.parseDate(value);
  }

  public String marshal(Calendar value) {
    if (value == null) {
      return null;
    }
    return DatatypeConverter.printDate(value);
  }

}

