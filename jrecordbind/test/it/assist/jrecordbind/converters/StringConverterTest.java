package it.assist.jrecordbind.converters;

import junit.framework.TestCase;

public class StringConverterTest extends TestCase {

  private StringConverter converter;

  public StringConverterTest() {
    converter = new StringConverter();
  }

  public void testConvert() {
    assertEquals("", converter.convert(""));
    assertEquals("try", converter.convert("try"));
    assertNull(converter.convert(null));
  }

  public void testToStringObject() {
    assertEquals("", converter.toString(""));
    assertEquals("try", converter.toString("try"));
    assertEquals("", converter.toString(null));
  }

}
