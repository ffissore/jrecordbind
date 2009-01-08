package it.assist.jrecordbind;

/**
 * Use when marshalling: some records require data to be left or right aligned.
 * By default the {@link Marshaller} will left align. If you need a different
 * padding, you can specify it by implementing this interface
 * 
 * @author Federico Fissore
 */
public interface Padder {

  /**
   * Pads a string to the given length
   * 
   * @param string
   *          the string to pad
   * @param length
   *          the length of the final string
   * @return the padded string
   */
  String pad(String string, int length);

}
