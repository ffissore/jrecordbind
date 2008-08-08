package it.assist.jrecordbind;

public interface Converter {

  Object convert(String value);

  String toString(Object value);

}
