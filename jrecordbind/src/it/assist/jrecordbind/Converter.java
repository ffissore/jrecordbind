package it.assist.jrecordbind;

public interface Converter<E> {

  E convert(String value);

  String toString(Object value);

}
