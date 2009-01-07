package it.assist.jrecordbind.converters;

import junit.framework.TestCase;

public class VoidConverterTest extends TestCase {

  private VoidConverter converter;

  public VoidConverterTest() {
    converter = new VoidConverter();
  }

  public void testConvert() {
    assertNull(converter.convert(""));
    assertNull(converter.convert("try"));
    assertNull(converter.convert(null));
  }

  public void testToStringObject() {
    assertEquals("", converter.toString(""));
    assertEquals("", converter.toString("try"));
    assertEquals("", converter.toString(null));
  }

}
