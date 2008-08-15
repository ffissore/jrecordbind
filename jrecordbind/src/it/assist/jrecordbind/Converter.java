package it.assist.jrecordbind;

/**
 * Used by {@link Marshaller} and {@link Unmarshaller} to convert a String into an object and back. Must have a default constructor
 * 
 * @author Federico Fissore
 */
public interface Converter {

  /**
   * Converts a String into an object, e.g. a Date
   * @param value the string to convert
   * @return an instance of some kind
   */
  Object convert(String value);

  /**
   * Converts an object into string
   * @param value the object to convert
   * @return a string rapresentation of the object
   */
  String toString(Object value);

}
