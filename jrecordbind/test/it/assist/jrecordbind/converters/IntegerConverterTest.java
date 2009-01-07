package it.assist.jrecordbind.converters;

import junit.framework.TestCase;

public class IntegerConverterTest extends TestCase {

  private IntegerConverter converter;

  public IntegerConverterTest() {
    converter = new IntegerConverter();
  }

  public void testConvert() {
    assertEquals(Integer.valueOf(90), converter.convert("90"));
    assertEquals(Integer.valueOf(-1), converter.convert("-1"));
    assertNull(converter.convert(null));
    assertNull(converter.convert(""));
  }

  public void testToStringObject() {
    assertEquals("90", converter.toString(Integer.valueOf(90)));
    assertEquals("-1", converter.toString(Integer.valueOf(-1)));
    assertEquals("", converter.toString(null));
  }

}
